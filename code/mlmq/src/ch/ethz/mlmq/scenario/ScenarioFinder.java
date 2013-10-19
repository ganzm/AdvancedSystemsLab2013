package ch.ethz.mlmq.scenario;

import java.util.logging.Logger;

@Deprecated
public class ScenarioFinder {
	private static final Logger logger = Logger.getLogger(ScenarioFinder.class.getSimpleName());

	public static Scenario findScenario(String scenarioName) throws Exception {
		String packageName = "ch.ethz.mlmq.scenario";
		String fullName = packageName + "." + scenarioName;
		logger.info("Find scenario " + fullName);
		Class<?> c = Class.forName(fullName);
		return (Scenario) c.newInstance();
	}
}
