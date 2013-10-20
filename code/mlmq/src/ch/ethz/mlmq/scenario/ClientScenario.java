package ch.ethz.mlmq.scenario;

import ch.ethz.mlmq.client.Client;
import ch.ethz.mlmq.client.ClientConfiguration;
import ch.ethz.mlmq.scenario.startup.ClientStartup;

public abstract class ClientScenario extends Scenario<Client, ClientConfiguration> {
	@Override
	protected ClientStartup initSut() {
		return new ClientStartup();
	}
}
