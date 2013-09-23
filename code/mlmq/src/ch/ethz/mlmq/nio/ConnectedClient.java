package ch.ethz.mlmq.nio;

import java.nio.ByteBuffer;

/**
 * Represents a connected client
 * 
 * @author mat
 * 
 */
public class ConnectedClient {
	private final String name;

	private ByteBuffer rxBuffer = ByteBuffer.allocate(2000000);

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
}
