package ch.ethz.mlmq.test;

import java.io.IOException;
import java.util.logging.Logger;

import org.junit.BeforeClass;
import org.junit.Test;

import ch.ethz.mlmq.logging.LoggerUtil;

public class LoggingTest {

	@BeforeClass
	public static void beforeClass() throws IOException {

		LoggerUtil.initConsoleDebug();
	}

	@Test
	public void testLogger() {

		Logger l1 = Logger.getGlobal();
		l1.info("Info Hello Global Logger");
		l1.severe("Severe Hello Global Logger");
		l1.fine("fine Hello Global Logger");
		l1.finer("finer Hello Global Logger");
		l1.finest("finest Hello Global Logger");

		Logger l2 = Logger.getLogger("anylogger");
		l2.info("Hello anylogger Logger");

	}
}
