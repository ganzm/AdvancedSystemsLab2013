package ch.ethz.mlmq.nio;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;

import ch.ethz.mlmq.net.Protocol;
import ch.ethz.mlmq.server.ClientApplicationContext;

/**
 * Represents a connected client
 * 
 */
public class ConnectedClient {

	/**
	 * For debugging purpouses only
	 */
	private final String name;

	private CloseableByteBuffer rxBuffer;
	private CloseableByteBuffer txBuffer;

	private SelectionKey selectionKey;

	private final ClientApplicationContext clientApplicationContext;

	private long firstRequestByteSeen;
	private long startSendResponseTime;

	public ConnectedClient(int clientNetworkHandle, String name) {
		this.clientApplicationContext = new ClientApplicationContext(clientNetworkHandle);
		this.name = name;
	}

	public void initBuffers(CloseableByteBuffer rxBuffer, CloseableByteBuffer txBuffer) {
		this.rxBuffer = rxBuffer;
		this.txBuffer = txBuffer;
	}

	public void setSelectionKey(SelectionKey selectionKey) {
		this.selectionKey = selectionKey;
	}

	@Override
	public String toString() {
		return name;
	}

	public ByteBuffer getRxBuffer() {
		return rxBuffer.getByteBuffer();
	}

	public ByteBuffer getTxBuffer() {
		return txBuffer.getByteBuffer();
	}

	public ClientApplicationContext getClientContext() {
		return clientApplicationContext;
	}

	/**
	 * closes the connection to the client
	 * 
	 */
	public void close() {
		if (rxBuffer != null) {
			rxBuffer.close();
		}

		if (txBuffer != null) {
			txBuffer.close();
		}

		selectionKey.cancel();
	}

	public boolean hasReceivedMessage() {

		ByteBuffer buffer = rxBuffer.getByteBuffer();

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

	public CloseableByteBuffer swapRxBuffer(CloseableByteBuffer replacementBuffer) {
		CloseableByteBuffer tmp = rxBuffer;
		rxBuffer = replacementBuffer;
		return tmp;
	}

	/**
	 * sets the Respponse Buffer and switches to socket write mode
	 * 
	 * @param newTxBuffer
	 */
	public void setResponse(CloseableByteBuffer newTxBuffer) {
		if (txBuffer != null) {
			txBuffer.close();
		}

		txBuffer = newTxBuffer;

		// switch to buffer read mode
		newTxBuffer.getByteBuffer().flip();

		selectionKey.interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE);

		startSendResponseTime = System.nanoTime();
	}

	/**
	 * returns true if there is no more data to send
	 * 
	 * @return
	 */
	public boolean afterWrite() {
		if (!txBuffer.getByteBuffer().hasRemaining()) {

			selectionKey.interestOps(SelectionKey.OP_READ);

			// give by transmission buffer
			txBuffer.close();
			txBuffer = null;

			return true;
		}
		return false;
	}

	/**
	 * Used for performance logging
	 */
	public void setFirstRequestByteSeenTimeStamp() {
		firstRequestByteSeen = System.nanoTime();
	}

	/**
	 * Used for performance logging
	 */
	public long getFirstRequestByteSeenTimeStamp() {
		return firstRequestByteSeen;
	}

	/**
	 * Used for performance logging
	 */
	public long getStartSendResponseTime() {
		return startSendResponseTime;
	}
}
