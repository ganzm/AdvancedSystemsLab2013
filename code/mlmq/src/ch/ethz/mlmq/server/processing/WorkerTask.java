package ch.ethz.mlmq.server.processing;

import java.io.Closeable;

import ch.ethz.mlmq.nio.CloseableByteBuffer;
import ch.ethz.mlmq.server.ClientApplicationContext;

public class WorkerTask implements Closeable {

	private CloseableByteBuffer requestBuffer;

	private CloseableByteBuffer responseBuffer = null;

	private ClientApplicationContext clientContext;

	public WorkerTask(ClientApplicationContext clientContext, CloseableByteBuffer requestMessageBuffer) {
		this.clientContext = clientContext;
		this.requestBuffer = requestMessageBuffer;
	}

	public ClientApplicationContext getClientContext() {
		return clientContext;
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
