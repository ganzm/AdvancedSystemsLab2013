package ch.ethz.mlmq.nio;

import java.nio.ByteBuffer;

/**
 * Represents a connected client
 * 
 * 
 */
public class ConnectedClient {

	private final String name;

	private ByteBuffer rxBuffer = ByteBuffer.allocate(5000);
	private ByteBuffer txBuffer = ByteBuffer.allocate(5000);

	public ConnectedClient(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return name;
	}

	public ByteBuffer getRxBuffer() {
		return rxBuffer;
	}

	/**
	 * Checks the client's protocl state. Returns true if there is data to send
	 * 
	 * @return
	 */
	public boolean isProtocolStateWriting() {
		return true;
	}
}
