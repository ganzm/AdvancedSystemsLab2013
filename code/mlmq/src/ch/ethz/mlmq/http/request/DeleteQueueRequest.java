package ch.ethz.mlmq.http.request;

import ch.ethz.mlmq.dto.QueueDto;

public class DeleteQueueRequest implements Request {

	private QueueDto queue;

	public DeleteQueueRequest(long queueId) {
		queue = new QueueDto(queueId);
	}
}
