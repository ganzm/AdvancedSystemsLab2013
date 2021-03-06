package ch.ethz.mlmq.client;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import ch.ethz.mlmq.dto.BrokerDto;
import ch.ethz.mlmq.dto.ClientDto;
import ch.ethz.mlmq.dto.MessageDto;
import ch.ethz.mlmq.dto.MessageQueryInfoDto;
import ch.ethz.mlmq.dto.QueueDto;
import ch.ethz.mlmq.exception.MlmqException;
import ch.ethz.mlmq.net.ClientConnection;
import ch.ethz.mlmq.net.request.CreateQueueRequest;
import ch.ethz.mlmq.net.request.DeleteQueueRequest;
import ch.ethz.mlmq.net.request.DequeueMessageRequest;
import ch.ethz.mlmq.net.request.LookupClientRequest;
import ch.ethz.mlmq.net.request.LookupQueueRequest;
import ch.ethz.mlmq.net.request.PeekMessageRequest;
import ch.ethz.mlmq.net.request.QueuesWithPendingMessagesRequest;
import ch.ethz.mlmq.net.request.RegistrationRequest;
import ch.ethz.mlmq.net.request.Request;
import ch.ethz.mlmq.net.request.SendClientMessageRequest;
import ch.ethz.mlmq.net.request.SendMessageRequest;
import ch.ethz.mlmq.net.response.ClientResponse;
import ch.ethz.mlmq.net.response.ExceptionResponse;
import ch.ethz.mlmq.net.response.MessageResponse;
import ch.ethz.mlmq.net.response.QueueResponse;
import ch.ethz.mlmq.net.response.QueuesWithPendingMessagesResponse;
import ch.ethz.mlmq.net.response.RegistrationResponse;
import ch.ethz.mlmq.net.response.Response;
import ch.ethz.mlmq.net.response.SendClientMessageResponse;

public class ClientImpl implements Client {
	private static final Logger logger = Logger.getLogger(ClientImpl.class.getSimpleName());

	private ClientDto registeredAs;

	/**
	 * Broker where we are connected to
	 */
	private BrokerDto broker;
	private ClientConnection connection;

	private final String name;

	/**
	 * how long to wait before trying to connect again
	 */
	private long reconnectSleepTime = 5000;

	/**
	 * special request timeout for registration request
	 * 
	 * do not timeout registration request
	 */
	private long registrationTimeout = 10 * 60 * 1000;

	/**
	 * defines how many times we try to connect to a broker at startup
	 */
	private int numberOfInitialReconnects = 10;

	/**
	 * Defines how much time our server has to respond to our request in milliseconds
	 */
	private final long defaultResponseTimeoutTime;

	public ClientImpl(String name, BrokerDto broker, long responseTimeoutTime) {
		this.name = name;
		this.broker = broker;
		this.defaultResponseTimeoutTime = responseTimeoutTime;
		this.connection = new ClientConnection(broker.getHost(), broker.getPort());
	}

	public ClientImpl(ClientConfiguration config) {
		this(config.getName(), new BrokerDto(config.getBrokerHost(), config.getBrokerPort()), config.getResponseTimeoutTime());

		reconnectSleepTime = config.getReconnectSleepTime();
		numberOfInitialReconnects = config.getNumberOfConnectionAtempts();
	}

	public void init() throws IOException {

		for (int i = 0; !isConnected() && (i < numberOfInitialReconnects || numberOfInitialReconnects == -1); i++) {
			try {
				logger.info("Client init");
				connection.connect();
			} catch (IOException ex) {
				logger.warning("Could not connect to Broker " + broker);

				try {
					Thread.sleep(reconnectSleepTime);
				} catch (InterruptedException e) {
					logger.info("InterruptedException");
				}
			}
		}

		if (isConnected()) {
			logger.info("Client started");
		} else {
			throw new IOException("Could not connect to Broker " + broker);
		}
	}

	@Override
	public boolean isConnected() {
		return connection.isConnected();
	}

	@Override
	public void close() throws IOException {
		connection.close();
	}

	private Response sendRequest(Request request) throws IOException, MlmqException {
		return sendRequest(request, defaultResponseTimeoutTime);
	}

