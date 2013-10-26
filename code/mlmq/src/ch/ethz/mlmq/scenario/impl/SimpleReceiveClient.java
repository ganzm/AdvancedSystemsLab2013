package ch.ethz.mlmq.scenario.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import ch.ethz.mlmq.client.ClientConfiguration;
import ch.ethz.mlmq.dto.ClientDto;
import ch.ethz.mlmq.dto.MessageDto;
import ch.ethz.mlmq.dto.MessageQueryInfoDto;
import ch.ethz.mlmq.dto.QueueDto;
import ch.ethz.mlmq.logging.LoggerUtil;
import ch.ethz.mlmq.scenario.ClientScenario;

public class SimpleReceiveClient extends ClientScenario {
	private static final Logger logger = Logger.getLogger(SimpleReceiveClient.class.getSimpleName());

	private static final String WAITBETWEENACTION_KEY = "scenario.SimpleReceiveClient.waitTimeBetweenActions";

	private long waitTimeBetweenMessages;

	private volatile boolean running;

	public SimpleReceiveClient(ClientConfiguration config) {
		super(config);

		waitTimeBetweenMessages = config.getLongConfig(WAITBETWEENACTION_KEY);
	}

	@Override
	public void run() throws IOException {
		ClientDto me = client.register();
		QueueDto myQueue = client.lookupClientQueue(me.getId());

		List<QueueDto> queues = new ArrayList<>();
		running = true;
		while (running) {

			queues.clear();
			int messagesForMe = client.queuesWithPendingMessages(queues, 10);
			logger.info("Queues with pending messages " + queues);
			logger.info("My Queue contains " + messagesForMe + " messages");

			if (messagesForMe > 0) {
				MessageQueryInfoDto messageQueryInfo = new MessageQueryInfoDto(myQueue);

				MessageDto msg = client.dequeueMessage(messageQueryInfo);

				logger.info("Dequeued Message " + msg);
			}

			try {
				Thread.sleep(waitTimeBetweenMessages);
			} catch (InterruptedException e) {
				logger.severe(LoggerUtil.getStackTraceString(e));
			}
		}
	}

	@Override
	public void shutdown() {
		super.shutdown();
		running = false;
	}
}
