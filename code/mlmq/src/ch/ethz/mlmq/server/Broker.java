package ch.ethz.mlmq.server;

import ch.ethz.mlmq.exception.MlmqException;

/**
 * Interface to the broker which we expose to the TestRunManager
 * 
 * All calls on this interface need to be thread safe on the implementation
 */
public interface Broker {

	void startup() throws MlmqException;

	/**
	 * Terminates the Broker
	 */
	void shutdown();
}
