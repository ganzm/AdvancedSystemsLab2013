package ch.ethz.mlmq.server.processing;

import ch.ethz.mlmq.nio.CloseableByteBuffer;
import ch.ethz.mlmq.nio.ConnectedClient;

public class WorkerTask {

	/**
	 * indicates the clientId this Request
	 * 
	 * @see ConnectedClient#getId()
	 */
	private final int cleintId;

	private final CloseableByteBuffer requestBuffer;

	private CloseableByteBuffer responseBuffer = null;

	public WorkerTask(int id, CloseableByteBuffer requestMessageBuffer) {
		this.cleintId = id;
		this.requestBuffer = requestMessageBuffer;
	}

	public int getId() {
		return cleintId;
	}

	public CloseableByteBuffer getMessageBuffer() {
		return requestBuffer;
	}

	public CloseableByteBuffer getResponseBuffer() {
		return responseBuffer;
	}

	public void setResponseBuffer(CloseableByteBuffer responseBuffer) {
		this.responseBuffer = responseBuffer;
	}

}
