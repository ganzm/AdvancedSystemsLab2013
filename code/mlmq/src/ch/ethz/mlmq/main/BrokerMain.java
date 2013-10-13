package ch.ethz.mlmq.main;

import java.util.Properties;
import java.util.logging.Logger;

import ch.ethz.mlmq.logging.LoggerUtil;
import ch.ethz.mlmq.logging.PerformanceLoggerManager;
import ch.ethz.mlmq.server.Broker;
import ch.ethz.mlmq.server.BrokerCommandFileHandler;
import ch.ethz.mlmq.server.BrokerConfiguration;
import ch.ethz.mlmq.server.CommandListener;
import ch.ethz.mlmq.util.ConfigurationUtil;

public class BrokerMain implements CommandListener {

	private static final Logger logger = Logger.getLogger(BrokerMain.class.getSimpleName());

	private BrokerConfiguration config;
	private Broker broker;
	private BrokerCommandFileHandler commandFileHandler;

	public int run(String brokerConfigurationFile) {

		try {
			Properties props = ConfigurationUtil.loadPropertiesFromFile(brokerConfigurationFile);
			config = new BrokerConfiguration(props);
			broker = new Broker(config);

			logger.info("Configuring Performance Logger");
			PerformanceLoggerManager.configureLogger(config.getPerformanceLoggerConfig());
			logger.info("Starting Broker...");
			broker.startup();
			logger.info("Broker started");

			logger.info("CommandFileHandler...");
			commandFileHandler = new BrokerCommandFileHandler(config.getCommandoFilePath(), config.getCommandFileCheckIntervall(), this);
			commandFileHandler.start();
			logger.info("CommandFileHandler started");

			broker.join();

			return 0;
		} catch (Exception e) {
			logger.severe("Exception " + LoggerUtil.getStackTraceString(e));
			return -1;
		}
	}

	@Override
	public void onCommand(String command) {
		logger.info("BrokerCommandFile - onCommand [" + command + "]");

		command = command.toLowerCase();
		if (command.contains(BrokerCommandFileHandler.COMMAND_SHUTDOWN)) {
			doShutdown();
			return;
		} else if (command.contains(BrokerCommandFileHandler.COMMAND_LOG_STACKTRACE)) {
			LoggerUtil.logStackTrace(logger);
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
}
