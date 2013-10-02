package ch.ethz.mlmq.sample.nio;

import java.nio.ByteBuffer;

import ch.ethz.mlmq.net.Protocol;

/**
 * Represents a connected client
 * 
 */
public class ConnectedClient {

	private final String name;
	private final int clientId;

	private ByteBuffer rxBuffer;

	public ConnectedClient(int clientId, String name) {
		this.clientId = clientId;
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
	 * Checks the client's protocol state. Returns true if there is data to send
	 * 
	 * @return
	 */
	public boolean isProtocolStateWriting() {
		return true;
	}

	/**
	 * closes the connection to the client
	 * 
	 */
	public void close() {

	}

	public boolean hasReceivedMessage() {

		ByteBuffer buffer = rxBuffer;

		if (buffer.position() >= Protocol.LENGH_FIELD_LENGHT) {
			// enough data to read first lenght-int
			int endPosition = buffer.position();
			buffer.position(0);
			int messageLenght = buffer.getInt();

			if (messageLenght <= endPosition - Protocol.LENGH_FIELD_LENGHT) {
				// received complete message
				return true;
			}
			// else { not quite enough data received
		}

		return false;
	}

	public int getId() {
		return clientId;
	}

}
