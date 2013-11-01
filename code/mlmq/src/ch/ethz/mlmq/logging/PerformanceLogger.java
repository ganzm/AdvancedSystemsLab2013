package ch.ethz.mlmq.logging;

public interface PerformanceLogger {

	/**
	 * 
	 * @param executionTime
	 *            in milliseconds
	 */
	void log(long executionTime, String type);

	void close();

	/**
	 * Adds context information to each and every logged line
	 * 
	 * @param key
	 * @param value
	 */
	void setContext(String key, String value);
}
