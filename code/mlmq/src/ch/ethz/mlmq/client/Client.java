package ch.ethz.mlmq.client;

import java.io.IOException;
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
	 * @throws IOException
	 */
	ClientDto register() throws IOException;

	/**
	 * Creates a queue.
	 * 
	 * @return
	 */
	QueueDto createQueue() throws IOException;

	/**
	 * Deletes a queue.
	 * 
	 * @param id
	 */
	void deleteQueue(long id) throws IOException;

	/**
	 * Sends a message to a specific queue.
	 * 
	 * @param queueId
	 * @param content
	 * @param prio
	 */
	void sendMessage(long queueId, byte[] content, int prio) throws IOException;

	/**
	 * Sends a message to multiple queues.
	 * 
	 * @param queues
	 * @param message
	 * @throws IOException
	 */
	void sendMessage(long[] queueIds, byte[] content, int prio) throws IOException;

	/**
	 * Query for queues with pending messages.
	 * 
	 * @param queues
	 * @param message
	 * @throws IOException
	 */
	List<QueueDto> queuesWithPendingMessages() throws IOException;

	/**
	 * Reads the first message without removing it.
	 * 
	 * @param messageQueryInfo
	 * @return
	 * @throws IOException
	 */
	MessageDto peekMessage(MessageQueryInfoDto messageQueryInfo) throws IOException;

	/**
	 * Reads the first message and removes it.
	 * 
	 * @param messageQueryInfo
	 * @return
	 * @throws IOException
	 */
	MessageDto dequeueMessage(MessageQueryInfoDto messageQueryInfo) throws IOException;
}
