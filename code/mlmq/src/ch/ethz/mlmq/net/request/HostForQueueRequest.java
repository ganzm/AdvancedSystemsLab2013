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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((queue == null) ? 0 : queue.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		HostForQueueRequest other = (HostForQueueRequest) obj;
		if (queue == null) {
			if (other.queue != null)
				return false;
		} else if (!queue.equals(other.queue))
			return false;
		return true;
	}
}
