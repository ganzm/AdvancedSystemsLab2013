package ch.ethz.mlmq.logging;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.logging.Logger;

class PerformanceLoggerImpl implements PerformanceLogger {

	private static final String LOG_FORMAT = "%d;%tY%tm%td%tH%tM%tS;%s\n"; // ignore the timezone: %tz

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

		File directory = new File(loggerConfig.getFileName());
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
		String.format(LOG_FORMAT, executionTime, Calendar.getInstance(TimeZone.getTimeZone("GMT")), type);
		try {
			if (writer == null)
				writer = new FileWriter(loggerConfig.getFileName(), true);
			Calendar currentCalendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
			writer.write(String.format(LOG_FORMAT, executionTime, currentCalendar, type));
		} catch (IOException e) {
			logger.severe("Error while logging " + LoggerUtil.getStackTraceString(e));
			try {
				writer.close();
			} catch (IOException e1) {
				logger.severe("Error while closing writer " + LoggerUtil.getStackTraceString(e1));
			}
			writer = null;
			e.printStackTrace();
		}
	}
}
