package ch.ethz.mlmq.test.processing;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import ch.ethz.mlmq.dto.ClientDto;
import ch.ethz.mlmq.dto.MessageQueryInfoDto;
import ch.ethz.mlmq.dto.QueueDto;
import ch.ethz.mlmq.exception.MlmqException;
import ch.ethz.mlmq.logging.LoggerUtil;
import ch.ethz.mlmq.net.request.CreateQueueRequest;
import ch.ethz.mlmq.net.request.DeleteQueueRequest;
import ch.ethz.mlmq.net.request.DequeueMessageRequest;
import ch.ethz.mlmq.net.request.HostForQueueRequest;
import ch.ethz.mlmq.net.request.PeekMessageRequest;
import ch.ethz.mlmq.net.request.QueuesWithPendingMessagesRequest;
import ch.ethz.mlmq.net.request.RegistrationRequest;
import ch.ethz.mlmq.net.request.Request;
import ch.ethz.mlmq.net.request.SendMessageRequest;
import ch.ethz.mlmq.net.response.CreateQueueResponse;
import ch.ethz.mlmq.net.response.DeleteQueueResponse;
import ch.ethz.mlmq.net.response.HostForQueueResponse;
import ch.ethz.mlmq.net.response.MessageResponse;
import ch.ethz.mlmq.net.response.QueuesWithPendingMessagesResponse;
import ch.ethz.mlmq.net.response.RegistrationResponse;
import ch.ethz.mlmq.server.BrokerConfiguration;
import ch.ethz.mlmq.server.db.DbConnectionPool;
import ch.ethz.mlmq.server.db.util.DatabaseInitializer;
import ch.ethz.mlmq.server.processing.RequestProcessor;

public class RequestProcessorTest {

	private static BrokerConfiguration config;

	private RequestProcessor processor;

	private static DatabaseInitializer dbInitializer;
	private static String dbName = "mlmqunittest" + System.currentTimeMillis();

	private static DbConnectionPool pool;

	@BeforeClass
	public static void beforeClass() throws IOException, SQLException, MlmqException {
		LoggerUtil.initConsoleDebug();

		// load properties
		Properties props = BrokerConfiguration.loadProperties("unittestconfig.example.properties");
		props.put(BrokerConfiguration.DB_NAME, dbName);
		props.put(BrokerConfiguration.DB_CONNECTIONPOOL_SIZE, 1 + "");
		config = new BrokerConfiguration(props);

		dbInitializer = new DatabaseInitializer(config.getDbUrl(), config.getDbUserName(), config.getDbPassword(), dbName);
		dbInitializer.connect();
		dbInitializer.createDatabase();
		dbInitializer.createTables();

		pool = new DbConnectionPool(config);
		pool.init();
	}

	@AfterClass
	public static void afterClass() throws SQLException {
		pool.close();
		dbInitializer.deleteDatabase();
	}

	@Before
	public void before() {
		processor = new RequestProcessor(config);
	}

	@Test
	public void doTest() throws MlmqException {
		testCreateQueueRequest();
		testHostForQueueRequest();
		testSendMessageRequest();
		testQueuesWithPendingMessagesRequest();
		testRegistrationRequest();
		testDequeueMessageRequest();
		testPeekMessageRequest();
		testDeleteQueueRequest();
	}

	public void testCreateQueueRequest() throws MlmqException {
		Request request = new CreateQueueRequest();
		CreateQueueResponse response = (CreateQueueResponse) processor.process(1, request, pool);
		Assert.assertNotNull(response);
		Assert.assertNotNull(response.getQueueDto());
	}

	public void testHostForQueueRequest() throws MlmqException {
		Request request = new HostForQueueRequest(1);
		HostForQueueResponse response = (HostForQueueResponse) processor.process(1, request, pool);
		Assert.assertNotNull(response);
		Assert.assertNotNull(response.getBrokerDto());
	}

	public void testQueuesWithPendingMessagesRequest() throws MlmqException {
		Request request = new QueuesWithPendingMessagesRequest();
		QueuesWithPendingMessagesResponse response = (QueuesWithPendingMessagesResponse) processor.process(1, request, pool);
		Assert.assertNotNull(response);
		Assert.assertNotNull(response.getQueues());
	}

	public void testRegistrationRequest() throws MlmqException {
		Request request = new RegistrationRequest();
		RegistrationResponse response = (RegistrationResponse) processor.process(1, request, pool);
		Assert.assertNotNull(response);
		Assert.assertNotNull(response.getClientDto());
	}

	public void testDeleteQueueRequest() throws MlmqException {
		Request request = new DeleteQueueRequest(1);
		DeleteQueueResponse response = (DeleteQueueResponse) processor.process(1, request, pool);
		Assert.assertNotNull(response);
	}

	public void testDequeueMessageRequest() throws MlmqException {
		Request request = new DequeueMessageRequest(createTestMessageQueryInfoDto());
		MessageResponse response = (MessageResponse) processor.process(1, request, pool);
		Assert.assertNotNull(response);
	}

	private MessageQueryInfoDto createTestMessageQueryInfoDto() {
		QueueDto queueFilter = new QueueDto(123);
		ClientDto sender = new ClientDto(1);
		boolean shouldOrderByPriority = true;
		MessageQueryInfoDto messageQueryInfoDto = new MessageQueryInfoDto(queueFilter, sender, shouldOrderByPriority);

		return messageQueryInfoDto;
	}

	public void testPeekMessageRequest() throws MlmqException {
		Request request = new PeekMessageRequest(createTestMessageQueryInfoDto());
		MessageResponse response = (MessageResponse) processor.process(1, request, pool);
		Assert.assertNotNull(response);
	}

	public void testSendMessageRequest() throws MlmqException {
		long queueId = 1;
		byte[] content = "Hallo Welt".getBytes();
		int prio = 10;
		Request request = new SendMessageRequest(queueId, content, prio);
		MessageResponse response = (MessageResponse) processor.process(1, request, pool);
		Assert.assertNotNull(response);
	}
}
