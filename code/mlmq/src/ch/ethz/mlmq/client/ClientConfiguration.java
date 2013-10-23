package ch.ethz.mlmq.client;

import java.util.Properties;

import ch.ethz.mlmq.common.BalancedScenarioConfiguration;
import ch.ethz.mlmq.common.InvalidConfigurationException;
import ch.ethz.mlmq.logging.PerformanceLoggerConfig;
import ch.ethz.mlmq.server.Configuration;

public class ClientConfiguration implements Configuration {

	public static final String CLIENT_NAME = "client.name";
	public static final String RESPONSE_TIMEOUTTIME = "response.timeout";
	public static final String TESTSCENARIO_NR = "testscenario.nr";
	public static final String COMMANDOFILE_PATH = "commandofile.path";
	public static final String COMMANDOFILE_CHECKINTERVALL = "commandofile.checkintervall";
	public static final String PERFORMANCELOGGER_PATH = "performancelogger.logfilepath";

	public static final String SCENARIO_MAPPING = "scenario.mapping";
	public static final String SCENARIO_MYTYPE = "scenario.mytype";
	public static final String SCENARIO_MYPOSITION = "scenario.myposition";

	protected String brokerHost = "localhost";
	protected int brokerPort = 8099;
	protected String name = "DefaultClient";
	protected long responseTimeoutTime = 5000;
	protected String commandoFilePath = "./commando.txt";
	protected long commandoFileCheckIntervall = 5000;

	/**
	 * TODO this is Scenario config
	 */
	protected int numMessages = 100000;

	/**
	 * TODO this is Scenario config
	 */
	protected long waitTimeBetweenMessages = 5;

	/**
	 * Time to wait in ms before reconnecting
	 */
	private long reconnectSleepTime = 5000;

	/**
	 * Scenario Nr this client should be running
	 */
	protected int testScenario = 1;

	/**
	 * Number of connect attempts to make
	 * 
	 * TODO move to configuration
	 */
	protected int numberOfConnectionAtempts = -1;

	protected PerformanceLoggerConfig performanceLoggerConfig = new PerformanceLoggerConfig("log");
	private int totalLikeMe;
	private int myPosition;

	public ClientConfiguration() {

	}

	public ClientConfiguration(Properties props) throws InvalidConfigurationException {
		initFromProps(props);
	}

	private void initFromProps(Properties props) throws InvalidConfigurationException {
		BalancedScenarioConfiguration scenario = new BalancedScenarioConfiguration(props.getProperty(SCENARIO_MAPPING), Integer.parseInt(props
				.getProperty(SCENARIO_MYPOSITION)), props.getProperty(SCENARIO_MYTYPE));
		brokerHost = scenario.getBrokerHost();
		brokerPort = scenario.getBrokerPort();
		totalLikeMe = scenario.getTotalLikeMe();
		myPosition = scenario.getMyPosition();

		name = props.getProperty(SCENARIO_MYTYPE) + ": " + props.getProperty(CLIENT_NAME);
		responseTimeoutTime = Long.parseLong(props.getProperty(RESPONSE_TIMEOUTTIME));
		commandoFilePath = props.getProperty(COMMANDOFILE_PATH);
		commandoFileCheckIntervall = Long.parseLong(props.getProperty(COMMANDOFILE_CHECKINTERVALL));
		performanceLoggerConfig = new PerformanceLoggerConfig(props.getProperty(PERFORMANCELOGGER_PATH));
	}

	public int getTotalLikeMe() {
		return totalLikeMe;
	}

	public int getMyPosition() {
		return myPosition;
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

	public int getNumMessages() {
		return numMessages;
	}

	public long getWaitTimeBetweenMessages() {
		return waitTimeBetweenMessages;
	}

	public long getReconnectSleepTime() {
		return reconnectSleepTime;
	}

	public int getNumberOfConnectionAtempts() {
		return numberOfConnectionAtempts;
	}

}
