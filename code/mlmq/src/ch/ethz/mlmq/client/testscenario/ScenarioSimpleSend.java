package ch.ethz.mlmq.client.testscenario;

import java.io.IOException;
import java.util.logging.Logger;

import ch.ethz.mlmq.client.Client;
import ch.ethz.mlmq.client.ClientConfiguration;
import ch.ethz.mlmq.dto.QueueDto;
import ch.ethz.mlmq.logging.LoggerUtil;

public class ScenarioSimpleSend {
	private final Logger logger = Logger.getLogger(ScenarioSimpleSend.class.getSimpleName());

	private Client client;
	private ClientConfiguration config;

	public ScenarioSimpleSend(Client client, ClientConfiguration config) {
		this.client = client;
		this.config = config;
	}

	public void run() throws IOException {

		client.register();

		QueueDto queue = client.createQueue("QueueOf" + config.getName());

		for (int i = 0; i < 100000; i++) {
			byte[] content = ("Some Random Text and message Nr " + i).getBytes();
			try {
				client.sendMessage(queue.getId(), content, i % 10);

				Thread.sleep(50);
			} catch (Exception e) {
				logger.warning("Error while sending message " + e + " " + LoggerUtil.getStackTraceString(e));
			}
		}
	}
}
