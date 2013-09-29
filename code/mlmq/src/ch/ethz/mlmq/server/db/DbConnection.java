package ch.ethz.mlmq.server.db;

import java.io.Closeable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;

import ch.ethz.mlmq.logging.LoggerUtil;
import ch.ethz.mlmq.server.db.dao.QueueDao;

/**
 * Wrapper for a database connection used by the workers of the broker
 */
public class DbConnection implements Closeable {

	private Logger logger = Logger.getLogger("DbConnection");

	private String url;
	private String userName;
	private String password;

	private Connection connection = null;

	private QueueDao queueDao = null;

	public DbConnection(String url, String userName, String password) {
		this.url = url;
		this.userName = userName;
		this.password = password;
	}

	public void init() throws SQLException {
		this.connection = DriverManager.getConnection(url, userName, password);

		queueDao = new QueueDao();
		queueDao.init(connection);
	}

	@Override
	public void close() {

		logger.info("Closing DAO");
		queueDao.close();

		try {
			logger.info("Closing DbConnection");
			connection.close();
		} catch (SQLException e) {
			logger.severe("Errorwhile closing DBConnection " + LoggerUtil.getStackTraceString(e));
		}
	}
}
