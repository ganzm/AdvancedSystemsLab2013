package ch.ethz.mlmq.scenario.impl;

import java.io.IOException;
import java.util.List;
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
 * Requeust/Response client how it is described for the 2h test
 * 
 * Half of the request-response clients act in pairs with one acting as client and the other as server (one makes requests and the other answers them). Use
 * counters and information in the messages exchanged to keep track of what is going on.
 * 
 */
public class PairedClient extends ClientScenario {
	private static final Logger logger = Logger.getLogger(PairedClient.class.getSimpleName());

	private static final String WAITBETWEENACTIONS_KEY = "scenario.PairedClient.waitTimeBetweenActions";

	private final long waitTimeBetweenActions;

	private final Random rnd = new Random();

	private MessageQueryInfoDto receivingMessageQueryInfo;

	private int messageCounter = 0;

	private boolean sending;
	private long context;
	private QueueDto myQueue = null;
	private ClientDto partnerClient = null;

	public PairedClient(ClientConfiguration config) {
		super(config);
		waitTimeBetweenActions = config.getLongConfig(WAITBETWEENACTIONS_KEY);
	}

	@Override
	public void run() throws IOException, MlmqException {
		// connect
		ClientDto clientDto = connectClient();
		myQueue = client.lookupClientQueue(clientDto.getId());
		receivingMessageQueryInfo = new MessageQueryInfoDto(myQueue);

		// pick client with whome i want to exchange messages
		ScenarioMapping partnerClientMapping = getPartnerClient();
		if (partnerClientMapping == null) {
			logger.severe("I dont have a paired client - check configuration to fix this issue");
			return;
		}

		context = 0;

		boolean requesting = config.getMyMapping().getPosition() % 2 == 0;
		sending = requesting;

		// time when we started to send messages
		long startTime = System.nanoTime() / 1000;
		boolean running = true;
		for (int i = 0; running; i++) {
			try {

				// lookup queue of the partner client
				if (partnerClient == null) {
					String clientName = partnerClientMapping.getUniqueName();
					partnerClient = client.lookupClient(clientName);

					if (partnerClient == null) {
						logger.warning("My Communication Partner is not yet online - wait");
						Thread.sleep(2000);
						continue;
					}
				}

				if (requesting) {
					requestingAction();
				} else {
					respondingAction();
				}

				long dt = System.nanoTime() / 1000 - startTime;

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

	private void respondingAction() throws IOException, MlmqException {
		if (sending) {
			logger.fine("Sending response " + messageCounter + " to Context " + context);
			client.sendResponseToClient(partnerClient.getId(), context, ("" + messageCounter).getBytes(), rnd.nextInt(10));
			sending = !sending;
		} else {

			MessageDto msg = client.dequeueMessage(receivingMessageQueryInfo);
			if (msg != null) {
				String msgContent = new String(msg.getContent());
				messageCounter = Integer.parseInt(msgContent) + 1;
				context = msg.getConversationContext();
				logger.info("Received Message " + msgContent + " with conversation Context " + context);

				sending = !sending;
			}
		}
	}

	private void requestingAction() throws IOException, MlmqException {
		if (sending) {
			// requesting, sending
			context = client.sendRequestToClient(partnerClient.getId(), ("" + messageCounter).getBytes(), rnd.nextInt(10));

			sending = !sending;
		} else {
			// requesting, receiving
			long myQueueId = myQueue.getId();
			MessageQueryInfoDto messageQueryInfo = new MessageQueryInfoDto(myQueueId, (int) context);
			MessageDto responseMsg = client.dequeueMessage(messageQueryInfo);

			if (responseMsg != null) {
				String responseContext = new String(responseMsg.getContent());

				int responseCounter = Integer.parseInt(responseContext);

				if (responseCounter != messageCounter + 1) {
					logger.severe("Expected Message to contain " + (messageCounter + 1) + " but got " + responseCounter);
				}

				logger.fine("Got response for my request" + responseContext);

				messageCounter = responseCounter + 1;

				sending = !sending;
			}
		}
	}

	private ScenarioMapping getPartnerClient() {

		ScenarioMapping myMapping = config.getMyMapping();
		List<ClientScenarioMapping> allMappings = config.getAllClientScenarioMapping();
		int myIndex = allMappings.indexOf(myMapping);

		if (myIndex % 2 == 0) {

			if (myIndex + 1 < allMappings.size()) {
				return allMappings.get(myIndex + 1);
			}

		} else {
			if (myIndex - 1 >= 0) {
				return allMappings.get(myIndex - 1);
			}

		}

		return null;
	}
}
