package ch.ethz.mlmq.http.request;

import ch.ethz.mlmq.dto.QueueDto;

public class HostForQueueRequest implements Request {

	private QueueDto queue;

	public HostForQueueRequest(long queueId) {
		queue = new QueueDto(queueId);
	}

}
