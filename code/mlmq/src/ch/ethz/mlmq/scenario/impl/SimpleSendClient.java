package ch.ethz.mlmq.testrun;

import java.io.IOException;
import java.util.logging.Logger;

import ch.ethz.mlmq.client.Client;
import ch.ethz.mlmq.client.ClientConfiguration;
import ch.ethz.mlmq.dto.QueueDto;
import ch.ethz.mlmq.logging.LoggerUtil;

public class TestRunSimpleSend extends ClientTestRun {
	private final Logger logger = Logger.getLogger(TestRunSimpleSend.class.getSimpleName());

	private ClientConfiguration config;

	private final int numMessages;

	private final long waitTimeBetweenMessages;

	public TestRunSimpleSend(Client client, ClientConfiguration config) {
		super(client);
		this.config = config;
		this.numMessages = config.getNumMessages();
		this.waitTimeBetweenMessages = config.getWaitTimeBetweenMessages();
	}

	@Override
	public void run(Client client) throws IOException {
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
