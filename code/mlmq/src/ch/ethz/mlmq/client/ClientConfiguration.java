package ch.ethz.mlmq.client;

import java.util.Properties;

import ch.ethz.mlmq.common.BrokerScenarioMapping;
import ch.ethz.mlmq.common.ClientScenarioMapping;
import ch.ethz.mlmq.common.Configuration;
import ch.ethz.mlmq.common.ScenarioMapping;

public class ClientConfiguration extends Configuration {

	public static final String CLIENT_NAME = "client.name";
	public static final String RESPONSE_TIMEOUTTIME = "client.response.timeout";
	public static final String COMMANDOFILE_PATH = "commandofile.path";
	public static final String COMMANDOFILE_CHECKINTERVALL = "commandofile.checkintervall";
	public static final String PERFORMANCELOGGER_PATH = "performancelogger.logfilepath";

	public static final String CLIENT_RECONNECT_SLEEPTIME = "client.reconnect.sleeptime";
	public static final String CLIENT_RECONNECT_COUNT = "client.reconnect.count";

	public static final String SCENARIO_MAPPING = "scenario.mapping";
	public static final String SCENARIO_MYTYPE = "scenario.mytype";
	public static final String SCENARIO_MYPOSITION = "scenario.myposition";

	private static final String CLIENT_DELAY_GROUPSIZE = "client.delay.groupsize";
	private static final String CLIENT_DELAY_PERGROUP = "client.delay.delaypergroup";

	public ClientConfiguration(Properties props) {
		super(props);
	}

	@Override
	public String toString() {
		ClientScenarioMapping me = getClientScenarioMapping();
		BrokerScenarioMapping broker = getBroker();

		StringBuilder sb = new StringBuilder();

		sb.append(getClass().getSimpleName());
		sb.append(" ClientName[");
		sb.append(me.getScenarioClassName());
		sb.append("] ClientHost[");
		sb.append(me.getHost());
		sb.append("] ClientPosition[");
		sb.append(me.getPosition());
		sb.append("] Broker[");
		sb.append(broker.getHost());
		sb.append(":");
		sb.append(broker.getPort());
		sb.append("] BrokerScenario[");
		sb.append(broker.getScenarioClassName());
		sb.append("]");

		return sb.toString();
	}

	private ClientScenarioMapping getClientScenarioMapping() {
		ScenarioMapping mapping = getMyMapping();
		if (mapping instanceof ClientScenarioMapping) {
			return (ClientScenarioMapping) mapping;
		} else {
			throw new RuntimeException("Invalid Configuration");
		}
	}

	public String getBrokerHost() {
		return getBroker().getHost();
	}

	/**
	 * @return The Broker this Client is assigned to
	 */
	private BrokerScenarioMapping getBroker() {
		ClientScenarioMapping myClientMapping = getClientScenarioMapping();
		return super.getAssignedBroker(myClientMapping);
	}

	public int getBrokerPort() {
		return getBroker().getPort();
	}

	public String getName() {
		ScenarioMapping mapping = getMyMapping();
		return mapping.getUniqueName();
	}

	public long getResponseTimeoutTime() {
		return getLongConfig(RESPONSE_TIMEOUTTIME);
	}

	public long getReconnectSleepTime() {
		return getLongConfig(CLIENT_RECONNECT_SLEEPTIME);
	}

	public int getNumberOfConnectionAtempts() {
		return getIntConfig(CLIENT_RECONNECT_COUNT);
	}

	public int getClientDelayGroupSize() {
		return getIntConfig(CLIENT_DELAY_GROUPSIZE);
	}

	public long getClientDelayPerGroup() {
		return getIntConfig(CLIENT_DELAY_PERGROUP);
	}
}
