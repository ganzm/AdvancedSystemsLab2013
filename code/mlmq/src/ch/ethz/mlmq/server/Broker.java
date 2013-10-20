package ch.ethz.mlmq.server;

import ch.ethz.mlmq.scenario.Startable;

/**
 * Interface to the broker which we expose to the TestRunManager
 * 
 * All calls on this interface need to be thread safe
 */
public interface Broker extends Startable {

	/**
	 * Terminates the Broker
	 */
	void shutdown();
}
