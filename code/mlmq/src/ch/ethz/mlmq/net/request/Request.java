package ch.ethz.mlmq.net.request;

import java.io.Serializable;
import java.nio.ByteBuffer;

/**
 * Marker interface for the request
 */
public interface Request extends Serializable {

	void serialize(ByteBuffer buffer);

	void deserialize(ByteBuffer serializeBuffer);

	int getTypeId();

}
