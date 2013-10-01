package ch.ethz.mlmq.server.processing;

import java.nio.ByteBuffer;

public class WorkerTask {

	private final int id;
	private final ByteBuffer messageBuffer;

	public WorkerTask(int id, ByteBuffer messageBuffer) {
		this.id = id;
		this.messageBuffer = messageBuffer;
	}

	public int getId() {
		return id;
	}

	public ByteBuffer getMessageBuffer() {
		return messageBuffer;
	}

}
