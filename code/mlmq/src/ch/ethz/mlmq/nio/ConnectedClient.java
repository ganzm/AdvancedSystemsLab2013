package ch.ethz.mlmq.nio;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.util.logging.Logger;

import ch.ethz.mlmq.net.Protocol;

/**
 * Represents a connected client
 * 
 */
public class ConnectedClient {

	private static final Logger logger = Logger.getLogger("ConnectedClient");

	private final String name;
	private final int clientId;

	private ByteBuffer rxBuffer = ByteBuffer.allocate(5000);
	private ByteBuffer txBuffer = ByteBuffer.allocate(5000);

	private SelectionKey selectionKey;

	public ConnectedClient(int clientId, String name) {
		this.clientId = clientId;
		this.name = name;
	}

	public void setSelectionKey(SelectionKey selectionKey) {
		this.selectionKey = selectionKey;
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
		if (rxBuffer.position() >= Protocol.LENGH_FIELD_LENGHT) {
			// enough data to read first lenght-int
			int endPosition = rxBuffer.position();
			rxBuffer.position(0);
			int messageLenght = rxBuffer.getInt();

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

	public ByteBuffer swapRxBuffer(ByteBuffer replacementBuffer) {
		ByteBuffer tmp = rxBuffer;
		rxBuffer = replacementBuffer;
		return tmp;
	}
}
