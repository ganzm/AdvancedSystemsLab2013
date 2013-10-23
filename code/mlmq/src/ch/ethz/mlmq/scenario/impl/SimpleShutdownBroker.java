package ch.ethz.mlmq.scenario.impl;

import java.util.logging.Logger;

import ch.ethz.mlmq.logging.LoggerUtil;
import ch.ethz.mlmq.scenario.BrokerScenario;
import ch.ethz.mlmq.server.BrokerConfiguration;

public class SimpleShutdownBroker extends BrokerScenario {

	private static final Logger logger = Logger.getLogger(SimpleShutdownBroker.class.getSimpleName());

	private final long shutdownDelay;

	private static final String SHUTDOWNDELAY_KEY = "scenario.SimpleShutdownBroker.shutdownDelay";

	private final Object lock = new Object();

	/**
	 * we need this boolean in case of shutdowndelay is 0 and the timeout method is faster than lock.wait()
	 */
	private volatile boolean isTimedOut;

	public SimpleShutdownBroker(BrokerConfiguration config) {
		super(config);

		shutdownDelay = config.getLongConfig(SHUTDOWNDELAY_KEY) * 1000;
	}

	@Override
	protected void onTimeout(String timerName) {
		synchronized (lock) {
			isTimedOut = true;
			lock.notify();
		}
	}

	@Override
	public void run() {
		logger.info("Run Shutdown Scenario with timeout " + shutdownDelay);
		isTimedOut = false;
		startTimer("BrokerShutdown", shutdownDelay);

		try {
			synchronized (lock) {
				if (!isTimedOut) {
					lock.wait();
				}
			}
		} catch (InterruptedException e) {
			logger.severe("Error in SimpleShutdown" + LoggerUtil.getStackTraceString(e));
		}
	}
}
