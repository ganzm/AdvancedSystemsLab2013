package ch.ethz.mlmq.server.processing;

import java.nio.ByteBuffer;
import java.util.logging.Logger;

import ch.ethz.mlmq.exception.MlmqException;
import ch.ethz.mlmq.net.request.Request;
import ch.ethz.mlmq.net.request.RequestResponseFactory;
import ch.ethz.mlmq.net.response.ExceptionResponse;
import ch.ethz.mlmq.net.response.Response;
import ch.ethz.mlmq.nio.CloseableByteBuffer;
import ch.ethz.mlmq.server.db.DbConnectionPool;

public class Worker extends Thread {
	private final Logger logger = Logger.getLogger(Worker.class.getSimpleName());

	private final WorkerTaskQueueImpl requestQueue;
	private final WorkerTaskQueue responseQueue;

	private boolean running = false;

	private final RequestResponseFactory factory = new RequestResponseFactory();

	private final RequestProcessor processor;

	private final DbConnectionPool connectionPool;

	public Worker(String name, WorkerTaskQueueImpl requestQueue, WorkerTaskQueue responseQueue, DbConnectionPool connectionPool) {
		this.requestQueue = requestQueue;
		this.responseQueue = responseQueue;
		this.connectionPool = connectionPool;
		this.processor = new RequestProcessor();
		setName(name);
	}

	public void start() {
		logger.info("Starting Worker " + getName());
		super.start();
	}

	@Override
	public void run() {
		running = true;

		try {
			while (running) {
				WorkerTask task = requestQueue.blockingDequeue();
				if (task == null) {
					// nothing to work on
					continue;
				}

				try {
					process(task);
				} catch (Exception ex) {
					logger.severe(getName() + " Error while processing " + task);
				}
			}
		} finally {
			logger.info("Worker " + getName() + " closed");
		}
	}

	public void close() {
		logger.info("Closing Worker " + getName() + "...");
		running = false;
	}

	private void process(WorkerTask task) {
		CloseableByteBuffer requestBuffer = task.getAndRemoveRequestBuffer();
		ByteBuffer rawRequest = requestBuffer.getByteBuffer();

		Request request = factory.deserializeRequest(rawRequest);
		Response response = null;
		try {
			response = processor.process(task.getClientContext(), request, connectionPool);
		} catch (MlmqException ex) {
			response = new ExceptionResponse(ex);
		}

		// recycle RequestBuffer for the Response
		rawRequest.clear();
		factory.serializeResponseWithHeader(response, rawRequest);
		task.setResponseBuffer(requestBuffer);

		responseQueue.enqueue(task);
	}
}
