package ch.ethz.mlmq.logging;

import java.util.logging.Logger;

public class PerformanceLoggerManager {

	private static final Logger julLogger = Logger.getLogger(PerformanceLoggerManager.class.getSimpleName());

	private static PerformanceLogger logger;

	public static void configureLogger(PerformanceLoggerConfig loggerConfig, String initialLog) {
		final PerformanceLoggerConfig config = loggerConfig;
		PerformanceLoggerImpl loggerImpl = new PerformanceLoggerImpl(config);

		// write one line of log header data
		if (initialLog != null) {
			loggerImpl.write(initialLog);
			loggerImpl.write("\n");
		}

		logger = loggerImpl;
	}

	public static void shutDown() {
		if (logger != null) {
			logger.close();
		}
	}

	/**
	 * disable performance logging
	 */
	public static void configureDisabled() {
		logger = new PerformanceLogger() {
			@Override
			public void log(long executionTime, String type) {
				julLogger.info("PerformanceLogger - ExecutionTime [" + executionTime + "]ms " + type);
			}

			@Override
			public void close() {
				// do nothing
			}

			@Override
			public void setContext(String key, String value) {
				// do nothing
			}
		};
	}

	public static PerformanceLogger getLogger() {
		if (logger == null) {
			throw new RuntimeException("PerformanceLogger is not configured");
		}
		return logger;
	}

}
