package ch.ethz.mlmq.scenario.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.logging.Logger;

import ch.ethz.mlmq.client.ClientConfiguration;
import ch.ethz.mlmq.common.ClientScenarioMapping;
import ch.ethz.mlmq.common.ScenarioMapping;
import ch.ethz.mlmq.dto.ClientDto;
import ch.ethz.mlmq.dto.MessageDto;
import ch.ethz.mlmq.dto.MessageQueryInfoDto;
import ch.ethz.mlmq.dto.QueueDto;
import ch.ethz.mlmq.exception.MlmqException;
import ch.ethz.mlmq.exception.MlmqRequestTimeoutException;
import ch.ethz.mlmq.logging.LoggerUtil;
import ch.ethz.mlmq.scenario.ClientScenario;

/**
 * One way client how it is described for the 2h test
 * 
 * The one-way clients send a message at random to one of the other clients and wait for a message for them. Upon receiving one message, they mark it and send
 * it again to another random client. Initially the messages can be a counter set to 0 that is incremented in each iteration. That way the final messages
 * contain how many times they have been exchanged. The message could also contain the identifiers of the clients involved to trace back all the exchanges.
 */
public class OneWayClient extends ClientScenario {
	private static final Logger logger = Logger.getLogger(OneWayClient.class.getSimpleName());

	private static final String WAITBETWEENACTIONS_KEY = "scenario.OneWayClient.waitTimeBetweenActions";

	private final long waitTimeBetweenActions;

	private final Random rnd = new Random();

	private final Map<String, Long> clientNameToQueueMap = new HashMap<>();

	private MessageQueryInfoDto myQueueQueryInfo;;

	/**
	 * List contains all the clients we potentially send messages to
	 */
	private final List<ClientScenarioMapping> oneWayClientList = new ArrayList<>();

	public OneWayClient(ClientConfiguration config) {
		super(config);
		waitTimeBetweenActions = config.getLongConfig(WAITBETWEENACTIONS_KEY);
	}

	@Override
	public void run() throws IOException, MlmqException {

		ClientDto clientDto = connectClient();

		QueueDto myQueue = client.lookupClientQueue(clientDto.getId());
		myQueueQueryInfo = new MessageQueryInfoDto(myQueue);

		prepareClientList();

		sendInitialMessage();

		// time when we started to send messages
		long startTime = System.nanoTime();
		boolean running = true;
		for (int i = 0; running; i++) {
			try {

				performAction();

				long dt = System.nanoTime() - startTime;

				if (dt / waitTimeBetweenActions <= i) {
					long timeToSleep = waitTimeBetweenActions - (dt % waitTimeBetweenActions);
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

	private void prepareClientList() {
		String myName = OneWayClient.class.getSimpleName();
		List<ClientScenarioMapping> clientMappings = config.getAllClientScenarioMapping();
		ScenarioMapping myMapping = config.getMyMapping();
		for (ClientScenarioMapping mapping : clientMappings) {

			String className = mapping.getScenarioClassName();

			if (myName.equals(className) && myMapping != mapping) {
				oneWayClientList.add(mapping);
			}
		}
	}

	private void performAction() throws IOException, MlmqException {

		MessageDto msgDto = client.dequeueMessage(myQueueQueryInfo);

		if (msgDto != null) {
			String contentString = new String(msgDto.getContent());
			logger.fine("Message received " + contentString);

			int msgCount = Integer.parseInt(contentString);

			long receipient = pickRandomRecipientQueueId();
			client.sendMessage(receipient, ((msgCount + 1) + "").getBytes(), rnd.nextInt(10));
		}

	}

	private void sendInitialMessage() throws IOException, MlmqException {
		long queueId = pickRandomRecipientQueueId();
		client.sendMessage(queueId, "0".getBytes(), rnd.nextInt(10));
	}

	private long pickRandomRecipientQueueId() throws IOException, MlmqException {

		Long queueId = null;

		while (queueId == null) {
			int rndReceiverIndex = rnd.nextInt(oneWayClientList.size());
			ClientScenarioMapping rndReceiver = oneWayClientList.get(rndReceiverIndex);

			String clientName = rndReceiver.getUniqueName();

			queueId = clientNameToQueueMap.get(clientName);

			if (queueId == null) {
				logger.fine("Lookup Client queue of " + clientName);
				QueueDto queue = client.lookupClientQueue(clientName);
				if (queue != null) {
					queueId = queue.getId();
					clientNameToQueueMap.put(clientName, queueId);
				}
			}

			if (queueId == null) {
				// client may be not yet online
				logger.warning("Tried to send Message to client " + clientName + " - queue not found (this is ok if we just started up)");
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					logger.severe(LoggerUtil.getStackTraceString(e));
				}
			}
		}
		return queueId;
	}

}
