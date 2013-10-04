package ch.ethz.mlmq.server;

import java.util.logging.Logger;

import ch.ethz.mlmq.nio.BrokerNetworkInterface;
import ch.ethz.mlmq.server.db.DbConnectionPool;
import ch.ethz.mlmq.server.processing.WorkerPool;
import ch.ethz.mlmq.server.processing.WorkerTaskQueue;
import ch.ethz.mlmq.server.processing.WorkerTaskQueueImpl;

/**
 * Thats where we put all our server context to
 * 
 * represents a single broker instance
 */
public class Broker {

	private final Logger logger = Logger.getLogger(Broker.class.getSimpleName());

	private final BrokerConfiguration config;

	private BrokerNetworkInterface networkInterface;

	private DbConnectionPool connectionPool;

	private WorkerTaskQueueImpl requestQueue;

	private WorkerPool workerPool;

	public Broker(BrokerConfiguration config) {
		this.config = config;
		this.requestQueue = new WorkerTaskQueueImpl(config.getRequestQueueSize());
	}

	public void startup() {
		logger.info("Starting broker...");

		logger.info("Starting ConnectionPool");
		connectionPool = new DbConnectionPool(config.getDbConnectionPoolSize());
		connectionPool.init();

		logger.info("Starting Networking");
		networkInterface = new BrokerNetworkInterface(config, requestQueue);
		networkInterface.init();

		WorkerTaskQueue responseQueue = networkInterface.getResponseQueue();

		logger.info("Starting WorkerPool");
		workerPool = new WorkerPool(config, requestQueue, responseQueue, connectionPool);
		workerPool.init();

		logger.info("Broker started");
	}

	public void shutdown() {

		logger.info("Shutdown Broker");
		networkInterface.close();

		logger.info("Shutdown WorkerPool");
		workerPool.close();

		logger.info("Shutdown ConnectionPool");
		connectionPool.close();

	}
}
