package ch.ethz.mlmq.test.nio;

import java.io.IOException;
import java.util.logging.Logger;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import ch.ethz.mlmq.client.Client;
import ch.ethz.mlmq.client.ClientImpl;
import ch.ethz.mlmq.dto.BrokerDto;
import ch.ethz.mlmq.dto.QueueDto;
import ch.ethz.mlmq.logging.LoggerUtil;
import ch.ethz.mlmq.nio.BrokerNetworkInterface;
import ch.ethz.mlmq.server.BrokerConfiguration;

public class NetworkInterfaceTest {

	private Logger logger = Logger.getLogger("NetworkInterfaceTest");

	private BrokerNetworkInterface networkInterface;
	private BrokerConfiguration config;
	private Client client;

	@BeforeClass
	public static void beforeClass() throws IOException {
		LoggerUtil.initConsoleDebug();
	}

	@Before
	public void before() throws IOException {
		config = new BrokerConfiguration();

		setupNetworkInterface();
		setupClient();
	}

	private void setupClient() throws IOException {
		BrokerDto defaultBroker = new BrokerDto();

		defaultBroker.setHost("localhost");
		defaultBroker.setPort(config.getListenPort());

		client = new ClientImpl(defaultBroker);
	}

	private void setupNetworkInterface() {
		logger.info("Create network interface");
		networkInterface = new BrokerNetworkInterface(config);

		logger.info("Init network interface");
		networkInterface.init();
	}

	@After
	public void after() {
		logger.info("Close network interface");
		networkInterface.close();
	}

	@Test
	public void testSendSimpleMessage() throws IOException {
		QueueDto response = client.createQueue();

		Assert.assertNotNull(response);

		client.deleteQueue(response.getId());
	}
}
