package ch.ethz.mlmq.log_analyzer;

import java.util.HashMap;
import java.util.Map;

public class ArgUtil {

	private final Map<String, String> argMap;

	private ArgUtil(Map<String, String> argMap) {
		this.argMap = argMap;
	}

	public static ArgUtil parseArgs(String[] args) {
		Map<String, String> argMap = new HashMap<String, String>();

		String keyPrefix = "-";

		// skip <type> argument
		for (int i = 0; i < args.length; i++) {

			String argsI = args[i];
			if (argsI.startsWith(keyPrefix)) {

				String key = argsI.substring(1, argsI.length());
				String value = null;
				if (args.length > i + 1 && !args[i + 1].startsWith(keyPrefix)) {
					value = args[i + 1];
				} else {
					value = "true";
				}

				argMap.put(key, value);
			}
		}
		return new ArgUtil(argMap);
	}

	public Map<String, String> getArgMap() {
		return argMap;
	}

	public String getMandatory(String key) {
		String value = argMap.get(key);
		if (value == null) {
			throw new RuntimeException("ArgumentException Argument[" + key + "] not specified - " + argMap);
		}
		return value;
	}

	public String getOptional(String key, String defaultValue) {
		String value = argMap.get(key);
		if (value == null) {
			return defaultValue;
		}
		return value;
	}

	public boolean hasKey(String key) {
		return argMap.containsKey(key);
	}

}
