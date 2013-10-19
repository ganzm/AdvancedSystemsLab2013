package ch.ethz.mlmq.test.logging;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import ch.ethz.mlmq.logging.PerformanceLogger;
import ch.ethz.mlmq.logging.PerformanceLoggerConfig;
import ch.ethz.mlmq.logging.PerformanceLoggerManager;
import ch.ethz.mlmq.util.FileHelper;

public class PerformanceLoggerManagerTest {

	private static String LOG_DIR = "test" + File.separator + "tmp";

	@BeforeClass
	public static void createLogDir() {
		new File(LOG_DIR).mkdirs();
	}

	@AfterClass
	public static void deleteLogDir() {
		FileHelper.deleteRecursive(LOG_DIR);
	}

	@Test
	public void testInitializedLogger() {
		String logPath = LOG_DIR;
		PerformanceLoggerManager.configureLogger(new PerformanceLoggerConfig(logPath));
		PerformanceLogger logger = PerformanceLoggerManager.getLogger();
		logger.log(100, "test");
		Assert.assertTrue(Files.isDirectory(Paths.get(logPath)));
		Assert.assertEquals(1, new File(logPath).listFiles().length);
	}
}
