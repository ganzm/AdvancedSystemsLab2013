package ch.ethz.mlmq.testrun;

import ch.ethz.mlmq.client.Client;
import ch.ethz.mlmq.client.ClientConfiguration;
import ch.ethz.mlmq.exception.MlmqException;

public class TestRunManager {

	private final Client client;

	public TestRunManager(Client client) {
		this.client = client;
	}

	public void startTest(ClientConfiguration config) throws MlmqException {
		try {

			if (config.getTestScenario() == 1) {

				TestRunSimpleSend scenario = new TestRunSimpleSend(client, config);
				scenario.run();

			} else {
				throw new MlmqException("Unknown TestScenario " + config.getTestScenario());
			}
		} catch (MlmqException e) {
			throw e;
		} catch (Exception e) {
			throw new MlmqException(e);
		}
	}
}
