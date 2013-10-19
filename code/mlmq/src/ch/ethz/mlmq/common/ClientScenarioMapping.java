package ch.ethz.mlmq.common;

/**
 * This class maps an entry from the configuration file
 * 
 * @see Config file Key "common.scenario.mapping.client"
 */
public class ClientScenarioMapping extends ScenarioMapping {

	public ClientScenarioMapping(String name, String host, int position) {
		super(name, host, position);
	}
}
