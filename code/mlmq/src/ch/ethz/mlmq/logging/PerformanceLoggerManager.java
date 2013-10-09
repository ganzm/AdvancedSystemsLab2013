package ch.ethz.mlmq.logging;

public class PerformanceLoggerManager {

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

	public static PerformanceLogger getLogger() {
		if (logger == null) {
			throw new RuntimeException("PerformanceLogger is not configured");
		}
		return logger.get();
	}

}
