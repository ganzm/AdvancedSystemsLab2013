package ch.ethz.mlmq.logging;


public interface PerformanceLogger {

	/**
	 * 
	 * @param executionTime
	 *            in milliseconds
	 */
	void log(long executionTime, String type);
}
