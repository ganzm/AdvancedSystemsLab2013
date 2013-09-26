package ch.ethz.mlmq.net.response;

import ch.ethz.mlmq.dto.MessageDto;

public class MessageResponse implements Response{

	private static final long serialVersionUID = 1267869959385529197L;

	private MessageDto messageDto;

	public MessageDto getMessageDto() {
		return messageDto;
	}

}
