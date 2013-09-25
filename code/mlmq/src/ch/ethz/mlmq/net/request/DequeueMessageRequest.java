package ch.ethz.mlmq.net.request;

import ch.ethz.mlmq.dto.MessageQueryInfoDto;

public class DequeueMessageRequest implements QueueRequest {

	private static final long serialVersionUID = -4034153593869631817L;
	
	private MessageQueryInfoDto messageQueryInfo;

	public DequeueMessageRequest(MessageQueryInfoDto messageQueryInfo) {
		this.messageQueryInfo = messageQueryInfo;
	}

	@Override
	public long getQueueId() {
		return messageQueryInfo.getQueue().getId();
	}
}
