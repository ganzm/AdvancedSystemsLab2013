package ch.ethz.mlmq.net.response;

import java.io.Serializable;

import ch.ethz.mlmq.net.MlmqSerializable;

/**
 * Marker interface for the response
 */
public interface Response extends Serializable, MlmqSerializable {

	int getTypeId();

}
