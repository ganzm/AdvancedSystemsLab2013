package ch.ethz.mlmq.test;

import java.io.IOException;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import ch.ethz.mlmq.common.BalancedScenarioConfiguration;
import ch.ethz.mlmq.common.InvalidConfigurationException;
import ch.ethz.mlmq.logging.LoggerUtil;

public class ScenarioConfigurationTest {

	@BeforeClass
	public static void beforeClass() throws IOException {
		LoggerUtil.initConsoleDebug();
	}

	@Test
	public void testWithOneBroker() throws InvalidConfigurationException {
		String m = "broker:127.0.0.1;client:127.0.0.1";
		BalancedScenarioConfiguration s1 = new BalancedScenarioConfiguration(m, 0, "");
		Assert.assertEquals("127.0.0.1", s1.getBrokerHost());
		BalancedScenarioConfiguration s2 = new BalancedScenarioConfiguration(m, 1, "");
		Assert.assertEquals("127.0.0.1", s2.getBrokerHost());
		BalancedScenarioConfiguration s3 = new BalancedScenarioConfiguration(m, 2, "");
		Assert.assertEquals("127.0.0.1", s3.getBrokerHost());
		BalancedScenarioConfiguration s4 = new BalancedScenarioConfiguration(m, 3, "");
		Assert.assertEquals("127.0.0.1", s4.getBrokerHost());
		BalancedScenarioConfiguration s5 = new BalancedScenarioConfiguration(m, 4, "");
		Assert.assertEquals("127.0.0.1", s5.getBrokerHost());
	}

	@Test
	public void testWithMultipleBrokers() throws InvalidConfigurationException {
		String m = "broker:127.0.0.1,127.0.0.2,127.0.0.3;client:127.0.0.1";
		BalancedScenarioConfiguration s1 = new BalancedScenarioConfiguration(m, 0, "");
		Assert.assertEquals("127.0.0.1", s1.getBrokerHost());
		BalancedScenarioConfiguration s2 = new BalancedScenarioConfiguration(m, 1, "");
		Assert.assertEquals("127.0.0.2", s2.getBrokerHost());
		BalancedScenarioConfiguration s3 = new BalancedScenarioConfiguration(m, 2, "");
		Assert.assertEquals("127.0.0.3", s3.getBrokerHost());
		BalancedScenarioConfiguration s4 = new BalancedScenarioConfiguration(m, 3, "");
		Assert.assertEquals("127.0.0.1", s4.getBrokerHost());
		BalancedScenarioConfiguration s5 = new BalancedScenarioConfiguration(m, 4, "");
		Assert.assertEquals("127.0.0.2", s5.getBrokerHost());
	}

	@Test
	public void testWithNoBrokers() {
		String m = "special:127.0.0.1,127.0.0.2,127.0.0.3;client:127.0.0.1";
		try {
			BalancedScenarioConfiguration s1 = new BalancedScenarioConfiguration(m, 0, "");
			Assert.assertEquals("127.0.0.1", s1.getBrokerHost());
			Assert.fail("Expected InvalidConfigurationException");
		} catch (InvalidConfigurationException e) {
			Assert.assertTrue(true);
		}
	}

	@Test
	public void testBrokerPort() throws InvalidConfigurationException {
		String m = "special:127.0.0.1,127.0.0.2,127.0.0.3;client:127.0.0.1";
		BalancedScenarioConfiguration s = new BalancedScenarioConfiguration(m, 0, "");
		Assert.assertEquals(8099, s.getBrokerPort());
	}

	@Test
	public void testMyPosition() throws InvalidConfigurationException {
		String m1 = "special:127.0.0.1,127.0.0.2,127.0.0.3;client:127.0.0.1";
		BalancedScenarioConfiguration s1 = new BalancedScenarioConfiguration(m1, 0, "special");
		Assert.assertEquals(0, s1.getMyPosition());
		BalancedScenarioConfiguration s2 = new BalancedScenarioConfiguration(m1, 1, "special");
		Assert.assertEquals(1, s2.getMyPosition());
		BalancedScenarioConfiguration s3 = new BalancedScenarioConfiguration(m1, 2, "special");
		Assert.assertEquals(2, s3.getMyPosition());

		Assert.assertEquals(3, s1.getTotalLikeMe());
		Assert.assertEquals(3, s2.getTotalLikeMe());
		Assert.assertEquals(3, s3.getTotalLikeMe());

		m1 = "client:127.0.0.1;special:127.0.0.1,127.0.0.2,127.0.0.3";
		s1 = new BalancedScenarioConfiguration(m1, 0, "special");
		Assert.assertEquals(0, s1.getMyPosition());
		s2 = new BalancedScenarioConfiguration(m1, 1, "special");
		Assert.assertEquals(1, s2.getMyPosition());
		s3 = new BalancedScenarioConfiguration(m1, 2, "special");
		Assert.assertEquals(2, s3.getMyPosition());

		Assert.assertEquals(3, s1.getTotalLikeMe());
		Assert.assertEquals(3, s2.getTotalLikeMe());
		Assert.assertEquals(3, s3.getTotalLikeMe());
	}
}
