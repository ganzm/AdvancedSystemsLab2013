package ch.ethz.mlmq.server;

import java.util.logging.Logger;

import ch.ethz.mlmq.server.db.DbConnectionPool;

/**
 * Thats where we put all our server context to
 * 
 * represents a single broker instance
 */
public class Broker {

	private final Logger logger = Logger.getGlobal();

	private final BrokerConfiguration config;

	private BrokerNetworkInterface networkInterface;

	private DbConnectionPool connectionPool;

	public Broker(BrokerConfiguration config) {
		this.config = config;
	}

	public void startup() {
		logger.info("Starting broker...");

		connectionPool = new DbConnectionPool(config.getDbConnectionPoolSize());
		connectionPool.init();

		networkInterface = new BrokerNetworkInterface(config);

		logger.info("Broker started");
	}

	public void shutdown() {

		networkInterface.close();

		connectionPool.close();

	}

}
