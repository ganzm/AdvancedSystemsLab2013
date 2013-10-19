package ch.ethz.mlmq.main;

import java.io.IOException;
import java.net.ConnectException;
import java.util.logging.Logger;

import ch.ethz.mlmq.client.Client;
import ch.ethz.mlmq.client.ClientConfiguration;
import ch.ethz.mlmq.client.ClientImpl;
import ch.ethz.mlmq.common.InvalidConfigurationException;
import ch.ethz.mlmq.exception.MlmqException;
import ch.ethz.mlmq.testrun.TestRunManager;

public class ClientMain extends RunningJar<ClientConfiguration> {

	private static final Logger logger = Logger.getLogger(ClientMain.class.getSimpleName());

	private Client client;
	private TestRunManager testScenarioMgr;

	protected ClientConfiguration initConfig(String configurationFile) throws InvalidConfigurationException, IOException {
		return new ClientConfiguration(getProperties(configurationFile));
	}

	protected void doStartup() throws IOException {
		client = new ClientImpl(getConfig());
	}

	protected void doRun() throws IOException, InterruptedException, MlmqException {
		ClientConfiguration config = getConfig();
		for (int i = 0; !client.isConnected() && (i < config.getNumberOfConnectionAtempts() || config.getNumberOfConnectionAtempts() == -1); i++) {
			try {
				logger.info("Client init");
				client.init();
			} catch (ConnectException ex) {
				logger.warning("Could not connect to Broker " + config.getBrokerHost() + ":" + config.getBrokerPort());
				Thread.sleep(config.getReconnectSleepTime());
			}
		}
		logger.info("Client started");

		testScenarioMgr = new TestRunManager(client, config);
		testScenarioMgr.runTest();
	}

	@Override
	public boolean processCommand(String command) {
		// TODO: process specific commands (YAGNI??)
		return false;
	}

	protected void executeShutdown() throws IOException {
		client.close();
	}
}
