package ch.ethz.mlmq.server;

/**
 * Interface to the broker which we expose to the TestRunManager
 * 
 * All calls on this interface need to be thread safe
 */
public interface Broker {

	/**
	 * Terminates the Broker
	 */
	void shutdown();
}
