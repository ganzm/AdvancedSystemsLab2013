package ch.ethz.mlmq.dto;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.Arrays;

import ch.ethz.mlmq.net.MlmqSerializable;
import ch.ethz.mlmq.util.ByteBufferUtil;

/**
 * Data Transfer Object
 * 
 * used for data transport between Database and broker
 * 
 */
public class MessageDto implements Serializable, MlmqSerializable {

	private static final long serialVersionUID = -5253100231478702803L;

	private long id;
	private QueueDto queue;
	private byte[] content;
	private int prio;
	private ClientDto sender;
	private Integer conversationContext;

	public MessageDto() {

	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public QueueDto getQueue() {
		return queue;
	}

	public void setQueue(QueueDto queue) {
		this.queue = queue;
	}

	public byte[] getContent() {
		return content;
	}

	public void setContent(byte[] content) {
		this.content = content;
	}

	public int getPrio() {
		return prio;
	}

	public void setPrio(int prio) {
		this.prio = prio;
	}

	public ClientDto getSender() {
		return sender;
	}

	public void setSender(ClientDto sender) {
		this.sender = sender;
	}

	public Integer getConversationContext() {
		return conversationContext;
	}

	public void setConversationContext(Integer conversationContext) {
		this.conversationContext = conversationContext;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(content);
		result = prime * result + ((conversationContext == null) ? 0 : conversationContext.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + prio;
		result = prime * result + ((queue == null) ? 0 : queue.hashCode());
		result = prime * result + ((sender == null) ? 0 : sender.hashCode());
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
		MessageDto other = (MessageDto) obj;
		if (!Arrays.equals(content, other.content))
			return false;
		if (conversationContext == null) {
			if (other.conversationContext != null)
				return false;
		} else if (!conversationContext.equals(other.conversationContext))
			return false;
		if (id != other.id)
			return false;
		if (prio != other.prio)
			return false;
		if (queue == null) {
			if (other.queue != null)
				return false;
		} else if (!queue.equals(other.queue))
			return false;
		if (sender == null) {
			if (other.sender != null)
				return false;
		} else if (!sender.equals(other.sender))
			return false;
		return true;
	}

	@Override
	public void serialize(ByteBuffer buffer) {
		buffer.putLong(id);
		ByteBufferUtil.serialize(queue, buffer);
		ByteBufferUtil.putByteArray(content, buffer);
		buffer.putInt(prio);
		ByteBufferUtil.serialize(sender, buffer);
		ByteBufferUtil.putInteger(conversationContext, buffer);

	}

	@Override
	public MlmqSerializable deserialize(ByteBuffer buffer) {
		id = buffer.getLong();
		queue = (QueueDto) ByteBufferUtil.deserialize(new QueueDto(), buffer);
		content = ByteBufferUtil.getByteArray(buffer);
		prio = buffer.getInt();
		sender = (ClientDto) ByteBufferUtil.deserialize(new ClientDto(), buffer);
		conversationContext = ByteBufferUtil.getInteger(buffer);

		return this;
	}
}