	/**
	 * Sends a request to the broker
	 * 
	 * @param request
	 * @return
	 * @throws IOException
	 * @throws MlmqException
	 */
	private Response sendRequest(Request request, long responseTimeoutTime) throws IOException, MlmqException {
		Response response = connection.submitRequest(request, responseTimeoutTime);

		if (response instanceof ExceptionResponse) {
			ExceptionResponse r = (ExceptionResponse) response;
			Exception e = r.getException();
			if (e != null) {
				throw new MlmqException("Go ExceptionResponse from Server " + e.getMessage(), e);
			}
		}

		return response;
	}

	@Override
	public ClientDto register() throws IOException, MlmqException {
		RegistrationResponse repsonse = (RegistrationResponse) sendRequest(new RegistrationRequest(name), registrationTimeout);
		registeredAs = repsonse.getClientDto();
		return registeredAs;
	}

	@Override
	public QueueDto createQueue(String queueName) throws IOException, MlmqException {
		QueueResponse repsonse = (QueueResponse) sendRequest(new CreateQueueRequest(queueName));
		return repsonse.getQueueDto();
	}

	@Override
	public QueueDto lookupClientQueue(long clientId) throws IOException, MlmqException {
		QueueResponse repsonse = (QueueResponse) sendRequest(new LookupQueueRequest(clientId));
		return repsonse.getQueueDto();
	}

	@Override
	public QueueDto lookupClientQueue(String queueName) throws IOException, MlmqException {
		QueueResponse repsonse = (QueueResponse) sendRequest(new LookupQueueRequest(queueName));
		return repsonse.getQueueDto();
	}

	public ClientDto lookupClient(String clientName) throws IOException, MlmqException {
		ClientResponse repsonse = (ClientResponse) sendRequest(new LookupClientRequest(clientName));
		return repsonse.getClientDto();
	}

	@Override
	public void deleteQueue(long id) throws IOException, MlmqException {
		sendRequest(new DeleteQueueRequest(id));
	}

	@Override
	public void sendMessage(long queueId, byte[] content, int prio) throws IOException, MlmqException {
		sendRequest(new SendMessageRequest(queueId, content, prio));
	}

	@Override
	public void sendMessage(long[] queueIds, byte[] content, int prio) throws IOException, MlmqException {
		for (long q : queueIds) {
			sendMessage(q, content, prio);
		}
	}

	@Override
	public MessageDto peekMessage(MessageQueryInfoDto messageQueryInfo) throws IOException, MlmqException {
		MessageResponse response = (MessageResponse) sendRequest(new PeekMessageRequest(messageQueryInfo));
		return response.getMessageDto();
	}

	@Override
	public MessageDto dequeueMessage(MessageQueryInfoDto messageQueryInfo) throws IOException, MlmqException {
		MessageResponse response = (MessageResponse) sendRequest(new DequeueMessageRequest(messageQueryInfo));
		return response.getMessageDto();
	}

	@Override
	public int queuesWithPendingMessages(List<QueueDto> queues, int maxNumQueues) throws IOException, MlmqException {
		if (queues == null) {
			throw new IllegalArgumentException("Parameter queues must not be null");
		}

		QueuesWithPendingMessagesResponse response = (QueuesWithPendingMessagesResponse) sendRequest(new QueuesWithPendingMessagesRequest(maxNumQueues));
		queues.addAll(response.getQueues());
		return response.getNumMessagesInMyQueue();

	}

	@Override
	public void sendMessageToClient(long clientId, byte[] content, int prio) throws IOException, MlmqException {
		SendClientMessageRequest sendMessageRequest = new SendClientMessageRequest(clientId, content, prio);
		sendRequest(sendMessageRequest);
	}

	@Override
	public long sendRequestToClient(long clientId, byte[] content, int prio) throws IOException, MlmqException {
		SendClientMessageRequest sendMessageRequest = new SendClientMessageRequest(clientId, content, prio, true);
		SendClientMessageResponse response = (SendClientMessageResponse) sendRequest(sendMessageRequest);
		return response.getConversationContext();
	}

	@Override
	public long sendResponseToClient(long clientId, long context, byte[] content, int prio) throws IOException, MlmqException {
		SendClientMessageRequest sendMessageRequest = new SendClientMessageRequest(clientId, content, prio, context);
		SendClientMessageResponse response = (SendClientMessageResponse) sendRequest(sendMessageRequest);
		return response.getConversationContext();
	}

}
