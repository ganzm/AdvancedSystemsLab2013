package ch.ethz.mlmq.net.response;

import java.nio.ByteBuffer;

import ch.ethz.mlmq.net.MlmqSerializable;

public class SendMessageResponse implements Response {

	public static final long serialVersionUID = 1441841603203307110L;

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
