package ch.ethz.mlmq.dto;

import java.io.Serializable;

/**
 * Data Transfer Object
 * 
 * used for data transport between Database and broker
 * 
 */
public class MessageDto implements Serializable {

	private static final long serialVersionUID = -5253100231478702803L;

	private long id;
	private MessageContentDto messageContent;
	private QueueDto queue;

	public MessageDto() {

	}

	public long getId() {
		return id;
	}

	public MessageContentDto getMessageContent() {
		return messageContent;
	}

	public QueueDto getQueue() {
		return queue;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((messageContent == null) ? 0 : messageContent.hashCode());
		result = prime * result + ((queue == null) ? 0 : queue.hashCode());
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
		if (id != other.id)
			return false;
		if (messageContent == null) {
			if (other.messageContent != null)
				return false;
		} else if (!messageContent.equals(other.messageContent))
			return false;
		if (queue == null) {
			if (other.queue != null)
				return false;
		} else if (!queue.equals(other.queue))
			return false;
		return true;
	}

}
