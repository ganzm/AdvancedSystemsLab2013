package ch.ethz.mlmq.scenario;

import java.io.IOException;
import java.util.logging.Logger;

import ch.ethz.mlmq.client.Client;
import ch.ethz.mlmq.client.ClientConfiguration;
import ch.ethz.mlmq.client.ClientImpl;
import ch.ethz.mlmq.dto.ClientDto;
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
		delayClient();
	}

	protected ClientDto connectClient() throws IOException, MlmqException {
		if (client.isConnected()) {
			throw new MlmqException("Client already connected");
		}

		client.init();
		return client.register();
	}

	private void delayClient() {
		int position = config.getMyMapping().getPosition();
		int groupSize = config.getClientDelayGroupSize();
		long delayPerGroupInMin = config.getClientDelayPerGroup();

		if (groupSize <= 0 || delayPerGroupInMin <= 0) {
			// delay feature is disabled
			return;

		}

		// int division, round off, first group index starts at 0
		int groupIndex = position / groupSize;
		if (groupIndex == 0) {
			// we are the first to start
			return;
		}
		long totalDelayMs = groupIndex * delayPerGroupInMin * 60 * 1000;

		logger.info("Delaying Client at Position[" + position + "] by total [" + (groupIndex * delayPerGroupInMin) + "]min start him with Group [" + groupIndex
				+ "]");
		try {
			Thread.sleep(totalDelayMs);
		} catch (InterruptedException e) {
			logger.severe(LoggerUtil.getStackTraceString(e));
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
