package ch.ethz.mlmq.server.db;

import java.sql.SQLException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.logging.Logger;

import ch.ethz.mlmq.exception.MlmqException;
import ch.ethz.mlmq.exception.OutOfConnectionsException;
import ch.ethz.mlmq.server.BrokerConfiguration;

/**
 * Keeps a list of opened connections to the database
 * 
 * This class may be accessed from multiple worker threads an needs to be thread safe
 */
public class DbConnectionPool {

	private final Logger logger = Logger.getGlobal();

	private final ArrayBlockingQueue<DbConnection> connections;

	private String url;

	private String userName;

	private String password;

	private int poolSize;

	private int nrOfMissingConnections;

	private final Object lock = new Object();

	public DbConnectionPool(int poolSize, String url, String userName, String password) {
		connections = new ArrayBlockingQueue<DbConnection>(poolSize);
		this.poolSize = poolSize;
		this.url = url;
		this.userName = userName;
		this.password = password;
	}

	public DbConnectionPool(BrokerConfiguration config) {
		this(config.getDbConnectionPoolSize(), config.getDbUrlWithDbName(), config.getDbUserName(), config.getDbPassword());
	}

	public void init() throws MlmqException {
		logger.info("Init DbConnectionPool with " + poolSize + " connections");
		try {
			for (int i = 0; i < poolSize; i++) {
				connections.add(createConnection());
			}
		} catch (SQLException e) {
			throw new MlmqException("Error while initializing DbConnection from Pool", e);
		}
	}

	private DbConnection createConnection() throws SQLException {
		DbConnection dbConnection = new DbConnection(url, userName, password);
		dbConnection.init();
		return dbConnection;
	}

	/**
	 * Retrieves a connection from the pool
	 * 
	 * This method may be accessed concurrent
	 * 
	 * @return
	 * @throws OutOfConnectionsException
	 */
	public DbConnection getConnection() throws OutOfConnectionsException {
		DbConnection connection = connections.poll();
		if (connection != null) {
			return connection;
		}

		if (nrOfMissingConnections > 0) {
			try {
				connection = createConnection();

				synchronized (lock) {
					if (nrOfMissingConnections > 0) {
						nrOfMissingConnections--;
						return connection;
					}
					// else - there was another thread which called getConnection at the "same" moment
					// connection was created but should not be given away because connection pool size will be exceeded
					// throw OutOfConnectinosException instead
				}
			} catch (SQLException e) {
				throw new OutOfConnectionsException("No more connections available - Could not connect ot database", e);
			}
		}

		throw new OutOfConnectionsException("No more connections available - try again later" + poolSize);
	}

	/**
	 * Puts a connection back into the pool
	 * 
	 * This method may be accessed concurrent
	 * 
	 * @throws MlmqException
	 */
	public void returnConnection(DbConnection connection) throws MlmqException {
		if (connection.isClosed()) {
			logger.info("Closed connection was returned, create a new one");

			try {
				connections.add(createConnection());
			} catch (SQLException e) {
				nrOfMissingConnections++;
				throw new MlmqException("Exception while creating a new connection");
			} catch (IllegalStateException e) {
				throw new MlmqException("Internal PoolException - too many connections returned", e);
			}
		} else {
			connections.add(connection);
		}
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
