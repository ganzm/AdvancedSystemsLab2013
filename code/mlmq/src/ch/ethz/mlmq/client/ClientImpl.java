package ch.ethz.mlmq.client;

import java.util.HashMap;
import java.util.List;

import ch.ethz.mlmq.dto.BrokerDto;
import ch.ethz.mlmq.dto.ClientDto;
import ch.ethz.mlmq.dto.MessageDto;
import ch.ethz.mlmq.dto.MessageQueryInfoDto;
import ch.ethz.mlmq.dto.QueueDto;
import ch.ethz.mlmq.net.Connection;
import ch.ethz.mlmq.net.ConnectionPool;
import ch.ethz.mlmq.net.request.CreateQueueRequest;
import ch.ethz.mlmq.net.request.DeleteQueueRequest;
import ch.ethz.mlmq.net.request.DequeueMessageRequest;
import ch.ethz.mlmq.net.request.HostForQueueRequest;
import ch.ethz.mlmq.net.request.PeekMessageRequest;
import ch.ethz.mlmq.net.request.QueueRequest;
import ch.ethz.mlmq.net.request.QueuesWithPendingMessagesRequest;
import ch.ethz.mlmq.net.request.RegistrationRequest;
import ch.ethz.mlmq.net.request.Request;
import ch.ethz.mlmq.net.request.SendMessageRequest;
import ch.ethz.mlmq.net.response.CreateQueueResponse;
import ch.ethz.mlmq.net.response.ExceptionResponse;
import ch.ethz.mlmq.net.response.HostForQueueResponse;
import ch.ethz.mlmq.net.response.MessageResponse;
import ch.ethz.mlmq.net.response.QueuesWithPendingMessagesResponse;
import ch.ethz.mlmq.net.response.RegistrationResponse;
import ch.ethz.mlmq.net.response.Response;

class ClientImpl implements Client {

	private ClientDto registeredAs;
	private HashMap<Long, BrokerDto> hostCache = new HashMap<Long, BrokerDto>();
	private ConnectionPool brokerConnections;
	private BrokerDto defaultBroker;

	public ClientImpl(BrokerDto defaultBroker) {
		this.defaultBroker = defaultBroker;
		registeredAs = register();
		if (registeredAs == null)
			throw new RuntimeException("W00t");
	}

	private Response sendRequest(QueueRequest request) {
		return sendRequestToBroker(request, hostForQueue(request.getQueueId()));
	}

	private Response sendRequest(Request request) {
		return sendRequestToBroker(request, defaultBroker);
	}

	/**
	 * Sends a request to a specific broker.
	 * 
	 * @param request
	 * @param broker
	 * @return
	 */
	private Response sendRequestToBroker(Request request, BrokerDto broker) {
		Connection c = brokerConnections.getConnection(broker);
		Response response = c.submitRequest(request);

		if (response instanceof ExceptionResponse) {
			ExceptionResponse r = (ExceptionResponse) response;
			Exception e = r.getException();
			if (e != null)
				throw new RuntimeException("W00t");
			// TBD: Exception Handling
			// throw e;
		}

		return response;
	}

	private BrokerDto hostForQueue(long queueId) {
		if (hostCache.containsKey(queueId))
			return hostCache.get(queueId);

		HostForQueueResponse response = (HostForQueueResponse) sendRequest(new HostForQueueRequest(
				queueId));
		hostCache.put(queueId, response.getBrokerDto());

		return hostForQueue(queueId);
	}

	@Override
	public ClientDto register() {
		RegistrationResponse repsonse = (RegistrationResponse) sendRequest(new RegistrationRequest());
		return repsonse.getClientDto();
	}

	@Override
	public QueueDto createQueue() {
		CreateQueueResponse repsonse = (CreateQueueResponse) sendRequest(new CreateQueueRequest());
		return repsonse.getQueueDto();
	}

	@Override
	public void deleteQueue(long id) {
		sendRequest(new DeleteQueueRequest(id));
	}

	@Override
	public void sendMessage(long queueId, byte[] content, int prio) {
		sendRequest(new SendMessageRequest(queueId, content, prio));
	}

	@Override
	public void sendMessage(long[] queueIds, byte[] content, int prio) {
		for (long q : queueIds) {
			sendMessage(q, content, prio);
		}
	}

	@Override
	public MessageDto peekMessage(MessageQueryInfoDto messageQueryInfo) {
		MessageResponse response = (MessageResponse) sendRequest(new PeekMessageRequest(
				messageQueryInfo));
		return response.getMessageDto();
	}

	@Override
	public MessageDto dequeueMessage(MessageQueryInfoDto messageQueryInfo) {
		MessageResponse response = (MessageResponse) sendRequest(new DequeueMessageRequest(
				messageQueryInfo));
		return response.getMessageDto();
	}

	@Override
	public List<QueueDto> queuesWithPendingMessages() {
		QueuesWithPendingMessagesResponse response = (QueuesWithPendingMessagesResponse) sendRequest(new QueuesWithPendingMessagesRequest());
		return response.getQueues();
	}

}
