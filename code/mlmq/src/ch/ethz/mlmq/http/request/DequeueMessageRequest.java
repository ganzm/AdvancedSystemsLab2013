package ch.ethz.mlmq.http.request;

import ch.ethz.mlmq.dto.MessageQueryInfoDto;

public class DequeueMessageRequest implements Request {

	private MessageQueryInfoDto messageQueryInfo;

	public DequeueMessageRequest(MessageQueryInfoDto messageQueryInfo) {
		this.messageQueryInfo = messageQueryInfo;
	}
}
