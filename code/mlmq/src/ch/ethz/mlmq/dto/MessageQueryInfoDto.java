package ch.ethz.mlmq.dto;

import java.io.Serializable;

/**
 * Data Transfer Object
 * 
 * used to filter messages.
 */
public class MessageQueryInfoDto implements Serializable {
	private static final long serialVersionUID = 1658560065690073547L;

	/**
	 * only get messages which belong to this queue
	 */
	private QueueDto queue;

	/**
	 * only get message which were sent by a specific sender
	 */
	private ClientDto sender;

	private boolean shouldOrderByPriority;

	private Integer conversationContext = null;

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

}
