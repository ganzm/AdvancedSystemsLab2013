package ch.ethz.mlmq.server;

public class ClientApplicationContext {

	/**
	 * client id assigned from the networking layer
	 */
	private final int clientNetworkHandle;

	/**
	 * Client ID assigned from the database
	 */
	private int clientId;

	private String clientName;

	public ClientApplicationContext(int clientNetworkHandle) {
		this.clientNetworkHandle = clientNetworkHandle;
	}

	public int getClientNetworkHandle() {
		return clientNetworkHandle;
	}

	public int getClientId() {
		return clientId;
	}

	public void setClientId(int clientId) {
		this.clientId = clientId;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public String getClientName() {
		return clientName;
	}
}
