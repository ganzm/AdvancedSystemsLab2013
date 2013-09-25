package ch.ethz.mlmq.net.response;

import ch.ethz.mlmq.dto.MessageDto;

public class MessageResponse implements Response{

	private MessageDto messageDto;

	public MessageDto getMessageDto() {
		return messageDto;
	}

}
