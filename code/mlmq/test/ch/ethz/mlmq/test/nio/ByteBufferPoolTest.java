package ch.ethz.mlmq.test.nio;

import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import ch.ethz.mlmq.logging.LoggerUtil;
import ch.ethz.mlmq.nio.ByteBufferPool;
import ch.ethz.mlmq.nio.CloseableByteBuffer;
import ch.ethz.mlmq.server.BrokerConfiguration;
import ch.ethz.mlmq.util.ConfigurationUtil;

public class ByteBufferPoolTest {

	private Logger logger = Logger.getLogger(NetworkInterfaceTest.class.getSimpleName());
	private int bufferSize = 10;

	@BeforeClass
	public static void beforeClass() throws IOException {
		LoggerUtil.initConsoleDebug();
	}

	@Test
	public void testByteBufferPool() throws IOException {
		logger.info("Test ByteBuffer");
		BrokerConfiguration config = getTestConfig();
		ByteBufferPool pool = new ByteBufferPool(config);

		CloseableByteBuffer buffer1 = pool.aquire();
		CloseableByteBuffer buffer2 = pool.aquire();
		CloseableByteBuffer buffer3 = pool.aquire();
		CloseableByteBuffer buffer4 = pool.aquire();

		Assert.assertNotNull(buffer1);
		Assert.assertNotNull(buffer2);
		Assert.assertNotNull(buffer3);
		Assert.assertNotNull(buffer4);

		pool.release(buffer1);
		CloseableByteBuffer buffer1copy = pool.aquire();
		Assert.assertTrue(buffer1 == buffer1copy);
		logger.info("Test ByteBuffer finished");
	}

	private BrokerConfiguration getTestConfig() throws IOException {
		Properties props = ConfigurationUtil.loadPropertiesFromJar("brokerconfig.properties");
		props.put(BrokerConfiguration.MAX_MESSAGE_SIZE, bufferSize + "");
		return new BrokerConfiguration(props);
	}
}
