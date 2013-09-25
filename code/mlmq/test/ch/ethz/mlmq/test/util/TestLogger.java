package ch.ethz.mlmq.test.util;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.logging.StreamHandler;

/**
 * Creates a simple java.util.Logger for UnitTests
 * 
 * @author ganzm
 * 
 */
public class TestLogger {

	private static Logger instance = initTestLogger();

	/**
	 * It's thread safe now, right?
	 * 
	 * @return
	 */
	public static Logger getLogger() {
		return instance;
	}

	private static Logger initTestLogger() {
		Logger logger = Logger.getLogger("TestLogger");

		Formatter formatter = new SimpleFormatter();

		StreamHandler streamHandler = new StreamHandler(System.out, formatter) {

			@Override
			public synchronized void publish(LogRecord record) {
				super.publish(record);
				// java.util.logging does not flush by default
				flush();
			}
		};

		logger.setUseParentHandlers(false);
		logger.addHandler(streamHandler);
		logger.info("TestLogger initialized");

		streamHandler.flush();

		return logger;
	}
}
