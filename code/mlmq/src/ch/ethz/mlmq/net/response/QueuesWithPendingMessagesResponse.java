package ch.ethz.mlmq.net.response;

import java.util.List;

import ch.ethz.mlmq.dto.QueueDto;

public class QueuesWithPendingMessagesResponse implements Response {

	private static final long serialVersionUID = 4798365845603757482L;
	
	private List<QueueDto> queues;

	public List<QueueDto> getQueues() {
		// TODO Auto-generated method stub
		return queues;
	}

}
