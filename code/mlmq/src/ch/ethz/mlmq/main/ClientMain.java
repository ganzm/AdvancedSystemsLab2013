package ch.ethz.mlmq.main;

import java.io.IOException;
import java.net.ConnectException;
import java.util.logging.Logger;

import ch.ethz.mlmq.client.Client;
import ch.ethz.mlmq.client.ClientConfiguration;
import ch.ethz.mlmq.client.ClientImpl;
import ch.ethz.mlmq.common.CommandFileHandler;
import ch.ethz.mlmq.common.CommandListener;
import ch.ethz.mlmq.common.InvalidConfigurationException;
import ch.ethz.mlmq.exception.MlmqException;
import ch.ethz.mlmq.logging.LoggerUtil;
import ch.ethz.mlmq.logging.PerformanceLoggerManager;
import ch.ethz.mlmq.testrun.TestRunManager;

public class ClientMain extends RunningJar<ClientConfiguration> implements CommandListener {

	private static final Logger logger = Logger.getLogger(ClientMain.class.getSimpleName());

	private Client clientInterface;
	private TestRunManager testScenarioMgr;

	protected void doStartup() throws IOException {
		clientInterface = new ClientImpl(getConfig());
	}

	protected void doRun() throws IOException, InterruptedException, MlmqException {
		ClientConfiguration config = getConfig();
		for (int i = 0; !clientInterface.isConnected() && (i < config.getNumberOfConnectionAtempts() || config.getNumberOfConnectionAtempts() == -1); i++) {
			try {
				logger.info("Client init");
				clientInterface.init();
			} catch (ConnectException ex) {
				logger.warning("Could not connect to Broker " + config.getBrokerHost() + ":" + config.getBrokerPort());
				Thread.sleep(config.getReconnectSleepTime());
			}
		}
		logger.info("Client started");

		testScenarioMgr = new TestRunManager(clientInterface, config);
		testScenarioMgr.runTest();
	}

	protected void initConfig(String configurationFile) throws InvalidConfigurationException, IOException {
		setConfig(new ClientConfiguration(getProperties(configurationFile)));
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
		logger.info("Doing " + this.getClass().getSimpleName() + getConfig().getName() + " shutdown...");

		commandFileHandler.stop();

		try {
			clientInterface.close();
		} catch (IOException e) {
			logger.severe("Error while shutting down " + getConfig().getName() + " " + LoggerUtil.getStackTraceString(e));
		}

		PerformanceLoggerManager.shutDown();
	}
}
