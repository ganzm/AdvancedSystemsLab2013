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

}
