package ch.ethz.mlmq.net.response;

import java.nio.ByteBuffer;

import ch.ethz.mlmq.dto.MessageDto;
import ch.ethz.mlmq.net.MlmqSerializable;
import ch.ethz.mlmq.util.ByteBufferUtil;

public class MessageResponse implements Response {

	public static final long serialVersionUID = 1267869959385529197L;

	private MessageDto messageDto;

	public MessageResponse() {

	}

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

	@Override
	public void serialize(ByteBuffer buffer) {
		ByteBufferUtil.serialize(messageDto, buffer);
	}

	@Override
	public MlmqSerializable deserialize(ByteBuffer buffer) {
		messageDto = (MessageDto) ByteBufferUtil.deserialize(new MessageDto(), buffer);
		return this;
	}

	@Override
	public int getTypeId() {
		return (int) serialVersionUID;
	}
}
