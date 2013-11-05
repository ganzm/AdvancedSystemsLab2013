package ch.ethz.mlmq.scenario.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

import ch.ethz.mlmq.client.ClientConfiguration;
import ch.ethz.mlmq.dto.MessageDto;
import ch.ethz.mlmq.dto.MessageQueryInfoDto;
import ch.ethz.mlmq.dto.QueueDto;
import ch.ethz.mlmq.exception.MlmqException;
import ch.ethz.mlmq.exception.MlmqRequestTimeoutException;
import ch.ethz.mlmq.logging.LoggerUtil;
import ch.ethz.mlmq.scenario.ClientScenario;

public class SendReceiveClient extends ClientScenario {
	private static final Logger logger = Logger.getLogger(SendReceiveClient.class.getSimpleName());

	private static final String NUMMESSAGE_KEY = "scenario.SimpleSendClient.numMessages";
	private static final String WAITBETWEENMESSAGES_KEY = "scenario.SimpleSendClient.waitTimeBetweenMessages";

	private final int numMessages;

	private long waitTimeBetweenMessages;

	/**
	 * Queue of this client
	 */
	private QueueDto queue;

	private Random rnd = new Random(System.currentTimeMillis());

	private final List<QueueDto> queues = new ArrayList<>();

	private List<QueueDto> publicQueues;

	private final int totalNrOfClients;

	private int receivedMessages;

	private int sentMessages;

	public SendReceiveClient(ClientConfiguration config) {
		super(config);

		numMessages = config.getIntConfig(NUMMESSAGE_KEY);
		waitTimeBetweenMessages = config.getLongConfig(WAITBETWEENMESSAGES_KEY);
		totalNrOfClients = config.getAllClientScenarioMapping().size();
	}

	@Override
	public void run() throws IOException, MlmqException {
		boolean initialized = false;

		connectClient();

		String queueName = "QueueOf" + config.getName();

		// time when we started to send messages
		long startTime = System.currentTimeMillis();
		for (int i = 0; i < numMessages; i++) {
			try {

				if (!initialized) {
					queue = client.createQueue(queueName);
					publicQueues = createPublicQueues();
					initialized = true;
				}

				performAction();

				long dt = System.currentTimeMillis() - startTime;

				if (dt / waitTimeBetweenMessages <= i) {
					long timeToSleep = waitTimeBetweenMessages - (dt % waitTimeBetweenMessages);
					Thread.sleep(timeToSleep);
					// else { We are behind in sending messages - don't sleep }
				}
			} catch (MlmqRequestTimeoutException e) {
				logger.severe("MlmqRequestTimeoutException - try to reconnect " + LoggerUtil.getStackTraceString(e));
				connectClient();
			} catch (Exception e) {
				logger.severe("Error while sending message " + e + " " + LoggerUtil.getStackTraceString(e));
				break;
			}
		}
	}

	private List<QueueDto> createPublicQueues() throws IOException, MlmqException {
		List<QueueDto> result = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			result.add(client.createQueue("Public-" + i));
		}
		return result;
	}

	private void performAction() throws IOException, MlmqException {

		if (rnd.nextBoolean()) {
			// send a message

			// yes, we are guessing clientId's.... omg
			int clientId = rnd.nextInt(totalNrOfClients);

			QueueDto receivingClientQueue = client.lookupClientQueue(clientId);

			if (receivingClientQueue != null && rnd.nextBoolean()) {
				// send personal message
				logger.fine("Sending personal Message to Client " + clientId);
				client.sendMessage(receivingClientQueue.getId(), createRandomMessage(), rnd.nextInt(10));
				sentMessages++;

			} else {
				// send to a random public queue
				int queueIndex = rnd.nextInt(publicQueues.size());
				logger.fine("Sending public Message to Queue " + queueIndex);
				client.sendMessage(publicQueues.get(queueIndex).getId(), createRandomMessage(), rnd.nextInt(10));
				sentMessages++;
			}

		} else {
			// receive a message
			queues.clear();
			int messagesForMe = client.queuesWithPendingMessages(queues, 10);

			MessageQueryInfoDto msgQueryInfo = null;
			if (messagesForMe > 0 && rnd.nextBoolean()) {
				// receive personal message
				msgQueryInfo = new MessageQueryInfoDto(queue);

			} else if (queues.size() > 0) {
				msgQueryInfo = new MessageQueryInfoDto(queues.get(0));
			}

			if (msgQueryInfo != null) {
				MessageDto msg = client.dequeueMessage(msgQueryInfo);
				if (msg != null) {
					logger.fine("Receive Message " + new String(msg.getContent()));
					receivedMessages++;
				}
			}
		}
	}

	private byte[] createRandomMessage() {
		return ("Hi there I am a client with SentMessages[" + sentMessages + "] ReceivedMessages[" + receivedMessages + "] sent at " + new Date()).getBytes();
	}

}
