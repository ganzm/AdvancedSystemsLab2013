package ch.ethz.mlmq.logging;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.LogManager;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.StreamHandler;

public class LoggerUtil {

	/**
	 * Debug Log configuration properties (key=value)
	 */
	//@formatter:off
	private static final String loggingProperties = ""
			+ "handlers= java.util.logging.ConsoleHandler\n" 
			+ ".level= ALL\n";
	//@formatter:on

	private static boolean isConsoleInitialized = false;

	/**
	 * Reads logging configuration from file 'logging.properties' and inits java.util.logging
	 * 
	 * @throws IOException
	 */
	public static void initDefault() throws IOException {
		LogManager mgr = LogManager.getLogManager();

		ClassLoader classLoader = LoggerUtil.class.getClassLoader();
		try (InputStream ins = classLoader.getResourceAsStream("logging.properties")) {
			mgr.readConfiguration(ins);
		}
	}

	public static void initFromFile(String fileName) throws IOException {
		LogManager mgr = LogManager.getLogManager();

		File file = new File(fileName);
		if (!file.exists()) {
			throw new IOException("Logger config file does not exist " + fileName);
		}
		try (InputStream ins = new FileInputStream(file)) {
			mgr.readConfiguration(ins);
		}
	}

	/**
	 * Use this method to setup your TestLoggers
	 */
	public static void initConsoleDebug() {

		if (isConsoleInitialized) {
			return;
		}
		try {
			try (ByteArrayInputStream ins = new ByteArrayInputStream(loggingProperties.getBytes())) {
				LogManager mgr = LogManager.getLogManager();
				mgr.readConfiguration(ins);
			}

			initStdOut();
		} catch (IOException ex) {
			Logger.getGlobal().severe("Error while initializing ConsoleDebug logging " + getStackTraceString(ex));
		} finally {
			isConsoleInitialized = true;
		}
	}

	/**
	 * Helper converts the stack trace of an exception to a string
	 * 
	 * @param ex
	 * @return
	 */
	public static String getStackTraceString(Exception ex) {
		ByteArrayOutputStream bOut = new ByteArrayOutputStream();
		try (PrintStream printStream = new PrintStream(bOut)) {
			ex.printStackTrace(printStream);
			return new String(bOut.toByteArray());
		}
	}

	/**
	 * Replaces ConsoleLogger with stdout logger
	 */
	public static void initStdOut() {

		Logger rootLogger = Logger.getGlobal().getParent();
		Handler[] handlers = rootLogger.getHandlers();

		for (Handler handler : handlers) {
			if (handler instanceof ConsoleHandler) {
				ConsoleHandler cHandler = (ConsoleHandler) handler;

				// create new handler
				StreamHandler stdOutHandler = new StreamHandler(System.out, cHandler.getFormatter()) {
					@Override
					public synchronized void publish(LogRecord record) {
						super.publish(record);
						flush();
					}
				};
				stdOutHandler.setLevel(rootLogger.getLevel());

				// replace handler
				rootLogger.addHandler(stdOutHandler);
				rootLogger.removeHandler(cHandler);

				return;
			}
		}
	}

	public static void logStackTrace(Logger logger) {
		StringBuffer buf = new StringBuffer();

		Map<Thread, StackTraceElement[]> stacktraces = Thread.getAllStackTraces();
		Iterator<Entry<Thread, StackTraceElement[]>> iter = stacktraces.entrySet().iterator();

		while (iter.hasNext()) {
			Entry<Thread, StackTraceElement[]> entry = iter.next();

			Thread t = entry.getKey();
			StackTraceElement[] elements = entry.getValue();

			buf.append("Thread ");
			buf.append(t.getName());

			for (int i = 0; i < elements.length; i++) {
				StackTraceElement element = elements[i];
				buf.append("\n\t");
				buf.append(element.getClassName());
				buf.append("   ");
				buf.append(element.getMethodName());
				buf.append("   ");
				buf.append(element.getLineNumber());

			}

			buf.append("\n\n");
		}

		logger.info(buf.toString());
	}

}
