package ch.ethz.mlmq.main;

import java.io.IOException;
import java.util.logging.Logger;

import ch.ethz.mlmq.exception.MlmqException;
import ch.ethz.mlmq.server.BrokerConfiguration;
import ch.ethz.mlmq.server.BrokerImpl;
import ch.ethz.mlmq.testrun.TestRunManager;

public class BrokerMain extends RunningJar<BrokerConfiguration> {

	static final Logger logger = Logger.getLogger(BrokerMain.class.getSimpleName());

	private BrokerImpl broker;

	@Override
	protected void doRun() throws MlmqException {
		// TODO make configurable
		TestRunManager testScenarioMgr = new TestRunManager(broker, 1);
		testScenarioMgr.runTest();
	}

	@Override
	protected void doStartup() throws MlmqException {
		broker = new BrokerImpl(getConfig());
		broker.startup();
	}

	@Override
	public boolean processCommand(String command) {
		// TODO: process specific commands (YAGNI??)
		return false;
	}

	protected void executeShutdown() {
		broker.shutdown();
	}

	@Override
	protected BrokerConfiguration initConfig(String configurationFile) throws IOException {
		return new BrokerConfiguration(getProperties(configurationFile));
	}
}
