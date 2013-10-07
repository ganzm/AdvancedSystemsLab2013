package ch.ethz.mlmq.net.request;

import ch.ethz.mlmq.dto.MessageQueryInfoDto;

public class PeekMessageRequest implements QueueRequest {

	private static final long serialVersionUID = 5535263002144596601L;

	private MessageQueryInfoDto messageQueryInfo;

	public PeekMessageRequest(MessageQueryInfoDto messageQueryInfo) {
		this.messageQueryInfo = messageQueryInfo;
	}

	@Override
	public long getQueueId() {
		return messageQueryInfo.getQueue().getId();
	}

	public MessageQueryInfoDto getMessageQueryInfo() {
		return messageQueryInfo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((messageQueryInfo == null) ? 0 : messageQueryInfo.hashCode());
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
		PeekMessageRequest other = (PeekMessageRequest) obj;
		if (messageQueryInfo == null) {
			if (other.messageQueryInfo != null)
				return false;
		} else if (!messageQueryInfo.equals(other.messageQueryInfo))
			return false;
		return true;
	}

}
