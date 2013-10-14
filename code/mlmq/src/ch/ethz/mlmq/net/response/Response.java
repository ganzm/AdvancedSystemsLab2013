package ch.ethz.mlmq.net.response;

import java.io.Serializable;
import java.nio.ByteBuffer;

/**
 * Marker interface for the response
 */
public interface Response extends Serializable {

	void serialize(ByteBuffer buffer);

	void deserialize(ByteBuffer serializeBuffer);

	int getTypeId();

}
