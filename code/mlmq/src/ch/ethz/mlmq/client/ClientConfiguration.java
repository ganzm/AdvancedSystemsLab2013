package ch.ethz.mlmq.client;

import java.util.Properties;

import ch.ethz.mlmq.common.InvalidConfigurationException;
import ch.ethz.mlmq.common.BalancedScenarioConfiguration;
import ch.ethz.mlmq.logging.PerformanceLoggerConfig;

public class ClientConfiguration {

	public static final String CLIENT_NAME = "client.name";
	public static final String RESPONSE_TIMEOUTTIME = "response.timeout";
	public static final String TESTSCENARIO_NR = "testscenario.nr";
	public static final String COMMANDOFILE_PATH = "commandofile.path";
	public static final String COMMANDOFILE_CHECKINTERVALL = "commandofile.checkintervall";
	public static final String PERFORMANCELOGGER_PATH = "performancelogger.logfilepath";

	public static final String SCENARIO_MAPPING = "scenario.mapping";
	public static final String SCENARIO_MYTYPE = "scenario.mytype";
	public static final String SCENARIO_MYPOSITION = "scenario.myposition";

	public static final String TESTRUN_ID = "testrun.id";

	protected String brokerHost = "localhost";
	protected int brokerPort = 8099;
	protected String name = "DefaultClient";
	protected long responseTimeoutTime = 5000;
	protected String commandoFilePath = "./commando.txt";
	protected long commandoFileCheckIntervall = 5000;
	/**
	 * Scenario Nr this client should be running
	 */
	protected int testScenario = 1;

	protected PerformanceLoggerConfig performanceLoggerConfig = new PerformanceLoggerConfig("log");

	public ClientConfiguration() {

	}

	public ClientConfiguration(Properties props) throws InvalidConfigurationException {
		initFromProps(props);
	}

	private void initFromProps(Properties props) throws InvalidConfigurationException {
		BalancedScenarioConfiguration scenario = new BalancedScenarioConfiguration(props.getProperty(SCENARIO_MAPPING),
				Integer.parseInt(props.getProperty(SCENARIO_MYPOSITION)));
		brokerHost = scenario.getBrokerHost();
		brokerPort = scenario.getBrokerPort();
		name = props.getProperty(SCENARIO_MYTYPE) + ": " + props.getProperty(CLIENT_NAME);
		responseTimeoutTime = Long.parseLong(props.getProperty(RESPONSE_TIMEOUTTIME));
		commandoFilePath = props.getProperty(COMMANDOFILE_PATH);
		commandoFileCheckIntervall = Long.parseLong(props.getProperty(COMMANDOFILE_CHECKINTERVALL));
		performanceLoggerConfig = new PerformanceLoggerConfig(props.getProperty(PERFORMANCELOGGER_PATH));
	}

	public String getBrokerHost() {
		return brokerHost;
	}

	public int getBrokerPort() {
		return brokerPort;
	}

	public String getName() {
		return name;
	}

	public long getResponseTimeoutTime() {
		return responseTimeoutTime;
	}

	public int getTestScenario() {
		return testScenario;
	}

	public PerformanceLoggerConfig getPerformanceLoggerConfig() {
		return performanceLoggerConfig;
	}

	public String getCommandoFilePath() {
		return commandoFilePath;
	}

	public long getCommandFileCheckIntervall() {
		return commandoFileCheckIntervall;
	}
}
