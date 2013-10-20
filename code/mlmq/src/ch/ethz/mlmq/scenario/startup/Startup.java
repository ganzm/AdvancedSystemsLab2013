package ch.ethz.mlmq.scenario.startup;

import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;

import ch.ethz.mlmq.common.CommandFileHandler;
import ch.ethz.mlmq.common.CommandListener;
import ch.ethz.mlmq.common.InvalidConfigurationException;
import ch.ethz.mlmq.logging.LoggerUtil;
import ch.ethz.mlmq.logging.PerformanceLoggerManager;
import ch.ethz.mlmq.scenario.Startable;
import ch.ethz.mlmq.server.Configuration;
import ch.ethz.mlmq.util.ConfigurationUtil;

public abstract class Startup<T_SUT extends Startable, T_CONFIG extends Configuration> implements CommandListener {

	protected abstract T_CONFIG initConfig(String configurationFile) throws InvalidConfigurationException, IOException;

	protected boolean processCommand(String command) {
		return false;
	}

	protected abstract T_SUT initSut() throws Exception;

	protected void doStart() throws Exception {
	}

	protected abstract void executeShutdown() throws Exception;

	private static final Logger logger = Logger.getLogger(BrokerStartup.class.getSimpleName());
	protected CommandFileHandler commandFileHandler;
	private T_CONFIG config;
	private T_SUT sut;

	protected Properties getProperties(String configurationFile) throws IOException {
		Properties props = ConfigurationUtil.loadPropertiesFromFile(configurationFile);
		return props;
	}

	protected void initPerformanceLog(T_CONFIG config) {
		logger.info("Configuring Performance Logger");
		PerformanceLoggerManager.configureLogger(config.getPerformanceLoggerConfig());
	}

	protected void initCommandFileHandler() {
		logger.info("CommandFileHandler...");
		commandFileHandler = new CommandFileHandler(config.getCommandoFilePath(), config.getCommandFileCheckIntervall(), this);
		commandFileHandler.start();
		logger.info("CommandFileHandler started");
	}

	protected void setConfig(T_CONFIG config) {
		this.config = config;
	}

	public void start(String configurationFile) throws Exception {
		setConfig(initConfig(configurationFile));
		initPerformanceLog(getConfig());

		logger.info("Starting " + getName());
		this.sut = initSut();
		logger.info("Started " + getName());

		initCommandFileHandler();

		sut.startup();
		doStart();
	}

	private String getName() {
		return this.getClass().getSimpleName() + " " + getConfig().getName();
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

	public T_CONFIG getConfig() {
		return config;
	}

	public T_SUT getSut() {
		return sut;
	}

}
