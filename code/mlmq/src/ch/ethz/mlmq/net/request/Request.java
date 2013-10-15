package ch.ethz.mlmq.net.request;

import java.io.Serializable;

import ch.ethz.mlmq.net.HomeMadeSerializable;

/**
 * Marker interface for the request
 */
public interface Request extends Serializable, HomeMadeSerializable {

	int getTypeId();

}
