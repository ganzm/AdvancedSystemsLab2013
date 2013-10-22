package ch.ethz.mlmq.common;

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
