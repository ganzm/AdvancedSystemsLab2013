package ch.ethz.mlmq.client;

import java.util.Properties;

import ch.ethz.mlmq.logging.PerformanceLoggerConfig;

public class ClientConfiguration {

	public static final String BROKER_HOST = "broker.host";
	public static final String BROKER_PORT = "broker.port";
	public static final String CLIENT_NAME = "client.name";
	public static final String RESPONSE_TIMEOUTTIME = "response.timeout";
	public static final String TESTSCENARIO_NR = "testscenario.nr";
	public static final String COMMANDOFILE_PATH = "commandofile.path";
	public static final String COMMANDOFILE_CHECKINTERVALL = "commandofile.checkintervall";
	public static final String PERFORMANCELOGGER_PATH = "performancelogger.logfilepath";

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

	public ClientConfiguration(Properties props) {
		initFromProps(props);
	}

	private void initFromProps(Properties props) {
		brokerHost = props.getProperty(BROKER_HOST);
		brokerPort = Integer.parseInt(props.getProperty(BROKER_PORT));
		name = props.getProperty(CLIENT_NAME);
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
