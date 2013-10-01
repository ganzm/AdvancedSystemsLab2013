package ch.ethz.mlmq.server;

import java.util.logging.Logger;

import ch.ethz.mlmq.nio.BrokerNetworkInterface;
import ch.ethz.mlmq.server.db.DbConnectionPool;
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

	private WorkerTaskQueue requestQueue;

	public Broker(BrokerConfiguration config) {
		this.config = config;
		this.requestQueue = new WorkerTaskQueueImpl();
	}

	public void startup() {
		logger.info("Starting broker...");

		connectionPool = new DbConnectionPool(config.getDbConnectionPoolSize());
		connectionPool.init();

		networkInterface = new BrokerNetworkInterface(config, requestQueue);

		logger.info("Broker started");
	}

	public void shutdown() {

		networkInterface.close();

		connectionPool.close();

	}

}
