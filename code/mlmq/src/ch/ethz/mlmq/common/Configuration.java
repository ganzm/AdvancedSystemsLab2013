package ch.ethz.mlmq.common;

import java.util.List;
import java.util.Properties;

import ch.ethz.mlmq.logging.PerformanceLoggerConfig;

public class Configuration {

	public static final String PERFORMANCELOGGER_PATH = "common.performancelogger.logfilepath";
	public static final String COMMANDOFILE_PATH = "common.commandofile.path";
	public static final String COMMANDOFILE_CHECKINTERVALL = "common.commandofile.checkintervall";

	public static final String COMMANDFILEHANDLER_FILEPATH = "common.commandofile.path";
	public static final String COMMANDFILEHANDLER_CHECKINTERVALL = "common.commandofile.checkintervall";

	public static final String SCENARIO_MYTYPE = "common.scenario.mytype";
	public static final String SCENARIO_MYTYPE_CLIENT_VALUE = "client";
	public static final String SCENARIO_MYTYPE_BROKER_VALUE = "broker";

	protected final PerformanceLoggerConfig performanceLoggerConfig;

	/**
	 * That's where we store all our configuration values
	 */
	protected Properties props;

	private List<BrokerScenarioMapping> brokerScenarioMappings;
	private List<ClientScenarioMapping> clientScenarioMappings;

	private ScenarioMapping myMapping;

	protected Configuration(Properties props) {
		this.props = props;

		performanceLoggerConfig = createPerformanceLogger();
		parseScenarioMapping(props);
	}

	protected PerformanceLoggerConfig createPerformanceLogger() {
		return new PerformanceLoggerConfig(getStringConfig(PERFORMANCELOGGER_PATH));
	}

	protected void parseScenarioMapping(Properties props) {
		throw new RuntimeException("TODO");
		// brokerScenarioMappings = new ArrayList<>();
		// clientScenarioMappings = new ArrayList<>();
		// myMapping = null;
	}

	/**
	 * That's where we assign clients to specific brokers
	 * 
	 * @param clientMapping
	 * @return
	 */
	public BrokerScenarioMapping getAssignedBroker(ClientScenarioMapping clientMapping) {

		// evenly spread clients
		int pos = clientMapping.getPosition();
		return brokerScenarioMappings.get(pos % brokerScenarioMappings.size());
	}

	public int getIntConfig(String configKey) {
		return Integer.parseInt(getStringConfig(configKey));
	}

	public long getLongConfig(String configKey) {
		return Long.parseLong(getStringConfig(configKey));
	}

	public String getStringConfig(String configKey) {
		String value = props.getProperty(configKey);
		if (value == null) {
			throw new RuntimeException("ConfigurationKey [" + configKey + "] not found");
		}
		return value;
	}

	public ScenarioMapping getMyMapping() {
		return myMapping;
	}

	public PerformanceLoggerConfig getPerformanceLoggerConfig() {
		return performanceLoggerConfig;
	}

	public String getCommandFileHandlerPath() {
		return getStringConfig(COMMANDFILEHANDLER_FILEPATH);
	}

	public int getCommandFileHandlerCheckIntervall() {
		return getIntConfig(COMMANDFILEHANDLER_CHECKINTERVALL);
	}
}
