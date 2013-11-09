package ch.ethz.mlmq.common;

/**
 * represents what's written in the configuration at
 * 
 * common.scenario.mapping.broker common.scenario.mapping.client
 * 
 * e.g.
 * 
 * common.scenario.mapping.broker = SimpleShutdownBroker#127.0.0.1:8099;SimpleShutdownBroker#127.0.0.1:8100,SimpleShutdownBroker#127.0.0.1:8101
 */
public class ScenarioMapping {

	private String name;
	private String host;

	/**
	 * Position index in the configuration file
	 */
	private int position;

	public ScenarioMapping(String name, String host, int position) {
		this.name = name;
		this.host = host;
		this.position = position;
	}

	public String getScenarioClassName() {
		return name;
	}

	public String getUniqueName() {
		return name + "_" + position;
	}

	public String getHost() {
		return host;
	}

	public int getPosition() {
		return position;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + " Name[" + name + "] Host[" + host + "] Position[" + position + "]";
	}
}
