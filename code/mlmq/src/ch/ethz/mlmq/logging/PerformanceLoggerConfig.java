package ch.ethz.mlmq.logging;

import java.io.File;

public class PerformanceLoggerConfig {

	private final String directoryPath;
	private final String filePrefix;
	private int count = 1;

	public PerformanceLoggerConfig(String directoryPath, String filePrefix) {
		this.directoryPath = directoryPath;
		this.filePrefix = filePrefix;
	}

	public String getDirectoryPath() {
		return directoryPath;
	}

	public String logRotate() {
		++count;
		return getFileName();
	}

	public String getFileName() {
		return getDirectoryPath() + File.separator + filePrefix + count + ".log";
	}

}
