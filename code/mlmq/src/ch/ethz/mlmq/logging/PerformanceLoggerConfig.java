package ch.ethz.mlmq.logging;

import java.io.File;

public class PerformanceLoggerConfig {

	private String directoryPath;
	private int count = 1;

	public PerformanceLoggerConfig(String directoryPath) {
		this.directoryPath = directoryPath;
	}

	public String getDirectoryPath() {
		return directoryPath;
	}

	public String logRotate() {
		++count;
		return getFileName();
	}

	public String getFileName() {
		return getDirectoryPath() + File.separator + Thread.currentThread().getId() + "_" + count + ".log";
	}

}
