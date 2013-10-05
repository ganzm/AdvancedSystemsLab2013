package ch.ethz.mlmq.server.processing;

import java.util.logging.Logger;

import ch.ethz.mlmq.dto.ClientDto;
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
import ch.ethz.mlmq.net.response.RegistrationResponse;
import ch.ethz.mlmq.net.response.Response;
import ch.ethz.mlmq.server.db.DbConnectionPool;

public class RequestProcessor {

	private final Logger logger = Logger.getLogger(RequestProcessor.class.getSimpleName());

	public Response process(int clientId, Request request, DbConnectionPool pool) throws MlmqException {

		logger.info("Process Request " + request);

		if (request instanceof CreateQueueRequest) {
			throw new MlmqException("TODO - not yet implemented");
		} else if (request instanceof HostForQueueRequest) {
			throw new MlmqException("TODO - not yet implemented");
		} else if (request instanceof QueuesWithPendingMessagesRequest) {
			throw new MlmqException("TODO - not yet implemented");
		} else if (request instanceof RegistrationRequest) {
			return processRegistrationRequest(clientId);
		} else if (request instanceof DeleteQueueRequest) {
			throw new MlmqException("TODO - not yet implemented");
		} else if (request instanceof DequeueMessageRequest) {
			throw new MlmqException("TODO - not yet implemented");
		} else if (request instanceof PeekMessageRequest) {
			throw new MlmqException("TODO - not yet implemented");
		} else if (request instanceof SendMessageRequest) {
			throw new MlmqException("TODO - not yet implemented");
		} else {
			throw new MlmqException("Unexpected Request to process " + request.getClass().getSimpleName() + " - " + request);
		}
	}

	private Response processRegistrationRequest(int clientId) {
		ClientDto clientDto = new ClientDto(clientId);
		RegistrationResponse response = new RegistrationResponse(clientDto);
		return response;
	}
}
