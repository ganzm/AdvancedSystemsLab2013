package ch.ethz.mlmq.net.request;

public class SendMessageRequest implements QueueRequest {

	private static final long serialVersionUID = 5365826671308061202L;

	private long queueId;

	public SendMessageRequest(long queueId, byte[] content, int prio) {
		this.queueId = queueId;
	}

	@Override
	public long getQueueId() {
		return queueId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (queueId ^ (queueId >>> 32));
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
		SendMessageRequest other = (SendMessageRequest) obj;
		if (queueId != other.queueId)
			return false;
		return true;
	}

}
