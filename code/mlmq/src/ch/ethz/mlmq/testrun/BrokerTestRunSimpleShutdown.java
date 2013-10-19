package ch.ethz.mlmq.testrun;

import java.util.logging.Logger;

import ch.ethz.mlmq.server.Broker;

public class BrokerTestRunSimpleShutdown extends BrokerTestRun {
	private final Logger logger = Logger.getLogger(BrokerTestRunSimpleShutdown.class.getSimpleName());

	private final long shutdownDelay;

	public BrokerTestRunSimpleShutdown(Broker broker, long shutdownDelay) {
		super(broker);
		this.shutdownDelay = shutdownDelay;
	}

	@Override
	protected void run(Broker broker) {
		super.startTimer("timer", shutdownDelay);
	}

	@Override
	protected void onTimeout(String name, Broker broker) {
		logger.info("Timeout " + name);
		broker.shutdown();
	}
}
