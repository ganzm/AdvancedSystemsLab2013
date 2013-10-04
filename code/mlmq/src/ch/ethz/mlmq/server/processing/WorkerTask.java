package ch.ethz.mlmq.server.processing;

import java.io.Closeable;

import ch.ethz.mlmq.nio.CloseableByteBuffer;
import ch.ethz.mlmq.nio.ConnectedClient;

public class WorkerTask implements Closeable {

	/**
	 * indicates the clientId which performed this Request
	 * 
	 * @see ConnectedClient#getId()
	 */
	private final int clientId;

	private CloseableByteBuffer requestBuffer;

	private CloseableByteBuffer responseBuffer = null;

	public WorkerTask(int id, CloseableByteBuffer requestMessageBuffer) {
		this.clientId = id;
		this.requestBuffer = requestMessageBuffer;
	}

	/**
	 * indicates the clientId which performed this Request
	 * 
	 * @return
	 */
	public int getClientId() {
		return clientId;
	}

	public CloseableByteBuffer getRequestBuffer() {
		return requestBuffer;
	}

	public CloseableByteBuffer getResponseBuffer() {
		return responseBuffer;
	}

	public CloseableByteBuffer getAndRemoveRequestBuffer() {
		CloseableByteBuffer tmp = requestBuffer;
		requestBuffer = null;
		return tmp;
	}

	public CloseableByteBuffer getAndRemoveResponseBuffer() {
		CloseableByteBuffer tmp = responseBuffer;
		responseBuffer = null;
		return tmp;
	}

	public void setResponseBuffer(CloseableByteBuffer responseBuffer) {
		this.responseBuffer = responseBuffer;
	}

	@Override
	public void close() {
		if (requestBuffer != null) {
			requestBuffer.close();
		}

		if (responseBuffer != null) {
			responseBuffer.close();
		}
	}
}
