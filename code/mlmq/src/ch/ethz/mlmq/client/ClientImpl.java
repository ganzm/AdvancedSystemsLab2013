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
import ch.ethz.mlmq.net.request.PeekMessageRequest;
import ch.ethz.mlmq.net.request.QueuesWithPendingMessagesRequest;
import ch.ethz.mlmq.net.request.RegistrationRequest;
import ch.ethz.mlmq.net.request.Request;
import ch.ethz.mlmq.net.request.SendClientMessageRequest;
import ch.ethz.mlmq.net.request.SendMessageRequest;
import ch.ethz.mlmq.net.response.CreateQueueResponse;
import ch.ethz.mlmq.net.response.ExceptionResponse;
import ch.ethz.mlmq.net.response.MessageResponse;
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
	 * defines how many times we try to connect to a broker at startup
	 */
	private int numberOfInitialReconnects = 10;

	public ClientImpl(String name, BrokerDto broker, long responseTimeoutTime) {
		this.name = name;
		this.broker = broker;
		this.connection = new ClientConnection(broker.getHost(), broker.getPort(), responseTimeoutTime);
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

	private Response sendRequest(Request request) throws IOException {
		return sendRequestToBroker(request, broker);
	}

	/**
	 * Sends a request to a specific broker.
	 * 
	 * @param request
	 * @param broker
	 * @param timeout
	 * @return
	 * @throws IOException
	 * @throws MlmqException
	 */
	private Response sendRequestToBroker(Request request, BrokerDto broker) throws IOException {
		Response response = connection.submitRequest(request);

		if (response instanceof ExceptionResponse) {
			ExceptionResponse r = (ExceptionResponse) response;
			Exception e = r.getException();
			if (e != null) {
				throw new IOException("Go ExceptionResponse from Server " + e.getMessage(), e);
			}
		}

		return response;
	}

	@Override
	public ClientDto register() throws IOException {
		RegistrationResponse repsonse = (RegistrationResponse) sendRequest(new RegistrationRequest(name));
		registeredAs = repsonse.getClientDto();
		return registeredAs;
	}

	@Override
	public QueueDto createQueue(String queueName) throws IOException {
		CreateQueueResponse repsonse = (CreateQueueResponse) sendRequest(new CreateQueueRequest(queueName));
		return repsonse.getQueueDto();
	}

	@Override
	public void deleteQueue(long id) throws IOException {
		sendRequest(new DeleteQueueRequest(id));
	}

	@Override
	public void sendMessage(long queueId, byte[] content, int prio) throws IOException {
		sendRequest(new SendMessageRequest(queueId, content, prio));
	}

	@Override
	public void sendMessage(long[] queueIds, byte[] content, int prio) throws IOException {
		for (long q : queueIds) {
			sendMessage(q, content, prio);
		}
	}

	@Override
	public MessageDto peekMessage(MessageQueryInfoDto messageQueryInfo) throws IOException {
		MessageResponse response = (MessageResponse) sendRequest(new PeekMessageRequest(messageQueryInfo));
		return response.getMessageDto();
	}

	@Override
	public MessageDto dequeueMessage(MessageQueryInfoDto messageQueryInfo) throws IOException {
		MessageResponse response = (MessageResponse) sendRequest(new DequeueMessageRequest(messageQueryInfo));
		return response.getMessageDto();
	}

	@Override
	public List<QueueDto> queuesWithPendingMessages() throws IOException {
		QueuesWithPendingMessagesResponse response = (QueuesWithPendingMessagesResponse) sendRequest(new QueuesWithPendingMessagesRequest());
		return response.getQueues();
	}

	@Override
	public void sendMessageToClient(long clientId, byte[] content, int prio) throws IOException {
		SendClientMessageRequest sendMessageRequest = new SendClientMessageRequest(clientId, content, prio);
		sendRequest(sendMessageRequest);
	}

	@Override
	public long sendRequestToClient(long clientId, byte[] content, int prio) throws IOException {
		SendClientMessageRequest sendMessageRequest = new SendClientMessageRequest(clientId, content, prio, true);
		SendClientMessageResponse response = (SendClientMessageResponse) sendRequest(sendMessageRequest);
		return response.getConversationContext();
	}

	@Override
	public long sendResponseToClient(long clientId, long context, byte[] content, int prio) throws IOException {
		SendClientMessageRequest sendMessageRequest = new SendClientMessageRequest(clientId, content, prio, context);
		SendClientMessageResponse response = (SendClientMessageResponse) sendRequest(sendMessageRequest);
		return response.getConversationContext();
	}

}
