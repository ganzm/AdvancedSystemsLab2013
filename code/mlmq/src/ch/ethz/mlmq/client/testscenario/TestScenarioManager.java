package ch.ethz.mlmq.client.testscenario;

import ch.ethz.mlmq.client.Client;
import ch.ethz.mlmq.client.ClientConfiguration;
import ch.ethz.mlmq.exception.MlmqException;

public class TestScenarioManager {

	private final Client client;

	public TestScenarioManager(Client client) {
		this.client = client;
	}

	public void startTest(ClientConfiguration config) throws MlmqException {
		try {

			if (config.getTestScenario() == 1) {

				ScenarioSimpleSend scenario = new ScenarioSimpleSend(client, config);
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
