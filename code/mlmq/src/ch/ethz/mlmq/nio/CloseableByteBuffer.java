package ch.ethz.mlmq.nio;

import java.io.Closeable;
import java.io.IOException;
import java.nio.ByteBuffer;

public class CloseableByteBuffer implements Closeable {

	private final ByteBuffer buffer;
	private final ByteBufferPool pool;

	public CloseableByteBuffer(ByteBuffer buffer, ByteBufferPool pool) {
		this.buffer = buffer;
		this.pool = pool;
	}

	@Override
	public void close() throws IOException {

		pool.release(buffer);
	}

}
