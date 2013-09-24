package ch.ethz.mlmq.http.request;

import ch.ethz.mlmq.dto.MessageQueryInfoDto;

public class PeekMessageRequest implements Request {

	private MessageQueryInfoDto messageQueryInfo;

	public PeekMessageRequest(MessageQueryInfoDto messageQueryInfo) {
		this.messageQueryInfo = messageQueryInfo;
	}

}
