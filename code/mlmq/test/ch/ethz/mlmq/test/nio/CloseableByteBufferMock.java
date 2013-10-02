package ch.ethz.mlmq.test.nio;

import java.nio.ByteBuffer;

import ch.ethz.mlmq.nio.CloseableByteBuffer;

public class CloseableByteBufferMock extends CloseableByteBuffer {

	public CloseableByteBufferMock(int capacity) {
		super(ByteBuffer.allocate(capacity), null);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void close() {
	}
}
