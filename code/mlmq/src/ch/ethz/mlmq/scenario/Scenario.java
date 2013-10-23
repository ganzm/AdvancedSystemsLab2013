package ch.ethz.mlmq.scenario;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import ch.ethz.mlmq.command.CommandFileHandler;
import ch.ethz.mlmq.command.CommandListener;
import ch.ethz.mlmq.common.Configuration;
import ch.ethz.mlmq.exception.MlmqException;
import ch.ethz.mlmq.logging.LoggerUtil;
import ch.ethz.mlmq.logging.PerformanceLoggerManager;

public abstract class Scenario implements CommandListener {

	private static final Logger logger = Logger.getLogger(Scenario.class.getSimpleName());

	private final Timer timer = new Timer(true);

	private final ConcurrentHashMap<String, TimerTask> activetimerTasks = new ConcurrentHashMap<>();

	private final CommandFileHandler commandFileHandler;

	protected Scenario(Configuration config) {
		// init PerformanceLogger
		PerformanceLoggerManager.configureLogger(config.getPerformanceLoggerConfig());

		// init command file Handler
		commandFileHandler = new CommandFileHandler(config.getCommandFileHandlerPath(), config.getCommandFileHandlerCheckIntervall(), this);
	}

	/**
	 * initializes stuff
	 * 
	 * @throws MlmqException
	 */
	public void init() throws MlmqException {
		commandFileHandler.start();
	}

	/**
	 * Performs a specific scenario
	 * 
	 * as soon as this method terminates the system will shutdown by calling shutdown()
	 */
	public abstract void run() throws Exception;

	public void shutdown() {
		commandFileHandler.stop();
		PerformanceLoggerManager.shutDown();
	}

	protected void startTimer(final String name, long timeout) {
		logger.fine("Start Timer [" + name + "] with timeout [" + timeout + "]ms");

		TimerTask timerTask = new TimerTask() {
			@Override
			public void run() {
				timedOut(name);
			}
		};

		activetimerTasks.put(name, timerTask);
		timer.schedule(timerTask, timeout);
	}

	protected boolean clearTimer(String name) {
		logger.fine("Clear Timer [" + name + "]");

		TimerTask toStop = activetimerTasks.remove(name);
		if (toStop != null) {
			toStop.cancel();
			return true;
		}
		return false;
	}

	private void timedOut(String timerName) {
		activetimerTasks.remove(timerName);

		logger.info("Timer Timeout [" + timerName + "]");
		onTimeout(timerName);
	}

	/**
	 * Override this method to listen to timer timeouts
	 * 
	 * @param timerName
	 */
	protected void onTimeout(String timerName) {

	}

	/**
	 * Callback from CommandoFileHandler
	 */
	@Override
	public void onCommand(String command) {
		logger.info("BrokerCommandFile - onCommand [" + command + "]");

		command = command.toLowerCase();
		if (command.contains(CommandFileHandler.COMMAND_SHUTDOWN)) {
			shutdown();
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
}
