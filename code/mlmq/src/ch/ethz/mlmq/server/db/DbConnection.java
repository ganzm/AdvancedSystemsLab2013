package ch.ethz.mlmq.server.db;

import java.io.Closeable;
import java.io.IOException;
import java.sql.Connection;

import ch.ethz.mlmq.server.db.dao.QueueDao;

/**
 * Wrapper for a database connection used by the workers of the broker
 */
public class DbConnection implements Closeable {

	private Connection connection = null;

	private QueueDao queueDao = null;

	public DbConnection() {

		// this.connection = DriverManager.getConnection(url, userName, password);

	}

	@Override
	public void close() throws IOException {
		// TODO Auto-generated method stub

	}

}
