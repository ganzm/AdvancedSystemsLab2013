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
		DeleteQueueRequest other = (DeleteQueueRequest) obj;
		if (queue == null) {
			if (other.queue != null)
				return false;
		} else if (!queue.equals(other.queue))
			return false;
		return true;
	}
}
