package ch.ethz.mlmq.net.request;

import ch.ethz.mlmq.dto.QueueDto;

public class DeleteQueueRequest implements QueueRequest {
	
	private static final long serialVersionUID = -689440274911871604L;
	
	private QueueDto queue;

	public DeleteQueueRequest(long queueId) {
		queue = new QueueDto(queueId);
	}

	@Override
	public long getQueueId() {
		return queue.getId();
	}
}
