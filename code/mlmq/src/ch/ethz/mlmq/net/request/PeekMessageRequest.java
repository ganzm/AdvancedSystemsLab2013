package ch.ethz.mlmq.net.request;

import ch.ethz.mlmq.dto.MessageQueryInfoDto;

public class PeekMessageRequest implements QueueRequest {

	private MessageQueryInfoDto messageQueryInfo;

	public PeekMessageRequest(MessageQueryInfoDto messageQueryInfo) {
		this.messageQueryInfo = messageQueryInfo;
	}

	@Override
	public long getQueueId() {
		return messageQueryInfo.getQueue().getId();
	}

}
