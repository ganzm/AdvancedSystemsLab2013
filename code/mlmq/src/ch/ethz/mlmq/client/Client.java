package ch.ethz.mlmq.client;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;

import ch.ethz.mlmq.dto.ClientDto;
import ch.ethz.mlmq.dto.MessageDto;
import ch.ethz.mlmq.dto.MessageQueryInfoDto;
import ch.ethz.mlmq.dto.QueueDto;

/**
 * The client interface
 */
public interface Client extends Closeable {

	/**
	 * Initializes the client and connects it to the Broker
	 * 
	 * @throws IOException
	 */
	void init() throws IOException;

	/**
	 * indicates whether the client is connected or not
	 * 
	 * @return
	 */
	boolean isConnected();

	/**
	 * Register as a new client.
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
	QueueDto createQueue(String queueName) throws IOException;

	/**
	 * Tries to find the where we can send personal messages to a client
	 * 
	 * @param clientId
	 * @return Queue may be null if not found
	 * @throws IOException
	 */
	QueueDto lookupClientQueue(long clientId) throws IOException;

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
	 * Sends a private message to a client
	 * 
	 * @param clientId
	 * @param content
	 * @param prio
	 * @throws IOException
	 */
	void sendMessageToClient(long clientId, byte[] content, int prio) throws IOException;

	/**
	 * 
	 * Request/Responses are posted to the private client queue it is sent to
	 * 
	 * As soon as a client performs a Request it receives a context identifier
	 * 
	 * Any response received from a client needs to be in the queue of the receiving client and needs to have the same context identifier
	 * 
	 * @param client
	 * @param content
	 * @param prio
	 * @throws IOException
	 * @return returns a context identifier
	 */
	long sendRequestToClient(long client, byte[] content, int prio) throws IOException;

	/**
	 * 
	 * @param clientId
	 * @param context
	 * @param content
	 * @param prio
	 * @throws IOException
	 */
	long sendResponseToClient(long clientId, long context, byte[] content, int prio) throws IOException;

	/**
	 * Query for queues with pending messages.
	 * 
	 * This method returns number of message in the client's queue and a list of queues which are not empty.
	 * 
	 * @param queues
	 *            output parameter containing Queues which contain messages. This method simply appends the results to this list
	 * @param maxNumQueues
	 *            maximum number of returned QueueDto's
	 * @return number of messages in the client's personal queue
	 * @throws IOException
	 */
	int queuesWithPendingMessages(List<QueueDto> queues, int maxNumQueues) throws IOException;

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
