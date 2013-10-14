package ch.ethz.mlmq.test;

import java.io.IOException;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import ch.ethz.mlmq.common.InvalidConfigurationException;
import ch.ethz.mlmq.common.ScenarioConfiguration;
import ch.ethz.mlmq.logging.LoggerUtil;

public class ScenarioConfigurationTest {

	@BeforeClass
	public static void beforeClass() throws IOException {
		LoggerUtil.initConsoleDebug();
	}

	@Test
	public void testWithOneBroker() throws InvalidConfigurationException {
		String m = "broker:127.0.0.1;client:127.0.0.1";
		ScenarioConfiguration s1 = new ScenarioConfiguration(m, 0);
		Assert.assertEquals("127.0.0.1", s1.getBrokerHost());
		ScenarioConfiguration s2 = new ScenarioConfiguration(m, 1);
		Assert.assertEquals("127.0.0.1", s2.getBrokerHost());
		ScenarioConfiguration s3 = new ScenarioConfiguration(m, 2);
		Assert.assertEquals("127.0.0.1", s3.getBrokerHost());
		ScenarioConfiguration s4 = new ScenarioConfiguration(m, 3);
		Assert.assertEquals("127.0.0.1", s4.getBrokerHost());
		ScenarioConfiguration s5 = new ScenarioConfiguration(m, 4);
		Assert.assertEquals("127.0.0.1", s5.getBrokerHost());
	}

	@Test
	public void testWithMultipleBrokers() throws InvalidConfigurationException {
		String m = "broker:127.0.0.1,127.0.0.2,127.0.0.3;client:127.0.0.1";
		ScenarioConfiguration s1 = new ScenarioConfiguration(m, 0);
		Assert.assertEquals("127.0.0.1", s1.getBrokerHost());
		ScenarioConfiguration s2 = new ScenarioConfiguration(m, 1);
		Assert.assertEquals("127.0.0.2", s2.getBrokerHost());
		ScenarioConfiguration s3 = new ScenarioConfiguration(m, 2);
		Assert.assertEquals("127.0.0.3", s3.getBrokerHost());
		ScenarioConfiguration s4 = new ScenarioConfiguration(m, 3);
		Assert.assertEquals("127.0.0.1", s4.getBrokerHost());
		ScenarioConfiguration s5 = new ScenarioConfiguration(m, 4);
		Assert.assertEquals("127.0.0.2", s5.getBrokerHost());
	}

	@Test
	public void testWithNoBrokers() {
		String m = "special:127.0.0.1,127.0.0.2,127.0.0.3;client:127.0.0.1";
		try {
			ScenarioConfiguration s1 = new ScenarioConfiguration(m, 0);
			Assert.assertEquals("127.0.0.1", s1.getBrokerHost());
			Assert.fail("Expected InvalidConfigurationException");
		} catch (InvalidConfigurationException e) {
			Assert.assertTrue(true);
		}
	}

	@Test
	public void testBrokerPort() throws InvalidConfigurationException {
		String m = "special:127.0.0.1,127.0.0.2,127.0.0.3;client:127.0.0.1";
		ScenarioConfiguration s = new ScenarioConfiguration(m, 0);
		Assert.assertEquals(8099, s.getBrokerPort());
	}
}
