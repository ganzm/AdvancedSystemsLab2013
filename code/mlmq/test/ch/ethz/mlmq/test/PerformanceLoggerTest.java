package ch.ethz.mlmq.test;

import org.junit.Test;

import ch.ethz.mlmq.logging.PerformanceLoggerConfig;
import ch.ethz.mlmq.logging.PerformanceLoggerImpl;

public class PerformanceLoggerTest {

	@Test
	public void testPerformanceLogger() {

		PerformanceLoggerConfig config = new PerformanceLoggerConfig("log");
		PerformanceLoggerImpl logger = new PerformanceLoggerImpl(config);

		logger.log(100, "AnyType");
	}
}
