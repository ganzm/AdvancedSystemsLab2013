package ch.ethz.mlmq.http.response;

import ch.ethz.mlmq.dto.MessageDto;

public class MessageResponse implements Response{

	private MessageDto messageDto;

	public MessageDto getMessageDto() {
		return messageDto;
	}

}
