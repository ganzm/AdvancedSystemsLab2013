package ch.ethz.mlmq.scenario.scenarios;

import java.io.IOException;
import java.util.logging.Logger;

import ch.ethz.mlmq.client.Client;
import ch.ethz.mlmq.client.ClientConfiguration;
import ch.ethz.mlmq.dto.QueueDto;
import ch.ethz.mlmq.logging.LoggerUtil;
import ch.ethz.mlmq.scenario.ClientScenario;

public class SimpleSendClient extends ClientScenario {
	private final Logger logger = Logger.getLogger(SimpleSendClient.class.getSimpleName());

	@Override
	public void run(Client client, ClientConfiguration config) throws IOException {
		int numMessages = config.getNumMessages();
		long waitTimeBetweenMessages = config.getWaitTimeBetweenMessages();

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
