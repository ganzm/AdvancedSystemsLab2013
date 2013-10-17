package ch.ethz.mlmq.logging;

import java.io.Closeable;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.logging.Logger;

public class PerformanceLoggerImpl implements PerformanceLogger, Closeable {

	private static final String LOG_FORMAT = "%d;%2$tY%2$tm%2$td%2$tH%2$tM%2$tS;%3$s\n"; // ignore the timezone: %2$tz

	private final Logger logger = Logger.getLogger(PerformanceLoggerImpl.class.getSimpleName());

	private PerformanceLoggerConfig loggerConfig;

	private FileWriter writer;

	public PerformanceLoggerImpl(PerformanceLoggerConfig loggerConfig) {
		validateConfig(loggerConfig);
		this.loggerConfig = loggerConfig;
		initWriter();
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
		String logMessage = String.format(LOG_FORMAT, executionTime, getCurrentDate().getTime(), type);
		write(logMessage);
	}

	protected void write(String logMessage) {
		try {
			if (writer == null) {
				initWriter();
			}
			writer.write(logMessage);
		} catch (IOException e) {
			logger.severe("Error while logging " + LoggerUtil.getStackTraceString(e));
			closeWriter();
		}
	}

	private void initWriter() {
		try {
			writer = new FileWriter(loggerConfig.getFileName(), true);
		} catch (IOException e) {
			logger.severe("Error while creating logfile " + LoggerUtil.getStackTraceString(e));
			closeWriter();
		}
	}

	protected Calendar getCurrentDate() {
		return Calendar.getInstance(TimeZone.getTimeZone("GMT"));
	}

	private void closeWriter() {
		if (writer == null)
			return;

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
