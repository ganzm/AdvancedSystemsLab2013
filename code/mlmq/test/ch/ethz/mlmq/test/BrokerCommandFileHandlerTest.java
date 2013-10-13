package ch.ethz.mlmq.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Logger;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import ch.ethz.mlmq.logging.LoggerUtil;
import ch.ethz.mlmq.server.BrokerCommandFileHandler;
import ch.ethz.mlmq.server.CommandListener;

public class BrokerCommandFileHandlerTest {

	private static final Logger logger = Logger.getLogger(BrokerCommandFileHandlerTest.class.getSimpleName());

	private BrokerCommandFileHandler fileHandler;

	private String pathToFileToWatch = "test" + File.separator + "blub" + File.separator + "brokercommando.txt";

	private long fileCheckIntervall = 500;

	private String receivedCommand = null;

	@BeforeClass
	public static void beforeClass() throws IOException {
		LoggerUtil.initConsoleDebug();
	}

	@Before
	public void before() {
		CommandListener commandListener = new CommandListener() {

			@Override
			public void onCommand(String command) {
				Assert.assertNull("Expect this method to be called just once", receivedCommand);
				receivedCommand = command;
			}
		};

		fileHandler = new BrokerCommandFileHandler(pathToFileToWatch, fileCheckIntervall, commandListener);
		fileHandler.start();
	}

	@After
	public void after() {
		fileHandler.stop();
	}

	@Test
	public void doTest() throws InterruptedException, FileNotFoundException, IOException {
		logger.info("Start Test");

		// cheap way to wait for startup
		Thread.sleep(1000);

		File file = new File(pathToFileToWatch);

		try (FileOutputStream fout = new FileOutputStream(file)) {
			fout.write(BrokerCommandFileHandler.COMMAND_SHUTDOWN.getBytes());
			Thread.sleep(1000);
		}

		Assert.assertEquals(BrokerCommandFileHandler.COMMAND_SHUTDOWN, receivedCommand);

		// and again
		receivedCommand = null;

		try (FileOutputStream fout = new FileOutputStream(file)) {
			fout.write(BrokerCommandFileHandler.COMMAND_LOG_STACKTRACE.getBytes());
			Thread.sleep(1000);
		}

		Assert.assertEquals(BrokerCommandFileHandler.COMMAND_LOG_STACKTRACE, receivedCommand);
	}
}
