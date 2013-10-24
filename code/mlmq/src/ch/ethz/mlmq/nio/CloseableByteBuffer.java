package ch.ethz.mlmq.nio;

import java.io.Closeable;
import java.nio.ByteBuffer;

/**
 * Closely relate ByteBuffer which is put back into the pool when closed
 */
public class CloseableByteBuffer implements Closeable {

	private final ByteBuffer buffer;
	private final ByteBufferPool pool;

	public CloseableByteBuffer(ByteBuffer buffer, ByteBufferPool pool) {
		this.buffer = buffer;
		this.pool = pool;
	}

	@Override
	public void close() {
		pool.release(this);
	}

	public ByteBuffer getByteBuffer() {
		return buffer;
	}
}
