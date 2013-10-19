package ch.ethz.mlmq.scenario;

import java.util.logging.Logger;

import ch.ethz.mlmq.exception.MlmqException;
import ch.ethz.mlmq.server.Broker;
import ch.ethz.mlmq.server.BrokerConfiguration;
import ch.ethz.mlmq.server.BrokerImpl;

public abstract class BrokerScenario extends Scenario {
	private static final Logger logger = Logger.getLogger(BrokerScenario.class.getSimpleName());

	protected final BrokerConfiguration config;

	protected Broker broker;

	protected BrokerScenario(BrokerConfiguration config) {
		super(config);
		this.config = config;
		this.broker = new BrokerImpl(config);
	}

	@Override
	public void init() throws MlmqException {
		logger.info("Starting BrokerScenario");

		super.init();
		broker.startup();
	}

	@Override
	public void shutdown() {
		logger.info("Shutting down BrokerScenario");

		super.shutdown();
		broker.shutdown();
	}
}
