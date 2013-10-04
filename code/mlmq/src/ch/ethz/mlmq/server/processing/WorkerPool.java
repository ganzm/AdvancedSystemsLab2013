package ch.ethz.mlmq.server.processing;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import ch.ethz.mlmq.server.BrokerConfiguration;
import ch.ethz.mlmq.server.db.DbConnectionPool;

public class WorkerPool {

	private final Logger logger = Logger.getLogger(WorkerPool.class.getSimpleName());

	private final List<Worker> workers;
	private final int workerThreadCount;
	private final WorkerTaskQueue responseQueue;
	private final WorkerTaskQueueImpl requestQueue;
	private final DbConnectionPool connectionPool;

	public WorkerPool(BrokerConfiguration config, WorkerTaskQueueImpl requestQueue, WorkerTaskQueue responseQueue, DbConnectionPool connectionPool) {
		this.requestQueue = requestQueue;
		this.responseQueue = responseQueue;
		this.connectionPool = connectionPool;
		this.workerThreadCount = config.getWorkerThreadCount();
		this.workers = new ArrayList<>(workerThreadCount);
	}

	public void init() {
		logger.info("Init WorkerPool - Number of workers " + workerThreadCount);
		for (int i = 0; i < workerThreadCount; i++) {
			Worker worker = new Worker("Worker" + i, requestQueue, responseQueue, connectionPool);
			worker.start();
			workers.add(worker);
		}
	}

	public void close() {
		logger.info("Closing WorkerPool");
		for (Worker worker : workers) {
			worker.close();
		}
	}
}
