package ch.ethz.mlmq.scenario;

import java.util.logging.Logger;

import ch.ethz.mlmq.scenario.scenarios.SimpleShutdownBroker;

public class ScenarioFinder {

	private final Logger logger = Logger.getLogger(ScenarioFinder.class.getSimpleName());

	public Scenario<?, ?> findScenario(String scenarioName) throws Exception {
		String scenarioPkgName = SimpleShutdownBroker.class.getPackage().getName();
		logger.info("Loading Scenario class " + scenarioPkgName + "." + scenarioName);
		Class<?> c = Class.forName(scenarioPkgName + "." + scenarioName);
		return (Scenario<?, ?>) c.newInstance();
	}
}
