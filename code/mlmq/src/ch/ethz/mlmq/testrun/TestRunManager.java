package ch.ethz.mlmq.testrun;

import java.io.IOException;

import ch.ethz.mlmq.client.Client;
import ch.ethz.mlmq.client.ClientConfiguration;
import ch.ethz.mlmq.exception.MlmqException;
import ch.ethz.mlmq.server.Broker;
import ch.ethz.mlmq.testrun.instances.BrokerTestRunSimpleShutdown;
import ch.ethz.mlmq.testrun.instances.TestRunSimpleSend;

public class TestRunManager {

	/**
	 * Either client or broker is null
	 */
	private Client client = null;
	/**
	 * Either client or broker is null
	 */
	private Broker broker = null;
	private ClientConfiguration config;
	private int testScenarioName;

	public TestRunManager(Client client, ClientConfiguration config) {
		this.client = client;
		this.config = config;

		// TODO wait for Config refactoring
		this.testScenarioName = config.getTestScenario();
	}

	public TestRunManager(Broker broker, int testRunId) {
		this.broker = broker;
		this.testScenarioName = testRunId;
	}

	public void runTest() throws MlmqException {
		try {
			if (testScenarioName == 1) {
				runTestRun1(config);
			} else {
				throw new MlmqException("Unknown TestScenario " + config.getTestScenario());
			}
		} catch (MlmqException e) {
			throw e;
		} catch (Exception e) {
			throw new MlmqException(e);
		}
	}

	private void runTestRun1(ClientConfiguration config) throws IOException {
		if (client != null) {
			TestRunSimpleSend clientSendTest = new TestRunSimpleSend(client, config);
			clientSendTest.run();
		} else {
			// TODO configurable
			// this configures how long this test is running
			long shutdownDelay = 1 * 60 * 1000;

			BrokerTestRunSimpleShutdown brokerTestRun = new BrokerTestRunSimpleShutdown(broker, shutdownDelay);
			brokerTestRun.run();
		}
	}
}
