package ch.ethz.mlmq.server.db;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.logging.Logger;

import ch.ethz.mlmq.exception.OutOfConnectionsException;

/**
 * Keeps a list of opened connections to the database
 * 
 * This class may be accessed from multiple worker threads an needs to be thread safe
 */
public class DbConnectionPool {

	private final Logger logger = Logger.getGlobal();

	private final ArrayBlockingQueue<DbConnection> connections;

	public DbConnectionPool(int poolSize) {
		connections = new ArrayBlockingQueue<DbConnection>(poolSize);
	}

	public void init() {
		logger.info("Init DbConnectionPool");
		// TODO
	}

	/**
	 * Retrieves a connection from the pool
	 * 
	 * @return
	 * @throws OutOfConnectionsException
	 */
	public DbConnection getConnection() throws OutOfConnectionsException {
		return connections.poll();
	}

	/**
	 * Puts a connection back into the pool
	 */
	public void returnConnection(DbConnection connection) {
		connections.add(connection);
	}

	/**
	 * Maximum number of pooled connections
	 * 
	 * @return
	 */
	public int getPoolSize() {
		return connections.size();
	}

	/**
	 * shuts down this pool and all it's contained connections
	 */
	public void close() {
		logger.info("Closing DbConnectionPool...");

		for (DbConnection conn : connections) {
			conn.close();
		}

		logger.info("DbConnectionPool closed");
	}
}
