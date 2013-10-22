package ch.ethz.mlmq.test;

import java.io.IOException;
import java.util.Properties;

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

	class TestConfiguration extends Configuration {

		protected TestConfiguration(Properties props) {
			super(props);
		}

	};

	@Test
	public void testWithOneBroker() throws InvalidConfigurationException {
		String b = "broker#127.0.0.1:1234";
		String c = "client#127.0.0.2";
		TestConfiguration config = new TestConfiguration(createProps(b, c));

		Assert.assertEquals(1, config.getAllBrokerScenarioMapping().size());
		Assert.assertEquals(1, config.getAllClientScenarioMapping().size());

		BrokerScenarioMapping brokerMapping = config.getAllBrokerScenarioMapping().get(0);
		ClientScenarioMapping clientMapping = config.getAllClientScenarioMapping().get(0);

		Assert.assertEquals("broker", brokerMapping.getName());
		Assert.assertEquals("127.0.0.1", brokerMapping.getName());
		Assert.assertEquals("1234", brokerMapping.getPort());
		Assert.assertEquals("client", clientMapping.getName());
		Assert.assertEquals("127.0.0.2", clientMapping.getName());
	}

	@Test
	public void testComplicated() throws InvalidConfigurationException {
		String b = "brokerType1#127.0.0.1:1234;127.0.0.2:1235;brokerType2#127.0.0.3:1236;brokerType2#127.0.0.4:1237;127.0.0.5:1238";
		String c = "client#127.0.0.1;127.0.0.2;127.0.0.3;127.0.0.4;127.0.0.5;127.0.0.6";
		TestConfiguration config = new TestConfiguration(createProps(b, c));

		BrokerScenarioMapping brokerScenarioMapping;

		Assert.assertEquals(5, config.getAllBrokerScenarioMapping().size());

		// Broker 0
		brokerScenarioMapping = config.getAllBrokerScenarioMapping().get(0);
		Assert.assertEquals("brokerType1", brokerScenarioMapping.getName());
		Assert.assertEquals("127.0.0.1", brokerScenarioMapping.getHost());
		Assert.assertEquals("1234", brokerScenarioMapping.getPort());
		Assert.assertEquals(0, brokerScenarioMapping.getPosition());

		// Broker 1
		brokerScenarioMapping = config.getAllBrokerScenarioMapping().get(1);
		Assert.assertEquals("brokerType1", brokerScenarioMapping.getName());
		Assert.assertEquals("127.0.0.2", brokerScenarioMapping.getHost());
		Assert.assertEquals("1235", brokerScenarioMapping.getPort());
		Assert.assertEquals(1, brokerScenarioMapping.getPosition());

		// Broker 2
		brokerScenarioMapping = config.getAllBrokerScenarioMapping().get(1);
		Assert.assertEquals("brokerType2", brokerScenarioMapping.getName());
		Assert.assertEquals("127.0.0.3", brokerScenarioMapping.getHost());
		Assert.assertEquals("1236", brokerScenarioMapping.getPort());
		Assert.assertEquals(2, brokerScenarioMapping.getPosition());

		// Broker 3
		brokerScenarioMapping = config.getAllBrokerScenarioMapping().get(1);
		Assert.assertEquals("brokerType2", brokerScenarioMapping.getName());
		Assert.assertEquals("127.0.0.4", brokerScenarioMapping.getHost());
		Assert.assertEquals("1237", brokerScenarioMapping.getPort());
		Assert.assertEquals(3, brokerScenarioMapping.getPosition());

		// Broker 4
		brokerScenarioMapping = config.getAllBrokerScenarioMapping().get(1);
		Assert.assertEquals("brokerType2", brokerScenarioMapping.getName());
		Assert.assertEquals("127.0.0.5", brokerScenarioMapping.getHost());
		Assert.assertEquals("1238", brokerScenarioMapping.getPort());
		Assert.assertEquals(4, brokerScenarioMapping.getPosition());

		// check Client Mapping
		Assert.assertEquals(6, config.getAllClientScenarioMapping().size());
		for (int i = 0; i < 6; i++) {
			ClientScenarioMapping clientScenarioMapping = config.getAllClientScenarioMapping().get(i);

			Assert.assertEquals("client", clientScenarioMapping.getName());
			Assert.assertEquals("127.0.0." + (i + 1), clientScenarioMapping.getHost());
			Assert.assertEquals(i, clientScenarioMapping.getPosition());
		}
	}

	private Properties createProps(String brokerMapping, String clientMapping) {
		Properties props = new Properties();
		props.put(Configuration.PERFORMANCELOGGER_PATH, "log");
		props.put(Configuration.SCENARIO_MYTYPE_BROKER_VALUE, brokerMapping);
		props.put(Configuration.SCENARIO_MYTYPE_CLIENT_VALUE, clientMapping);

		return props;
	}
}
