package ch.ethz.mlmq.server.processing;

import java.sql.SQLException;
import java.util.logging.Logger;

import ch.ethz.mlmq.dto.BrokerDto;
import ch.ethz.mlmq.dto.ClientDto;
import ch.ethz.mlmq.dto.QueueDto;
import ch.ethz.mlmq.exception.MlmqException;
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
import ch.ethz.mlmq.net.response.RegistrationResponse;
import ch.ethz.mlmq.net.response.Response;
import ch.ethz.mlmq.net.response.SendMessageResponse;
import ch.ethz.mlmq.server.BrokerConfiguration;
import ch.ethz.mlmq.server.ClientApplicationContext;
import ch.ethz.mlmq.server.db.DbConnection;
import ch.ethz.mlmq.server.db.DbConnectionPool;
import ch.ethz.mlmq.server.db.dao.ClientDao;
import ch.ethz.mlmq.server.db.dao.MessageDao;
import ch.ethz.mlmq.server.db.dao.QueueDao;

public class RequestProcessor {

	private final Logger logger = Logger.getLogger(RequestProcessor.class.getSimpleName());
	private BrokerConfiguration config;

	public RequestProcessor(BrokerConfiguration config) {
		this.config = config;
	}

	public Response process(ClientApplicationContext clientApplicationContext, Request request, DbConnectionPool pool) throws MlmqException {

		logger.info("Process Request " + request);

		if (request instanceof CreateQueueRequest) {
			return processCreateQueueRequest((CreateQueueRequest) request, pool);

		} else if (request instanceof HostForQueueRequest) {
			return processHostForQueueRequest((HostForQueueRequest) request, pool);

		} else if (request instanceof QueuesWithPendingMessagesRequest) {
			throw new MlmqException("TODO - not yet implemented");

		} else if (request instanceof RegistrationRequest) {
			return processRegistrationRequest((RegistrationRequest) request, clientApplicationContext, pool);

		} else if (request instanceof DeleteQueueRequest) {
			return processDeleteQueueRequest((DeleteQueueRequest) request, pool);

		} else if (request instanceof DequeueMessageRequest) {
			return processDequeueMessageRequest((DequeueMessageRequest) request, clientApplicationContext, pool);

		} else if (request instanceof PeekMessageRequest) {
			throw new MlmqException("TODO - not yet implemented");

		} else if (request instanceof SendMessageRequest) {
			return processSendMessageRequest((SendMessageRequest) request, clientApplicationContext, pool);

		} else {
			throw new MlmqException("Unexpected Request to process " + request.getClass().getSimpleName() + " - " + request);
		}
	}

	private Response processDequeueMessageRequest(DequeueMessageRequest request, ClientApplicationContext clientApplicationContext, DbConnectionPool pool)
			throws MlmqException {

		DbConnection connection = null;
		try {
			connection = pool.getConnection();
			MessageDao messageDao = connection.getMessageDao();

			return messageDao.dequeueMessage(request);

		} catch (SQLException ex) {
			connection.close();
			throw new MlmqException(ex);
		} finally {
			pool.returnConnection(connection);
		}
	}

	private Response processSendMessageRequest(SendMessageRequest request, ClientApplicationContext clientApplicationContext, DbConnectionPool pool)
			throws MlmqException {
		DbConnection connection = null;
		try {
			connection = pool.getConnection();

			MessageDao messageDao = connection.getMessageDao();
			messageDao.insertMessage(request, clientApplicationContext);
			SendMessageResponse response = new SendMessageResponse();
			return response;
		} catch (SQLException ex) {
			connection.close();
			throw new MlmqException(ex);
		} finally {
			pool.returnConnection(connection);
		}
	}

	private Response processHostForQueueRequest(HostForQueueRequest request, DbConnectionPool pool) {

		// TODO this is wrong
		String host = "localhost";

		int port = config.getListenPort();
		BrokerDto brokerDto = new BrokerDto(1, host, port);
		HostForQueueResponse response = new HostForQueueResponse(brokerDto);
		return response;
	}

	private Response processDeleteQueueRequest(DeleteQueueRequest request, DbConnectionPool pool) throws MlmqException {

		DbConnection connection = null;
		try {
			connection = pool.getConnection();

			QueueDao queueDao = connection.getQueueDao();

			long queueIdToDelete = request.getQueueId();
			queueDao.deleteQueue(queueIdToDelete);

			return new DeleteQueueResponse();

		} catch (SQLException ex) {
			connection.close();
			throw new MlmqException(ex);
		} finally {
			pool.returnConnection(connection);
		}

	}

	private Response processCreateQueueRequest(CreateQueueRequest request, DbConnectionPool pool) throws MlmqException {
		DbConnection connection = null;
		try {
			connection = pool.getConnection();

			QueueDao queueDao = connection.getQueueDao();
			QueueDto queue = queueDao.createQueue();

			CreateQueueResponse response = new CreateQueueResponse(queue);
			return response;

		} catch (SQLException ex) {
			connection.close();
			throw new MlmqException(ex);
		} finally {
			pool.returnConnection(connection);
		}
	}

	private Response processRegistrationRequest(RegistrationRequest request, ClientApplicationContext clientApplicationContext, DbConnectionPool pool)
			throws MlmqException {

		DbConnection connection = null;
		try {
			connection = pool.getConnection();

			ClientDao clientDao = connection.getClientDao();

			int newClientId = clientDao.insertNewClient(request.getClientName());

			clientApplicationContext.setClientId(newClientId);
			clientApplicationContext.setClientName(request.getClientName());

			ClientDto clientDto = new ClientDto(newClientId);
			RegistrationResponse response = new RegistrationResponse(clientDto);
			return response;

		} catch (SQLException ex) {
			connection.close();
			throw new MlmqException(ex);
		} finally {
			pool.returnConnection(connection);
		}
	}
}
