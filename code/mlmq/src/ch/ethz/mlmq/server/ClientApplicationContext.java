package ch.ethz.mlmq.server;

import ch.ethz.mlmq.dto.ClientDto;
import ch.ethz.mlmq.dto.QueueDto;
import ch.ethz.mlmq.nio.ConnectedClient;

/**
 * Broker stores context information for each connected client
 * 
 * This is closely related to ConnectedClient but does not contain networking specific state but rather application state per client
 * 
 * @see ConnectedClient
 */
public class ClientApplicationContext {

	/**
	 * client id assigned from the networking layer
	 */
	private final int clientNetworkHandle;

	private ClientDto clientDto;

	/**
	 * Queue where this client receives messages
	 */
	private QueueDto clientQueue;

	public ClientApplicationContext(int clientNetworkHandle) {
		this.clientNetworkHandle = clientNetworkHandle;
	}

	public boolean isRegistered() {
		return clientQueue != null;
	}

	public int getClientNetworkHandle() {
		return clientNetworkHandle;
	}

	public ClientDto getClient() {
		return clientDto;
	}

	public void setClientQueue(QueueDto clientQueue) {
		this.clientQueue = clientQueue;
	}

	public void setClient(ClientDto clientDto) {
		this.clientDto = clientDto;
	}

	public QueueDto getClientQueue() {
		return this.clientQueue;
	}

}
