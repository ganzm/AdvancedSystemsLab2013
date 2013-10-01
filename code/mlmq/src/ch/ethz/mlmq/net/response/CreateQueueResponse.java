package ch.ethz.mlmq.net.response;

import ch.ethz.mlmq.dto.QueueDto;

public class CreateQueueResponse implements Response {

	private static final long serialVersionUID = 1533054714047954400L;

	private final QueueDto queue;

	public CreateQueueResponse(QueueDto queue) {
		this.queue = queue;
	}

	public QueueDto getQueueDto() {
		return queue;
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
		CreateQueueResponse other = (CreateQueueResponse) obj;
		if (queue == null) {
			if (other.queue != null)
				return false;
		} else if (!queue.equals(other.queue))
			return false;
		return true;
	}

}
