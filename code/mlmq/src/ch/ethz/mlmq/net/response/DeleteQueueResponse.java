package ch.ethz.mlmq.net.response;

import java.nio.ByteBuffer;

import ch.ethz.mlmq.net.MlmqSerializable;

public class DeleteQueueResponse implements Response {

	public static final long serialVersionUID = -9129773578776412298L;

	@Override
	public void serialize(ByteBuffer buffer) {
	}

	@Override
	public MlmqSerializable deserialize(ByteBuffer buffer) {
		return this;
	}

	@Override
	public int getTypeId() {
		return (int) serialVersionUID;
	}

}
