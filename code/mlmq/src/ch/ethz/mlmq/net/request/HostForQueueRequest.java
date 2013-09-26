package ch.ethz.mlmq.net.request;

import ch.ethz.mlmq.dto.QueueDto;

public class HostForQueueRequest implements Request {

	private static final long serialVersionUID = 847237023458857614L;

	private QueueDto queue;

	public HostForQueueRequest(long queueId) {
		setQueue(new QueueDto(queueId));
	}

	public QueueDto getQueue() {
		return queue;
	}

	private void setQueue(QueueDto queue) {
		this.queue = queue;
	}

}
