package ch.ethz.mlmq.main;

import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;

import ch.ethz.mlmq.common.CommandFileHandler;
import ch.ethz.mlmq.common.CommandListener;
import ch.ethz.mlmq.common.InvalidConfigurationException;
import ch.ethz.mlmq.logging.LoggerUtil;
import ch.ethz.mlmq.logging.PerformanceLoggerManager;
import ch.ethz.mlmq.server.Configuration;
import ch.ethz.mlmq.util.ConfigurationUtil;

public abstract class RunningJar<T extends Configuration> implements CommandListener {

	protected abstract T initConfig(String configurationFile) throws InvalidConfigurationException, IOException;

	protected abstract boolean processCommand(String command);

	protected abstract void doStartup() throws Exception;

	protected abstract void doRun() throws Exception;

	protected abstract void executeShutdown() throws Exception;

	private static final Logger logger = Logger.getLogger(BrokerMain.class.getSimpleName());
	protected CommandFileHandler commandFileHandler;
	private T config;

	protected Properties getProperties(String configurationFile) throws IOException {
		Properties props = ConfigurationUtil.loadPropertiesFromFile(configurationFile);
		return props;
	}

	protected void initPerformanceLog(T config) {
		logger.info("Configuring Performance Logger");
		PerformanceLoggerManager.configureLogger(config.getPerformanceLoggerConfig());
	}

	protected void initCommandFileHandler() {
		logger.info("CommandFileHandler...");
		commandFileHandler = new CommandFileHandler(config.getCommandoFilePath(), config.getCommandFileCheckIntervall(), this);
		commandFileHandler.start();
		logger.info("CommandFileHandler started");
	}

	protected void setConfig(T config) {
		this.config = config;
	}

	protected T getConfig() {
		return config;
	}

	public int run(String configurationFile) {
		try {
			setConfig(initConfig(configurationFile));
			initPerformanceLog(getConfig());

			logger.info("Starting " + getName());
			doStartup();
			logger.info("Started " + getName());

			initCommandFileHandler();

			doRun();

			return 0;
		} catch (Exception e) {
			logger.severe("Exception " + LoggerUtil.getStackTraceString(e));
			return -1;
		}
	}

	private String getName() {
		String msg = this.getClass().getSimpleName() + " " + getConfig().getName();
		return msg;
	}

	public void onCommand(String command) {
		logger.info("CommandFile - onCommand [" + command + "]");

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
			if (!processCommand(command))
				logger.info("BrokerCommand unexpected command [" + command + "]");
		}
	}

	private void doShutdown() {
		logger.info("Doing Broker shutdown...");

		commandFileHandler.stop();

		try {
			executeShutdown();
		} catch (Exception e) {
			logger.severe("Error while shutting down " + getName() + " " + LoggerUtil.getStackTraceString(e));

		}

		PerformanceLoggerManager.shutDown();
	}

}
