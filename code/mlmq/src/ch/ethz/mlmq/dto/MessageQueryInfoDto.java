package ch.ethz.mlmq.dto;

/**
 * Data Transfer Object
 * 
 * used to filter incoming messages.
 */
public class MessageQueryInfoDto {
	private QueueDto queue;
	private ClientDto sender;
	private boolean shouldOrderByPriority;

	public MessageQueryInfoDto(QueueDto queueFilter, ClientDto sender,
			boolean shouldOrderByPriority) {
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

}
