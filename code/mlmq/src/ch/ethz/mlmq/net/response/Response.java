package ch.ethz.mlmq.net.response;

import java.io.Serializable;

import ch.ethz.mlmq.net.HomeMadeSerializable;

/**
 * Marker interface for the response
 */
public interface Response extends Serializable, HomeMadeSerializable {

	int getTypeId();

}
