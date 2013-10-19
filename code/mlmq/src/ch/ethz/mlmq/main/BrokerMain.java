package ch.ethz.mlmq.main;

import java.io.IOException;
import java.util.logging.Logger;

import ch.ethz.mlmq.common.CommandFileHandler;
import ch.ethz.mlmq.common.CommandListener;
import ch.ethz.mlmq.exception.MlmqException;
import ch.ethz.mlmq.logging.LoggerUtil;
import ch.ethz.mlmq.logging.PerformanceLoggerManager;
import ch.ethz.mlmq.server.BrokerConfiguration;
import ch.ethz.mlmq.server.BrokerImpl;
import ch.ethz.mlmq.testrun.TestRunManager;

public class BrokerMain extends RunningJar<BrokerConfiguration> implements CommandListener {

	static final Logger logger = Logger.getLogger(BrokerMain.class.getSimpleName());

	private BrokerImpl broker;
	private TestRunManager testScenarioMgr;

	@Override
	protected void doRun() throws MlmqException {
		// TODO make configurable
		testScenarioMgr = new TestRunManager(broker, 1);
		testScenarioMgr.runTest();
	}

	@Override
	protected void doStartup() throws MlmqException {
		broker = new BrokerImpl(getConfig());
		broker.startup();
	}

	@Override
	public void onCommand(String command) {
		logger.info("BrokerCommandFile - onCommand [" + command + "]");

		command = command.toLowerCase();
		if (command.contains(CommandFileHandler.COMMAND_SHUTDOWN)) {
			doShutdown();
			return;
		} else if (command.contains(CommandFileHandler.COMMAND_LOG_STACKTRACE)) {
			LoggerUtil.logStackTrace(logger);
			return;
		} else if (command.contains(CommandFileHandler.COMMAND_LOG_MEMORY)) {
			LoggerUtil.logMemory(logger);
			return;
		} else {
			logger.info("BrokerCommand unexpected command [" + command + "]");
		}
	}

	private void doShutdown() {
		logger.info("Doing Broker shutdown...");

		commandFileHandler.stop();

		broker.shutdown();

		PerformanceLoggerManager.shutDown();
	}

	@Override
	protected void initConfig(String configurationFile) throws IOException {
		setConfig(new BrokerConfiguration(getProperties(configurationFile)));
	}
}
