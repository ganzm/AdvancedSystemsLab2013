package ch.ethz.mlmq.common;

import java.util.HashMap;
import java.util.Map;

import ch.ethz.mlmq.exception.InvalidConfigurationException;

@Deprecated
public class BalancedScenarioConfiguration {

	private final int position;
	private Map<String, String[]> mapping;

	public BalancedScenarioConfiguration(String rawMapping, int position) throws InvalidConfigurationException {
		this.position = position;

		mapping = parseMapping(rawMapping);
	}

	private Map<String, String[]> parseMapping(String rawMapping) throws InvalidConfigurationException {
		String[] groups = rawMapping.split(";");
		Map<String, String[]> mapping = new HashMap<>();
		for (String group : groups) {
			String[] m = group.split(":");
			if (m.length != 2)
				throw new InvalidConfigurationException("Invalid configuration");
			mapping.put(m[0], m[1].split(","));
		}
		return mapping;
	}

	public String getBrokerHost() throws InvalidConfigurationException {
		if (!mapping.containsKey("broker"))
			throw new InvalidConfigurationException("Invalid configuration");
		String[] brokers = mapping.get("broker");
		if (brokers.length == 0)
			throw new InvalidConfigurationException("Invalid configuration");
		return brokers[position % brokers.length];
	}

	public int getBrokerPort() {
		return 8099;
	}

}
