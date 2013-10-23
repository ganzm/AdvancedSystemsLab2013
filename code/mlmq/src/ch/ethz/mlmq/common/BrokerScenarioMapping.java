package ch.ethz.mlmq.common;

/**
 * This class maps an entry from the configuration file
 * 
 * @see Config file Key "common.scenario.mapping.broker"
 */
public class BrokerScenarioMapping extends ScenarioMapping {

	private int port;

	public BrokerScenarioMapping(String name, String host, int port, int position) {
		super(name, host, position);
		this.port = port;
	}

	public int getPort() {
		return port;
	}
}
