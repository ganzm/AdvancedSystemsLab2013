package ch.ethz.mlmq.testrun;

import java.io.IOException;

import ch.ethz.mlmq.client.Client;
import ch.ethz.mlmq.client.ClientConfiguration;
import ch.ethz.mlmq.exception.MlmqException;
import ch.ethz.mlmq.server.Broker;

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
	private int testRunId;

	public TestRunManager(Client client, ClientConfiguration config) {
		this.client = client;
		this.config = config;

		// TODO wait for Config refactoring
		this.testRunId = config.getTestScenario();
	}

	public TestRunManager(Broker broker, int testRunId) {
		this.broker = broker;
		this.testRunId = testRunId;
	}

	public void runTest() throws MlmqException {
		try {
			if (testRunId == 1) {
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

		// TODO configurable
		// this configures how long this test is running
		long shutdownDelay = 1 * 60 * 1000;
		// TODO configurable
		int numMessages = 100000;
		// TODO configurable
		long waitTimeBetweenMessages = 5;

		if (client != null) {
			TestRunSimpleSend clientSendTest = new TestRunSimpleSend(client, config, numMessages, waitTimeBetweenMessages);
			clientSendTest.run();
		} else {
			BrokerTestRunSimpleShutdown brokerTestRun = new BrokerTestRunSimpleShutdown(broker, shutdownDelay);
			brokerTestRun.run();
		}

	}
}
