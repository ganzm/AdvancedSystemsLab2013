package ch.ethz.mlmq.client;

import java.util.HashMap;

import ch.ethz.mlmq.dto.BrokerDto;
import ch.ethz.mlmq.dto.ClientDto;
import ch.ethz.mlmq.dto.MessageDto;
import ch.ethz.mlmq.dto.MessageQueryInfoDto;
import ch.ethz.mlmq.dto.QueueDto;
import ch.ethz.mlmq.http.request.CreateQueueRequest;
import ch.ethz.mlmq.http.request.DeleteQueueRequest;
import ch.ethz.mlmq.http.request.DequeueMessageRequest;
import ch.ethz.mlmq.http.request.HostForQueueRequest;
import ch.ethz.mlmq.http.request.PeekMessageRequest;
import ch.ethz.mlmq.http.request.RegistrationRequest;
import ch.ethz.mlmq.http.request.Request;
import ch.ethz.mlmq.http.request.SendMessageRequest;
import ch.ethz.mlmq.http.response.CreateQueueResponse;
import ch.ethz.mlmq.http.response.HostForQueueResponse;
import ch.ethz.mlmq.http.response.MessageResponse;
import ch.ethz.mlmq.http.response.RegistrationResponse;
import ch.ethz.mlmq.http.response.Response;

class ClientImpl implements Client {

	private ClientDto registeredAs;
	private HashMap<Long, BrokerDto> hostCache = new HashMap<Long, BrokerDto>();

	public ClientImpl() {
		registeredAs = register();
	}

	private Response sendRequest(Request request) {
		// TODO: implement this
		return null;
	}

	private BrokerDto hostForQueue(long queueId) {
		if(hostCache.containsKey(queueId))
			return hostCache.get(queueId);
		
		HostForQueueResponse response = (HostForQueueResponse) sendRequest(new HostForQueueRequest(queueId));
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
		sendMessage(new long[] { queueId }, content, prio);
	}

	@Override
	public void sendMessage(long[] queueIds, byte[] content, int prio) {
		sendRequest(new SendMessageRequest(queueIds, content, prio));
	}

	@Override
	public MessageDto peekMessage(MessageQueryInfoDto messageQueryInfo) {
		MessageResponse response = (MessageResponse) sendRequest(new PeekMessageRequest(messageQueryInfo));
		return response.getMessageDto();
	}

	@Override
	public MessageDto dequeueMessage(MessageQueryInfoDto messageQueryInfo) {
		MessageResponse response = (MessageResponse) sendRequest(new DequeueMessageRequest(messageQueryInfo));
		return response.getMessageDto();
	}

}
