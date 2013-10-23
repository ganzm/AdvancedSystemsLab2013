package ch.ethz.mlmq.scenario;

import ch.ethz.mlmq.scenario.startup.BrokerStartup;
import ch.ethz.mlmq.server.Broker;
import ch.ethz.mlmq.server.BrokerConfiguration;

public abstract class BrokerScenario extends Scenario<Broker, BrokerConfiguration> {
	@Override
	protected BrokerStartup initSut() {
		return new BrokerStartup();
	}
}
