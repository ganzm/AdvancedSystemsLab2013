package ch.ethz.mlmq.net;

import java.nio.ByteBuffer;

public interface MlmqSerializable {
	void serialize(ByteBuffer buffer);

	MlmqSerializable deserialize(ByteBuffer buffer);
}
