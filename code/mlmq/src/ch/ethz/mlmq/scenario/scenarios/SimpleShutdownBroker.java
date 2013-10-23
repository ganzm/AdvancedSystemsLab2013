package ch.ethz.mlmq.scenario.scenarios;

import java.util.logging.Logger;

import ch.ethz.mlmq.scenario.BrokerScenario;
import ch.ethz.mlmq.server.Broker;
import ch.ethz.mlmq.server.BrokerConfiguration;

public class SimpleShutdownBroker extends BrokerScenario {
	private final Logger logger = Logger.getLogger(SimpleShutdownBroker.class.getSimpleName());

	private long shutdownDelay;

	@Override
	protected void run(Broker broker, BrokerConfiguration config) {
		super.startTimer("timer", shutdownDelay);
		this.shutdownDelay = config.getShutdownDelay();
	}

	@Override
	protected void onTimeout(String name, Broker broker) {
		logger.info("Timeout " + name);
		broker.shutdown();
	}
}
