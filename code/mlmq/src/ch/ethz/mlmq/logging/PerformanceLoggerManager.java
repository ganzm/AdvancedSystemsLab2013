package ch.ethz.mlmq.logging;

import java.util.logging.Logger;

public class PerformanceLoggerManager {

	private static final Logger julLogger = Logger.getLogger(PerformanceLoggerManager.class.getSimpleName());

	private static ThreadLocal<PerformanceLogger> logger;

	public static void configureLogger(PerformanceLoggerConfig loggerConfig) {
		final PerformanceLoggerConfig config = loggerConfig;
		logger = new ThreadLocal<PerformanceLogger>() {
			@Override
			protected PerformanceLogger initialValue() {
				return new PerformanceLoggerImpl(config);
			}
		};
	}

	public static void shutDown() {
		// TODO
	}

	/**
	 * disable performance logging
	 */
	public static void configureDisabled() {
		logger = new ThreadLocal<PerformanceLogger>() {
			@Override
			protected PerformanceLogger initialValue() {
				return new PerformanceLogger() {
					@Override
					public void log(long executionTime, String type) {
						julLogger.info("PerformanceLogger - ExecutionTime [" + executionTime + "]ms " + type);
					}
				};
			}
		};
	}

	public static PerformanceLogger getLogger() {
		if (logger == null) {
			throw new RuntimeException("PerformanceLogger is not configured");
		}
		return logger.get();
	}

}
