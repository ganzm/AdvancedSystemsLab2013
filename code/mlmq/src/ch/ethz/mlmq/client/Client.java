package ch.ethz.mlmq.client;

import java.util.List;

import ch.ethz.mlmq.dto.BrokerDto;
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
	 * @param queue
	 */
	void deleteQueue(QueueDto queue);

	/**
	 * Sends a message to a specific queue.
	 * 
	 * @param queue
	 * @param message
	 */
	void sendMessage(QueueDto queue, MessageDto message);

	/**
	 * Sends a message to multiple queues.
	 * 
	 * @param queues
	 * @param message
	 */
	void sendMessage(List<QueueDto> queues, MessageDto message);

	/**
	 * Gets the host which is responsible for a specific queue.
	 * 
	 * @param queue
	 * @return
	 */
	BrokerDto getHostForQueue(QueueDto queue);

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
