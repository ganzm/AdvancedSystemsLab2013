package ch.ethz.mlmq.net.request;

import ch.ethz.mlmq.dto.MessageQueryInfoDto;

public class DequeueMessageRequest implements Request {

	private static final long serialVersionUID = -4034153593869631817L;

	private MessageQueryInfoDto messageQueryInfo;

	public DequeueMessageRequest(MessageQueryInfoDto messageQueryInfo) {
		this.messageQueryInfo = messageQueryInfo;
	}

	public MessageQueryInfoDto getMessageQueryInfo() {
		return messageQueryInfo;
	}

	public long getQueueId() {
		return messageQueryInfo.getQueue().getId();
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
		DequeueMessageRequest other = (DequeueMessageRequest) obj;
		if (messageQueryInfo == null) {
			if (other.messageQueryInfo != null)
				return false;
		} else if (!messageQueryInfo.equals(other.messageQueryInfo))
			return false;
		return true;
	}

}
