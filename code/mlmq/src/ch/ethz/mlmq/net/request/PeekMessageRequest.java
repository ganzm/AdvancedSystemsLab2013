package ch.ethz.mlmq.net.request;

import java.nio.ByteBuffer;

import ch.ethz.mlmq.dto.MessageQueryInfoDto;
import ch.ethz.mlmq.net.HomeMadeSerializable;
import ch.ethz.mlmq.util.ByteBufferUtil;

public class PeekMessageRequest implements Request {

	public static final long serialVersionUID = 5535263002144596601L;

	private MessageQueryInfoDto messageQueryInfo;

	public PeekMessageRequest() {

	}

	public PeekMessageRequest(MessageQueryInfoDto messageQueryInfo) {
		this.messageQueryInfo = messageQueryInfo;
	}

	public long getQueueId() {
		return messageQueryInfo.getQueue().getId();
	}

	public MessageQueryInfoDto getMessageQueryInfo() {
		return messageQueryInfo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((messageQueryInfo == null) ? 0 : messageQueryInfo.hashCode());
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
		PeekMessageRequest other = (PeekMessageRequest) obj;
		if (messageQueryInfo == null) {
			if (other.messageQueryInfo != null)
				return false;
		} else if (!messageQueryInfo.equals(other.messageQueryInfo))
			return false;
		return true;
	}

	@Override
	public void serialize(ByteBuffer buffer) {
		ByteBufferUtil.serialize(messageQueryInfo, buffer);
	}

	@Override
	public HomeMadeSerializable deserialize(ByteBuffer buffer) {
		messageQueryInfo = (MessageQueryInfoDto) ByteBufferUtil.deserialize(new MessageQueryInfoDto(), buffer);
		return this;
	}

	@Override
	public int getTypeId() {
		return (int) serialVersionUID;
	}
}
