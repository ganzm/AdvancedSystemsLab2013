package ch.ethz.mlmq.net;

import java.nio.ByteBuffer;

public interface HomeMadeSerializable {
	void serialize(ByteBuffer buffer);

	HomeMadeSerializable deserialize(ByteBuffer buffer);
}
