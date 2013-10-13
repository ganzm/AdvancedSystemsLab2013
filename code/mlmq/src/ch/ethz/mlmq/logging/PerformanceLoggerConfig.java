package ch.ethz.mlmq.logging;

import java.io.File;

public class PerformanceLoggerConfig {

	private String directoryPath;

	public PerformanceLoggerConfig(String directoryPath) {
		this.directoryPath = directoryPath;
	}

	public String getDirectoryPath() {
		return directoryPath;
	}

	public String getFileName() {
		return directoryPath + File.separator + Thread.currentThread().getId() + ".log";
	}

}
