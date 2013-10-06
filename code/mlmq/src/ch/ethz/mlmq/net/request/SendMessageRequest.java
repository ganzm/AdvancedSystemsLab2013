package ch.ethz.mlmq.net.request;

import java.util.Arrays;

public class SendMessageRequest implements QueueRequest {

	private static final long serialVersionUID = 5365826671308061202L;

	private long queueId;

	private byte[] content;

	private int prio;

	public SendMessageRequest(long queueId, byte[] content, int prio) {
		this.queueId = queueId;
		this.content = content;
		this.prio = prio;
	}

	@Override
	public long getQueueId() {
		return queueId;
	}

	public byte[] getContent() {
		return content;
	}

	public int getPrio() {
		return prio;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(content);
		result = prime * result + prio;
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
		if (!Arrays.equals(content, other.content))
			return false;
		if (prio != other.prio)
			return false;
		if (queueId != other.queueId)
			return false;
		return true;
	}

}
