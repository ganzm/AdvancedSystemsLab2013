package ch.ethz.mlmq.testrun;

import java.util.logging.Logger;

import ch.ethz.mlmq.server.Broker;

public class BrokerTestRunSimpleShutdown extends BrokerTestRun {
	private final Logger logger = Logger.getLogger(BrokerTestRunSimpleShutdown.class.getSimpleName());

	private final long shutdownDelay;
	private final Broker broker;

	public BrokerTestRunSimpleShutdown(Broker broker, long shutdownDelay) {
		this.broker = broker;
		this.shutdownDelay = shutdownDelay;
	}

	@Override
	void run() {
		super.startTimer("timer", shutdownDelay);
	}

	@Override
	protected void onTimeout(String name) {
		logger.info("Timeout " + name);
		broker.shutdown();
	}
}
