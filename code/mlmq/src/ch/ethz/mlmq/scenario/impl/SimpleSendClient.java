package ch.ethz.mlmq.scenario.impl;

import java.io.IOException;
import java.util.logging.Logger;

import ch.ethz.mlmq.client.ClientConfiguration;
import ch.ethz.mlmq.dto.QueueDto;
import ch.ethz.mlmq.exception.MlmqException;
import ch.ethz.mlmq.exception.MlmqRequestTimeoutException;
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
		connectClient();

		String queueName = "QueueOf" + config.getName();
		QueueDto queue = getOrCreateQueue(queueName);

		// time when we started to send messages
		long startTime = System.currentTimeMillis();
		for (int i = 0; i < numMessages; i++) {
			try {
				sendSimpleMessage(queue, i);

				long dt = System.currentTimeMillis() - startTime;
				if (dt / waitTimeBetweenMessages <= i) {
					long timeToSleep = waitTimeBetweenMessages - (dt % waitTimeBetweenMessages);
					Thread.sleep(timeToSleep);
					// else { We are behind in sending messages - don't sleep }
				}

			} catch (MlmqRequestTimeoutException e) {
				logger.severe("MlmqRequestTimeoutException - try to reconnect " + LoggerUtil.getStackTraceString(e));
				connectClient();
			} catch (MlmqException e) {
				logger.severe("MlmQEception while sending message - try again - " + e + " " + LoggerUtil.getStackTraceString(e));
			} catch (Exception e) {
				logger.severe("Error while sending message - shutdown " + e + " " + LoggerUtil.getStackTraceString(e));
				break;
			}
		}
	}

	private void sendSimpleMessage(QueueDto queue, int i) throws IOException, MlmqException {
		byte[] content = ("Some Random Text and message Nr " + i).getBytes();
		client.sendMessage(queue.getId(), content, i % 10);
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
