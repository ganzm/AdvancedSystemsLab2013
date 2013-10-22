package ch.ethz.mlmq.client;

import java.util.Properties;

import ch.ethz.mlmq.common.BrokerScenarioMapping;
import ch.ethz.mlmq.common.ClientScenarioMapping;
import ch.ethz.mlmq.common.Configuration;
import ch.ethz.mlmq.common.ScenarioMapping;

public class ClientConfiguration extends Configuration {

	public static final String CLIENT_NAME = "client.name";
	public static final String RESPONSE_TIMEOUTTIME = "response.timeout";
	public static final String TESTSCENARIO_NR = "testscenario.nr";
	public static final String COMMANDOFILE_PATH = "commandofile.path";
	public static final String COMMANDOFILE_CHECKINTERVALL = "commandofile.checkintervall";
	public static final String PERFORMANCELOGGER_PATH = "performancelogger.logfilepath";

	public static final String SCENARIO_MAPPING = "scenario.mapping";
	public static final String SCENARIO_MYTYPE = "scenario.mytype";
	public static final String SCENARIO_MYPOSITION = "scenario.myposition";

	public ClientConfiguration(Properties props) {
		super(props);
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

	private BrokerScenarioMapping getBroker() {
		ClientScenarioMapping myClientMapping = getClientScenarioMapping();

		BrokerScenarioMapping broker = super.getAssignedBroker(myClientMapping);
		return broker;
	}

	public int getBrokerPort() {
		return getBroker().getPort();
	}

	public String getName() {
		ScenarioMapping mapping = getMyMapping();
		return mapping.getName() + "_" + mapping.getPosition();
	}

	public long getResponseTimeoutTime() {
		return getLongConfig(RESPONSE_TIMEOUTTIME);
	}
}
