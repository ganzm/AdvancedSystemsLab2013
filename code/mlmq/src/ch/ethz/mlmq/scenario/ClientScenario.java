package ch.ethz.mlmq.scenario;

import java.io.IOException;
import java.util.logging.Logger;

import ch.ethz.mlmq.client.Client;
import ch.ethz.mlmq.client.ClientConfiguration;
import ch.ethz.mlmq.client.ClientImpl;
import ch.ethz.mlmq.exception.MlmqException;
import ch.ethz.mlmq.logging.LoggerUtil;

public abstract class ClientScenario extends Scenario {

	private static final Logger logger = Logger.getLogger(ClientScenario.class.getSimpleName());

	protected final ClientConfiguration config;

	protected final Client client;

	protected ClientScenario(ClientConfiguration config) {
		super(config);
		this.config = config;
		this.client = new ClientImpl(config);
	}

	@Override
	public void init() throws MlmqException {
		super.init();

		try {
			client.init();
		} catch (IOException e) {
			throw new MlmqException(e);
		}
	}

	@Override
	public void shutdown() {
		super.shutdown();
		try {
			client.close();
		} catch (IOException e) {
			logger.severe("Error while shuting down client" + LoggerUtil.getStackTraceString(e));
		}
	}
}
