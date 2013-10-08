package ch.ethz.mlmq.test.processing;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

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
import ch.ethz.mlmq.net.request.PeekMessageRequest;
import ch.ethz.mlmq.net.request.QueuesWithPendingMessagesRequest;
import ch.ethz.mlmq.net.request.RegistrationRequest;
import ch.ethz.mlmq.net.request.Request;
import ch.ethz.mlmq.net.request.SendMessageRequest;
import ch.ethz.mlmq.net.response.CreateQueueResponse;
import ch.ethz.mlmq.net.response.DeleteQueueResponse;
import ch.ethz.mlmq.net.response.MessageResponse;
import ch.ethz.mlmq.net.response.QueuesWithPendingMessagesResponse;
import ch.ethz.mlmq.net.response.RegistrationResponse;
import ch.ethz.mlmq.net.response.SendMessageResponse;
import ch.ethz.mlmq.server.BrokerConfiguration;
import ch.ethz.mlmq.server.ClientApplicationContext;
import ch.ethz.mlmq.server.db.DbConnectionPool;
import ch.ethz.mlmq.server.db.util.DatabaseInitializer;
import ch.ethz.mlmq.server.processing.RequestProcessor;

public class RequestProcessorTest {

	private final Logger logger = Logger.getLogger(RequestProcessorTest.class.getSimpleName());

	private static BrokerConfiguration config;

	private RequestProcessor processor;

	private static DatabaseInitializer dbInitializer;
	private static String dbName = "mlmqunittest" + System.currentTimeMillis();

	private static DbConnectionPool pool;

	private ClientApplicationContext defaultContext = null;

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
	public void before() throws MlmqException {
		processor = new RequestProcessor();

		defaultContext = registerClient();
	}

	private ClientApplicationContext registerClient() throws MlmqException {
		logger.info("Register UnitTestClient");

		ClientApplicationContext context = new ClientApplicationContext(1);
		RegistrationRequest registerRequest = new RegistrationRequest("UnitTestClient");
		processor.process(context, registerRequest, pool);

		return context;
	}

	@Test
	public void doTest() throws MlmqException {
		testRegistrationRequest();
		testCreateQueueRequest();
		testSendMessageRequest();
		testDequeueMessageRequest();
		testPeekMessageRequest();
		testQueuesWithPendingMessagesRequest();
		testDeleteQueueRequest();
	}

	/**
	 * Clients can send messages to multiple queues
	 * 
	 * @throws MlmqException
	 */
	@Test
	public void testSendToMultipleQueues() throws MlmqException {

		int numQueues = 10;
		List<Long> queueIdList = new ArrayList<>();

		byte[] content = "Hallo Welt".getBytes();
		int prio = 10;

		for (int i = 0; i < numQueues; i++) {
			String queueName = "testSendToMultipleQueues" + i;
			logger.info("Creating Queue " + queueName);
			CreateQueueRequest createQueueRequest = new CreateQueueRequest(queueName);
			CreateQueueResponse response = (CreateQueueResponse) processor.process(defaultContext, createQueueRequest, pool);
			queueIdList.add(response.getQueueDto().getId());
		}

		Request request = new SendMessageRequest(queueIdList, content, prio);
		SendMessageResponse response = (SendMessageResponse) processor.process(defaultContext, request, pool);
		Assert.assertNotNull(response);

		for (Long queueId : queueIdList) {

			QueueDto queueFilter = new QueueDto(queueId);
			ClientDto sender = null;
			boolean shouldOrderByPriority = true;
			MessageQueryInfoDto messageQueryInfo = new MessageQueryInfoDto(queueFilter, sender, shouldOrderByPriority);
			DequeueMessageRequest dequeuRequest = new DequeueMessageRequest(messageQueryInfo);

			MessageResponse messageResponse = (MessageResponse) processor.process(defaultContext, dequeuRequest, pool);
			Assert.assertNotNull(messageResponse);

			Assert.assertArrayEquals(content, messageResponse.getMessageDto().getContent());
			Assert.assertEquals(prio, messageResponse.getMessageDto().getPrio());
		}
	}

	/**
	 * Clients can send a message to a queue indicating a particular receiver
	 * 
	 * If a message has an explicit receiver, it can only be accessed by that receiver
	 * 
	 * @throws MlmqException
	 */
	@Test
	public void testSendToClient() throws MlmqException {
		ClientApplicationContext clientContext1 = registerClient();
		ClientApplicationContext clientContext2 = registerClient();

		byte[] content = "Blub".getBytes();
		int prio = 5;

		logger.info("Client 1 sends a message to Client 2");
		long queueId = clientContext2.getClientQueue().getId();
		SendMessageRequest sendToClient2 = new SendMessageRequest(queueId, content, prio);
		processor.process(clientContext1, sendToClient2, pool);

		logger.info("Client 2 wants to read it");
		QueueDto queueFilter = clientContext2.getClientQueue();
		ClientDto sender = null;
		boolean shouldOrderByPriority = true;
		MessageQueryInfoDto messageQueryInfo = new MessageQueryInfoDto(queueFilter, sender, shouldOrderByPriority);
		DequeueMessageRequest readMessage = new DequeueMessageRequest(messageQueryInfo);
		MessageResponse receiveMessageResponse = (MessageResponse) processor.process(clientContext2, readMessage, pool);

		Assert.assertNotNull(receiveMessageResponse);
		Assert.assertArrayEquals(content, receiveMessageResponse.getMessageDto().getContent());
	}

	/**
	 * Clients can query for messages from a particular sender (at most one message is returned)
	 */
	@Test
	public void testQueryMessageFromSender() {
		Assert.fail("TODO");
	}

	public void testCreateQueueRequest() throws MlmqException {

		Request request = new CreateQueueRequest("SampleQueue");
		CreateQueueResponse response = (CreateQueueResponse) processor.process(defaultContext, request, pool);
		Assert.assertNotNull(response);
		Assert.assertNotNull(response.getQueueDto());
	}

	public void testQueuesWithPendingMessagesRequest() throws MlmqException {
		Request request = new QueuesWithPendingMessagesRequest();
		QueuesWithPendingMessagesResponse response = (QueuesWithPendingMessagesResponse) processor.process(defaultContext, request, pool);
		Assert.assertNotNull(response);
		Assert.assertNotNull(response.getQueues());
	}

	public void testRegistrationRequest() throws MlmqException {
		Request request = new RegistrationRequest("ClientName");
		RegistrationResponse response = (RegistrationResponse) processor.process(defaultContext, request, pool);
		Assert.assertNotNull(response);
		Assert.assertNotNull(response.getClientDto());
	}

	public void testDeleteQueueRequest() throws MlmqException {
		Request request = new DeleteQueueRequest(1);
		DeleteQueueResponse response = (DeleteQueueResponse) processor.process(defaultContext, request, pool);
		Assert.assertNotNull(response);
	}

	public void testDequeueMessageRequest() throws MlmqException {
		Request request = new DequeueMessageRequest(createTestMessageQueryInfoDto());
		MessageResponse response = (MessageResponse) processor.process(defaultContext, request, pool);
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
		MessageResponse response = (MessageResponse) processor.process(defaultContext, request, pool);
		Assert.assertNotNull(response);
	}

	public void testSendMessageRequest() throws MlmqException {
		long queueId = 1;
		byte[] content = "Hallo Welt".getBytes();
		int prio = 10;
		Request request = new SendMessageRequest(queueId, content, prio);
		SendMessageResponse response = (SendMessageResponse) processor.process(defaultContext, request, pool);
		Assert.assertNotNull(response);
	}
}
