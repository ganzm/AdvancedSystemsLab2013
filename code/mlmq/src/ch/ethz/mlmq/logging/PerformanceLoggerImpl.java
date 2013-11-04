package ch.ethz.mlmq.logging;

import java.io.Closeable;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

public class PerformanceLoggerImpl implements PerformanceLogger, Closeable {

	private static final String LOG_FORMAT = "%d;%d;%3$s;%4$s\n";

	private final ConcurrentHashMap<String, String> contextMap = new ConcurrentHashMap<>();
	private String contextString = "";

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
	public synchronized void log(long executionTime, String type) {
		String logMessage = String.format(LOG_FORMAT, executionTime, getCurrentDate().getTimeInMillis(), type, contextString);
		write(logMessage);
	}

	protected synchronized void write(String logMessage) {
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

	@Override
	public void setContext(String key, String value) {
		contextMap.put(key, value);
		contextString = contextMapToString();
	}

	private String contextMapToString() {
		StringBuilder sb = new StringBuilder();
		Set<Entry<String, String>> entrySet = contextMap.entrySet();
		for (Entry<String, String> entry : entrySet) {
			sb.append(entry.getKey());
			sb.append("[");
			sb.append(entry.getValue());
			sb.append("]");
		}

		return sb.toString();
	}

}
