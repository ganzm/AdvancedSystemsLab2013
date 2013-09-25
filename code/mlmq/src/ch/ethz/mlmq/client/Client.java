package ch.ethz.mlmq.client;

import java.util.List;

import ch.ethz.mlmq.dto.ClientDto;
import ch.ethz.mlmq.dto.MessageDto;
import ch.ethz.mlmq.dto.MessageQueryInfoDto;
import ch.ethz.mlmq.dto.QueueDto;

/**
 * The client interface
 */
public interface Client {

	/**
	 * Registers a new client.
	 * 
	 * @return
	 */
	ClientDto register();

	/**
	 * Creates a queue.
	 * 
	 * @return
	 */
	QueueDto createQueue();

	/**
	 * Deletes a queue.
	 * 
	 * @param id
	 */
	void deleteQueue(long id);

	/**
	 * Sends a message to a specific queue.
	 * 
	 * @param queueId
	 * @param content
	 * @param prio
	 */
	void sendMessage(long queueId, byte[] content, int prio);

	/**
	 * Sends a message to multiple queues.
	 * 
	 * @param queues
	 * @param message
	 */
	void sendMessage(long[] queueIds, byte[] content, int prio);

	/**
	 * Query for queues with pending messages.
	 * 
	 * @param queues
	 * @param message
	 */
	List<QueueDto> queuesWithPendingMessages();

	/**
	 * Reads the first message without removing it.
	 * 
	 * @param messageQueryInfo
	 * @return
	 */
	MessageDto peekMessage(MessageQueryInfoDto messageQueryInfo);

	/**
	 * Reads the first message and removes it.
	 * 
	 * @param messageQueryInfo
	 * @return
	 */
	MessageDto dequeueMessage(MessageQueryInfoDto messageQueryInfo);
}
