package ch.ethz.mlmq.server;

import ch.ethz.mlmq.logging.PerformanceLoggerConfig;

public interface Configuration {

	PerformanceLoggerConfig getPerformanceLoggerConfig();

	long getCommandFileCheckIntervall();

	String getCommandoFilePath();

	String getName();

}
