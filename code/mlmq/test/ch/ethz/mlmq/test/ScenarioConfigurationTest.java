package ch.ethz.mlmq.test;

import java.io.IOException;
import java.util.List;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import ch.ethz.mlmq.common.BrokerScenarioMapping;
import ch.ethz.mlmq.common.ClientScenarioMapping;
import ch.ethz.mlmq.common.Configuration;
import ch.ethz.mlmq.exception.InvalidConfigurationException;
import ch.ethz.mlmq.logging.LoggerUtil;

public class ScenarioConfigurationTest {

	@BeforeClass
	public static void beforeClass() throws IOException {
		LoggerUtil.initConsoleDebug();
	}

	@Test
	public void testWithOneBroker() throws InvalidConfigurationException {
		String b = "broker#127.0.0.1:1234";

		List<BrokerScenarioMapping> brokerMappings = Configuration.parseBrokerScenarioMapping(b);

		Assert.assertEquals(1, brokerMappings.size());
		BrokerScenarioMapping brokerMapping = brokerMappings.get(0);
		Assert.assertEquals("broker", brokerMapping.getScenarioClassName());
		Assert.assertEquals("127.0.0.1", brokerMapping.getHost());
		Assert.assertEquals(1234, brokerMapping.getPort());
	}

	@Test
	public void testWithOneClient() throws InvalidConfigurationException {
		String c = "client#127.0.0.2";

		List<ClientScenarioMapping> mappings = Configuration.parseClientScenarioMapping(c);

		Assert.assertEquals(1, mappings.size());
		ClientScenarioMapping clientMapping = mappings.get(0);

		Assert.assertEquals("client", clientMapping.getScenarioClassName());
		Assert.assertEquals("127.0.0.2", clientMapping.getHost());
	}

	@Test
	public void testComplicatedBroker() throws InvalidConfigurationException {
		String b = "brokerType1#127.0.0.1:1234;127.0.0.2:1235;brokerType2#127.0.0.3:1236;brokerType2#127.0.0.4:1237;127.0.0.5:1238";
		List<BrokerScenarioMapping> mapping = Configuration.parseBrokerScenarioMapping(b);

		Assert.assertEquals(5, mapping.size());
		BrokerScenarioMapping brokerScenarioMapping;

		// Broker 0
		brokerScenarioMapping = mapping.get(0);
		Assert.assertEquals("brokerType1", brokerScenarioMapping.getScenarioClassName());
		Assert.assertEquals("127.0.0.1", brokerScenarioMapping.getHost());
		Assert.assertEquals(1234, brokerScenarioMapping.getPort());
		Assert.assertEquals(0, brokerScenarioMapping.getPosition());

		// Broker 1
		brokerScenarioMapping = mapping.get(1);
		Assert.assertEquals("brokerType1", brokerScenarioMapping.getScenarioClassName());
		Assert.assertEquals("127.0.0.2", brokerScenarioMapping.getHost());
		Assert.assertEquals(1235, brokerScenarioMapping.getPort());
		Assert.assertEquals(1, brokerScenarioMapping.getPosition());

		// Broker 2
		brokerScenarioMapping = mapping.get(2);
		Assert.assertEquals("brokerType2", brokerScenarioMapping.getScenarioClassName());
		Assert.assertEquals("127.0.0.3", brokerScenarioMapping.getHost());
		Assert.assertEquals(1236, brokerScenarioMapping.getPort());
		Assert.assertEquals(2, brokerScenarioMapping.getPosition());

		// Broker 3
		brokerScenarioMapping = mapping.get(3);
		Assert.assertEquals("brokerType2", brokerScenarioMapping.getScenarioClassName());
		Assert.assertEquals("127.0.0.4", brokerScenarioMapping.getHost());
		Assert.assertEquals(1237, brokerScenarioMapping.getPort());
		Assert.assertEquals(3, brokerScenarioMapping.getPosition());

		// Broker 4
		brokerScenarioMapping = mapping.get(4);
		Assert.assertEquals("brokerType2", brokerScenarioMapping.getScenarioClassName());
		Assert.assertEquals("127.0.0.5", brokerScenarioMapping.getHost());
		Assert.assertEquals(1238, brokerScenarioMapping.getPort());
		Assert.assertEquals(4, brokerScenarioMapping.getPosition());

	}

	@Test
	public void testComplicatedClient() throws InvalidConfigurationException {
		String c = "client#127.0.0.1;127.0.0.2;127.0.0.3;127.0.0.4;127.0.0.5;127.0.0.6";

		List<ClientScenarioMapping> mapping = Configuration.parseClientScenarioMapping(c);

		// check Client Mapping
		Assert.assertEquals(6, mapping.size());
		for (int i = 0; i < 6; i++) {
			ClientScenarioMapping clientScenarioMapping = mapping.get(i);

			Assert.assertEquals("client", clientScenarioMapping.getScenarioClassName());
			Assert.assertEquals("127.0.0." + (i + 1), clientScenarioMapping.getHost());
			Assert.assertEquals(i, clientScenarioMapping.getPosition());
		}
	}

}
