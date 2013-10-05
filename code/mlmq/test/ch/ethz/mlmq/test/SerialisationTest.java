package ch.ethz.mlmq.test;

import java.io.IOException;
import java.nio.ByteBuffer;

import org.junit.Assert;
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
import ch.ethz.mlmq.net.request.RequestResponseFactory;
import ch.ethz.mlmq.net.request.SendMessageRequest;
import ch.ethz.mlmq.net.response.CreateQueueResponse;
import ch.ethz.mlmq.net.response.ExceptionResponse;
import ch.ethz.mlmq.net.response.HostForQueueResponse;
import ch.ethz.mlmq.net.response.MessageResponse;
import ch.ethz.mlmq.net.response.QueuesWithPendingMessagesResponse;
import ch.ethz.mlmq.net.response.RegistrationResponse;
import ch.ethz.mlmq.net.response.Response;

public class SerialisationTest {

	private RequestResponseFactory reqRespFactory = new RequestResponseFactory();

	@BeforeClass
	public static void beforeClass() throws IOException {
		LoggerUtil.initConsoleDebug();
	}

	@Test
	public void testSerializeCreateQueueRequest() {
		CreateQueueRequest request = new CreateQueueRequest();
		testRequest(request);
	}

	@Test
	public void testHostForQueueRequest() {
		HostForQueueRequest request = new HostForQueueRequest((long) Math.random());
		testRequest(request);
	}

	@Test
	public void testQueuesWithPendingMessagesRequest() {
		QueuesWithPendingMessagesRequest request = new QueuesWithPendingMessagesRequest();

		testRequest(request);
	}

	@Test
	public void testRegistrationRequest() {
		RegistrationRequest request = new RegistrationRequest();

		testRequest(request);
	}

	@Test
	public void testDeleteQueueRequest() {
		DeleteQueueRequest request = new DeleteQueueRequest((long) Math.random());

		testRequest(request);
	}

	@Test
	public void testDequeueMessageRequest() {

		QueueDto queueFilter = null;
		ClientDto sender = null;
		boolean shouldOrderByPriority = false;
		MessageQueryInfoDto messageQueryInfo = new MessageQueryInfoDto(queueFilter, sender, shouldOrderByPriority);
		DequeueMessageRequest request = new DequeueMessageRequest(messageQueryInfo);

		testRequest(request);
	}

	@Test
	public void testPeekMessageRequest() {

		QueueDto queueFilter = null;
		ClientDto sender = null;
		boolean shouldOrderByPriority = false;
		MessageQueryInfoDto messageQueryInfo = new MessageQueryInfoDto(queueFilter, sender, shouldOrderByPriority);
		PeekMessageRequest request = new PeekMessageRequest(messageQueryInfo);

		testRequest(request);
	}

	@Test
	public void testSendMessageRequest() {
		long queueId = Long.MAX_VALUE;
		byte[] content = "Hello World".getBytes();
		int prio = 5;

		SendMessageRequest request = new SendMessageRequest(queueId, content, prio);

		testRequest(request);
	}

	@Test
	public void testCreateQueueResponse() {
		QueueDto queue = new QueueDto(42);
		CreateQueueResponse response = new CreateQueueResponse(queue);
		testResponse(response);
	}

	@Test
	public void testExceptionResponse() {
		MlmqException exception = new MlmqException("This is any random excpetion", new RuntimeException("Nested Exception"));
		ExceptionResponse response = new ExceptionResponse(exception);
		testResponse(response);
	}

	@Test
	public void testHostForQueueResponse() {
		HostForQueueResponse response = new HostForQueueResponse();
		testResponse(response);
	}

	@Test
	public void testMessageResponse() {
		MessageResponse response = new MessageResponse();
		testResponse(response);
	}

	@Test
	public void testQueuesWithPendingMessagesResponse() {
		QueuesWithPendingMessagesResponse response = new QueuesWithPendingMessagesResponse();
		testResponse(response);
	}

	@Test
	public void testRegistrationResponse() {
		ClientDto clientDto = new ClientDto(27);
		RegistrationResponse response = new RegistrationResponse(clientDto);
		testResponse(response);
	}

	private void testResponse(Response response) {
		ByteBuffer serializeBuffer = ByteBuffer.allocate(4000);

		reqRespFactory.serializeResponse(response, serializeBuffer);

		Assert.assertTrue(0 != serializeBuffer.position());

		serializeBuffer.flip();
		Response responseActual = reqRespFactory.deserializeResponse(serializeBuffer);

		Assert.assertEquals(response.getClass(), responseActual.getClass());
		Assert.assertEquals(response, responseActual);
	}

	private void testRequest(Request request) {

		ByteBuffer serializeBuffer = ByteBuffer.allocate(4000);

		reqRespFactory.serializeRequest(request, serializeBuffer);

		Assert.assertTrue(0 != serializeBuffer.position());

		serializeBuffer.flip();
		Request requestActual = reqRespFactory.deserializeRequest(serializeBuffer);

		Assert.assertEquals(request.getClass(), requestActual.getClass());
		Assert.assertEquals(request, requestActual);
	}
}
