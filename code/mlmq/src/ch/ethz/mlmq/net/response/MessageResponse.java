package ch.ethz.mlmq.net.response;

import ch.ethz.mlmq.dto.MessageDto;

public class MessageResponse implements Response {

	private static final long serialVersionUID = 1267869959385529197L;

	private MessageDto messageDto;

	public MessageResponse(MessageDto messageDto) {
		this.messageDto = messageDto;
	}

	public MessageDto getMessageDto() {
		return messageDto;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((messageDto == null) ? 0 : messageDto.hashCode());
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
		MessageResponse other = (MessageResponse) obj;
		if (messageDto == null) {
			if (other.messageDto != null)
				return false;
		} else if (!messageDto.equals(other.messageDto))
			return false;
		return true;
	}

}
