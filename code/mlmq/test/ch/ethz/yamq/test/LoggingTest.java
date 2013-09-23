package ch.ethz.yamq.test;

import java.util.logging.LogManager;
import java.util.logging.Logger;

import org.junit.Test;

public class LoggingTest {

	private static final Logger logger = Logger.getLogger("default");
	
	@Test
	public void testLogger(){
		
		LogManager mgr = LogManager.getLogManager();
		mgr.addLogger(logger);
		
		logger.info("Hello Logger");
	}
}
