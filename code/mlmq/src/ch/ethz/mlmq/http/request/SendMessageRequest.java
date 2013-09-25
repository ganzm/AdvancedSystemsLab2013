package ch.ethz.mlmq.http.request;

import java.util.List;

import ch.ethz.mlmq.dto.QueueDto;

public class SendMessageRequest implements QueueRequest {

	private long queueId;

	public SendMessageRequest(long queueId, byte[] content, int prio) {
		this.queueId = queueId;
	}

	@Override
	public long getQueueId() {
		return queueId;
	}

}
