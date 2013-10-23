package ch.ethz.mlmq.test.testrun;

import org.junit.Assert;
import org.junit.Test;

import ch.ethz.mlmq.scenario.Scenario;
import ch.ethz.mlmq.scenario.ScenarioFinder;
import ch.ethz.mlmq.scenario.scenarios.SimpleSendClient;

public class ScenarioFinderTest {
	@Test
	public void testFindScenarioByName() throws Exception {
		ScenarioFinder f = new ScenarioFinder();
		Scenario<?, ?> t = f.findScenario("SimpleSendClient");
		Assert.assertTrue(t instanceof SimpleSendClient);
	}
}
