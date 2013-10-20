package ch.ethz.mlmq.client;

import java.io.IOException;
import java.net.ConnectException;
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
	private BrokerDto defaultBroker;
	private ClientConnection connection;

	private final String name;
	private ClientConfiguration config;

	public ClientImpl(String name, BrokerDto defaultBroker, long responseTimeoutTime) throws IOException {
		this.name = name;
		this.defaultBroker = defaultBroker;
		this.connection = new ClientConnection(defaultBroker.getHost(), defaultBroker.getPort(), responseTimeoutTime);
	}

	public ClientImpl(ClientConfiguration config) throws IOException {
		this(config.getName(), new BrokerDto(config.getBrokerHost(), config.getBrokerPort()), config.getResponseTimeoutTime());
		this.config = config;
	}

	public void init() throws IOException {
		connection.connect();
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
		return sendRequestToBroker(request, defaultBroker);
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

	@Override
	public void startup() throws InterruptedException, IOException {
		for (int i = 0; !isConnected() && (i < config.getNumberOfConnectionAtempts() || config.getNumberOfConnectionAtempts() == -1); i++) {
			try {
				logger.info("Client init");
				init();
			} catch (ConnectException ex) {
				logger.warning("Could not connect to Broker " + config.getBrokerHost() + ":" + config.getBrokerPort());
				Thread.sleep(config.getReconnectSleepTime());
			}
		}
		logger.info("Client started");
	}

}
