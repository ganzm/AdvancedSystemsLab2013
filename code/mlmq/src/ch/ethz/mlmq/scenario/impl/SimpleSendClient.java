package ch.ethz.mlmq.scenario.impl;

import java.io.IOException;
import java.util.logging.Logger;

import ch.ethz.mlmq.client.ClientConfiguration;
import ch.ethz.mlmq.dto.QueueDto;
import ch.ethz.mlmq.logging.LoggerUtil;
import ch.ethz.mlmq.scenario.ClientScenario;

public class SimpleSendClient extends ClientScenario {
	private static final Logger logger = Logger.getLogger(SimpleSendClient.class.getSimpleName());

	private static final String NUMMESSAGE_KEY = "scenario.SimpleSendClient.numMessages";
	private static final String WAITBETWEENMESSAGES_KEY = "scenario.SimpleSendClient.waitTimeBetweenMessages";

	private final int numMessages;

	private long waitTimeBetweenMessages;

	public SimpleSendClient(ClientConfiguration config) {
		super(config);

		numMessages = config.getIntConfig(NUMMESSAGE_KEY);
		waitTimeBetweenMessages = config.getLongConfig(WAITBETWEENMESSAGES_KEY);
	}

	@Override
	public void run() throws IOException {
		client.register();
		QueueDto queue = client.createQueue("QueueOf" + config.getName());

		for (int i = 0; i < numMessages; i++) {
			byte[] content = ("Some Random Text and message Nr " + i).getBytes();
			try {
				client.sendMessage(queue.getId(), content, i % 10);

				Thread.sleep(waitTimeBetweenMessages);

			} catch (IOException e) {
				logger.severe("IOEception while sending message - shutdown " + e + " " + LoggerUtil.getStackTraceString(e));

				// stop sending messages in case of ioexception
				i = numMessages;
			} catch (Exception e) {
				logger.warning("Error while sending message " + e + " " + LoggerUtil.getStackTraceString(e));
			}
		}
	}
}