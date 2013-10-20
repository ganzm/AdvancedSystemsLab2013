package ch.ethz.mlmq.scenario.startup;

import java.io.IOException;

import ch.ethz.mlmq.client.Client;
import ch.ethz.mlmq.client.ClientConfiguration;
import ch.ethz.mlmq.client.ClientImpl;
import ch.ethz.mlmq.common.InvalidConfigurationException;

public class ClientStartup extends Startup<Client, ClientConfiguration> {

	protected ClientConfiguration initConfig(String configurationFile) throws InvalidConfigurationException, IOException {
		return new ClientConfiguration(getProperties(configurationFile));
	}

	protected Client initSut() throws IOException {
		return new ClientImpl(getConfig());
	}

	protected void executeShutdown() throws IOException {
		getSut().close();
	}
}
