package ch.ethz.mlmq.nio;

import java.nio.ByteBuffer;

import ch.ethz.mlmq.server.BrokerConfiguration;

public class ByteBufferPool {

	private final int defaultBufferCapacity;

	public ByteBufferPool(BrokerConfiguration config) {

		// TODO config
		defaultBufferCapacity = 4000;
	}

	public void release(ByteBuffer buffer) {
		// TODO Auto-generated method stub

	}

	public ByteBuffer aquire() {
		return ByteBuffer.allocate(defaultBufferCapacity);
	}
}
