package ch.ethz.mlmq.scenario.impl;

import java.io.IOException;
import java.util.logging.Logger;

import ch.ethz.mlmq.client.ClientConfiguration;
import ch.ethz.mlmq.dto.QueueDto;
import ch.ethz.mlmq.exception.MlmqException;
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
	public void run() throws IOException, MlmqException {
		client.register();

		String queueName = "QueueOf" + config.getName();
		QueueDto queue = getOrCreateQueue(queueName);

		// time when we started to send messages
		long startTime = System.currentTimeMillis();
		for (int i = 0; i < numMessages; i++) {
			byte[] content = ("Some Random Text and message Nr " + i).getBytes();
			try {
				client.sendMessage(queue.getId(), content, i % 10);

				long dt = System.currentTimeMillis() - startTime;

				if (dt / waitTimeBetweenMessages <= i) {
					long timeToSleep = waitTimeBetweenMessages - (dt % waitTimeBetweenMessages);
					Thread.sleep(timeToSleep);
					// else { We are behind in sending messages - don't sleep }
				}
			} catch (MlmqException e) {
				logger.severe("MlmQEception while sending message - try again - " + e + " " + LoggerUtil.getStackTraceString(e));
			} catch (IOException e) {
				logger.severe("IOEception while sending message - shutdown " + e + " " + LoggerUtil.getStackTraceString(e));

				// stop sending messages in case of ioexception
				i = numMessages;
			} catch (Exception e) {
				logger.warning("Error while sending message " + e + " " + LoggerUtil.getStackTraceString(e));
			}
		}
	}

	private QueueDto getOrCreateQueue(String queueName) throws IOException, MlmqException {
		try {
			QueueDto queue = client.lookupClientQueue(queueName);
			if (queue == null) {
				return client.createQueue(queueName);
			} else {
				return queue;
			}
		} catch (Exception e) {
			logger.info("Queue " + queueName + " already exists try to lookup queue");
			return client.lookupClientQueue(queueName);
		}
	}
}
