package ch.ethz.mlmq.nio;

import java.nio.ByteBuffer;
import java.util.concurrent.ConcurrentLinkedQueue;

import ch.ethz.mlmq.server.BrokerConfiguration;

public class ByteBufferPool {

	private final int defaultBufferCapacity;

	private final ConcurrentLinkedQueue<CloseableByteBuffer> pooledBuffers;

	public ByteBufferPool(BrokerConfiguration config) {
		this.defaultBufferCapacity = config.getMaxMessageSize();
		this.pooledBuffers = new ConcurrentLinkedQueue<>();
	}

	public void release(CloseableByteBuffer buffer) {
		pooledBuffers.add(buffer);
	}

	public CloseableByteBuffer aquire() {
		CloseableByteBuffer buffer = pooledBuffers.poll();
		if (buffer == null) {
			buffer = new CloseableByteBuffer(ByteBuffer.allocate(defaultBufferCapacity), this);
		}

		buffer.getByteBuffer().clear();
		return buffer;
	}
}
