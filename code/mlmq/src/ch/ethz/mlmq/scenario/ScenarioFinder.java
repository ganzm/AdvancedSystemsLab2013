package ch.ethz.mlmq.scenario;

import ch.ethz.mlmq.scenario.scenarios.SimpleShutdownBroker;

public class ScenarioFinder {

	public Scenario<?, ?> findScenario(String scenarioName) throws Exception {
		String s = SimpleShutdownBroker.class.getPackage().getName();
		System.out.println(s + "." + scenarioName);
		Class<?> c = Class.forName(s + "." + scenarioName);
		return (Scenario<?, ?>) c.newInstance();
	}
}
