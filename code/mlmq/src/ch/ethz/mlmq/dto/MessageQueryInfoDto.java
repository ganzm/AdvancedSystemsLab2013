package ch.ethz.mlmq.dto;

import java.io.Serializable;
import java.nio.ByteBuffer;

import ch.ethz.mlmq.net.MlmqSerializable;
import ch.ethz.mlmq.util.ByteBufferUtil;

/**
 * Data Transfer Object
 * 
 * used to filter messages.
 */
public class MessageQueryInfoDto implements Serializable, MlmqSerializable {
	private static final long serialVersionUID = 1658560065690073547L;

	/**
	 * Optional Parameter - only get messages which belong to this queue
	 */
	private QueueDto queue;

	/**
	 * Optional Parameter - only get message which were sent by a specific sender
	 */
	private ClientDto sender;

	private boolean shouldOrderByPriority;

	private Integer conversationContext = null;

	/**
	 * Use this constructor if you want to get the oldest message from a specific queue
	 * 
	 * @param queue
	 */
	public MessageQueryInfoDto(QueueDto queue) {
		this.queue = queue;
	}

	/**
	 * Use this constructor if you want to check if you got a response for a specific conversation
	 * 
	 * @param myQueue
	 *            you need to specify your own client queue here
	 * @param conversationContext
	 */
	public MessageQueryInfoDto(QueueDto myQueue, int conversationContext) {
		this.queue = myQueue;
		this.conversationContext = conversationContext;
	}

	public MessageQueryInfoDto(QueueDto queueFilter, ClientDto sender, boolean shouldOrderByPriority) {
		this.queue = queueFilter;
		this.sender = sender;
		this.shouldOrderByPriority = shouldOrderByPriority;
	}

	public MessageQueryInfoDto(QueueDto queueFilter, ClientDto sender, boolean shouldOrderByPriority, int conversationContext) {
		this.conversationContext = conversationContext;
		this.queue = queueFilter;
		this.sender = sender;
		this.shouldOrderByPriority = shouldOrderByPriority;
	}

	public MessageQueryInfoDto(long queueId, int conversationContext) {
		this.conversationContext = conversationContext;
		this.queue = new QueueDto(queueId);
	}

	public MessageQueryInfoDto() {

	}

	/**
	 * @return the queue
	 */
	public QueueDto getQueue() {
		return queue;
	}

	public ClientDto getSender() {
		return sender;
	}

	public boolean shouldOrderByPriority() {
		return shouldOrderByPriority;
	}

	public Integer getConversationContext() {
		return conversationContext;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((conversationContext == null) ? 0 : conversationContext.hashCode());
		result = prime * result + ((queue == null) ? 0 : queue.hashCode());
		result = prime * result + ((sender == null) ? 0 : sender.hashCode());
		result = prime * result + (shouldOrderByPriority ? 1231 : 1237);
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
		MessageQueryInfoDto other = (MessageQueryInfoDto) obj;
		if (conversationContext == null) {
			if (other.conversationContext != null)
				return false;
		} else if (!conversationContext.equals(other.conversationContext))
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
		if (shouldOrderByPriority != other.shouldOrderByPriority)
			return false;
		return true;
	}

	@Override
	public void serialize(ByteBuffer buffer) {
		ByteBufferUtil.serialize(queue, buffer);
		ByteBufferUtil.serialize(sender, buffer);
		ByteBufferUtil.putBoolean(shouldOrderByPriority, buffer);
		ByteBufferUtil.putInteger(conversationContext, buffer);
	}

	@Override
	public MlmqSerializable deserialize(ByteBuffer buffer) {
		queue = (QueueDto) ByteBufferUtil.deserialize(new QueueDto(), buffer);
		sender = (ClientDto) ByteBufferUtil.deserialize(new ClientDto(), buffer);
		shouldOrderByPriority = ByteBufferUtil.getBoolean(buffer);
		conversationContext = ByteBufferUtil.getInteger(buffer);
		return this;
	}
}
