package ch.ethz.mlmq.net.request;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SendMessageRequest implements Request {

	private static final long serialVersionUID = 5365826671308061202L;

	/**
	 * Target queues
	 */
	private List<Long> queueIds;

	private byte[] content;

	private int prio;

	public SendMessageRequest(long queueId, byte[] content, int prio) {
		this.queueIds = new ArrayList<>();
		this.content = content;
		this.prio = prio;

		this.queueIds.add(queueId);
	}

	public SendMessageRequest(List<Long> queueIds, byte[] content, int prio) {
		this.queueIds = queueIds;
		this.content = content;
		this.prio = prio;
	}

	public byte[] getContent() {
		return content;
	}

	public int getPrio() {
		return prio;
	}

	public List<Long> getQueueIds() {
		return queueIds;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(content);
		result = prime * result + prio;
		result = prime * result + ((queueIds == null) ? 0 : queueIds.hashCode());
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
		if (queueIds == null) {
			if (other.queueIds != null)
				return false;
		} else if (!queueIds.equals(other.queueIds))
			return false;
		return true;
	}

}
