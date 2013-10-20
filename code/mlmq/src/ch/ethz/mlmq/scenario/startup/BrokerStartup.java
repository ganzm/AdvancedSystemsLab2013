package ch.ethz.mlmq.scenario.startup;

import java.io.IOException;
import java.util.logging.Logger;

import ch.ethz.mlmq.exception.MlmqException;
import ch.ethz.mlmq.server.Broker;
import ch.ethz.mlmq.server.BrokerConfiguration;
import ch.ethz.mlmq.server.BrokerImpl;

public class BrokerStartup extends Startup<Broker, BrokerConfiguration> {

	static final Logger logger = Logger.getLogger(BrokerStartup.class.getSimpleName());

	@Override
	protected BrokerConfiguration initConfig(String configurationFile) throws IOException {
		return new BrokerConfiguration(getProperties(configurationFile));
	}

	@Override
	protected Broker initSut() throws MlmqException {
		return new BrokerImpl(getConfig());
	}

	protected void executeShutdown() {
		getSut().shutdown();
	}

}
