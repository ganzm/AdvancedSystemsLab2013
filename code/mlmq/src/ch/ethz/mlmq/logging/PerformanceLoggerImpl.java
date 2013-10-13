package ch.ethz.mlmq.logging;

import java.io.Closeable;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;

public class PerformanceLoggerImpl implements PerformanceLogger, Closeable {

	private static final String LOG_FORMAT = "%d;%s;%s\n"; // ignore the timezone: %tz

	private SimpleDateFormat dateFormat = new SimpleDateFormat("%Y%m%d%H%M%S");

	private final Logger logger = Logger.getLogger(PerformanceLoggerImpl.class.getSimpleName());

	private PerformanceLoggerConfig loggerConfig;

	private FileWriter writer;

	public PerformanceLoggerImpl(PerformanceLoggerConfig loggerConfig) {

		validateConfig(loggerConfig);

		this.loggerConfig = loggerConfig;
	}

	private void validateConfig(PerformanceLoggerConfig loggerConfig) {
		if (loggerConfig == null) {
			throw new RuntimeException("Invalid logger config");
		}

		File directory = new File(loggerConfig.getDirectoryPath());
		if (directory.exists() && !directory.isDirectory()) {
			throw new RuntimeException("Expected [" + loggerConfig.getFileName() + "] to be a directory");
		}

		if (!directory.exists()) {
			if (!directory.mkdirs()) {
				throw new RuntimeException("Could not create PerformanceLogger Output Directory " + loggerConfig.getFileName());
			}
		}
	}

	@Override
	public void log(long executionTime, String type) {
		try {

			String logMessage = String.format(LOG_FORMAT, executionTime, dateFormat.format(new Date()), type);
			if (writer == null) {
				writer = new FileWriter(loggerConfig.getFileName(), true);
			}
			writer.write(logMessage);

		} catch (IOException e) {
			logger.severe("Error while logging " + LoggerUtil.getStackTraceString(e));
			closeWriter();
		}
	}

	private void closeWriter() {
		try {
			writer.close();
		} catch (IOException e1) {
			logger.severe("Error while closing writer " + LoggerUtil.getStackTraceString(e1));
		}
		writer = null;
	}

	public void close() {
		closeWriter();
	}

}
