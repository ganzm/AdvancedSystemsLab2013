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

	public void setQueues(List<QueueDto> queues) {
		this.queues = queues;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((queues == null) ? 0 : queues.hashCode());
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
		QueuesWithPendingMessagesResponse other = (QueuesWithPendingMessagesResponse) obj;
		if (queues == null) {
			if (other.queues != null)
				return false;
		} else if (!queues.equals(other.queues))
			return false;
		return true;
	}

}
