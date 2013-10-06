package ch.ethz.mlmq.server.db;

import java.io.Closeable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;

import ch.ethz.mlmq.logging.LoggerUtil;
import ch.ethz.mlmq.server.db.dao.ClientDao;
import ch.ethz.mlmq.server.db.dao.MessageDao;
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

	private boolean closed = true;

	private QueueDao queueDao = null;
	private MessageDao messageDao = null;
	private ClientDao clientDao = null;

	public DbConnection(String url, String userName, String password) {
		this.url = url;
		this.userName = userName;
		this.password = password;
	}

	public void init() throws SQLException {
		this.connection = DriverManager.getConnection(url, userName, password);
		closed = false;

		queueDao = new QueueDao();
		queueDao.init(connection);

		messageDao = new MessageDao();
		messageDao.init(connection);

		clientDao = new ClientDao();
		clientDao.init(connection);
	}

	@Override
	public void close() {
		logger.info("Closing DAO");
		queueDao.close();
		messageDao.close();
		clientDao.close();

		try {
			logger.info("Closing DbConnection");
			connection.close();
		} catch (SQLException e) {
			logger.severe("Errorwhile closing DBConnection " + LoggerUtil.getStackTraceString(e));
		} finally {
			closed = true;
		}
	}

	public boolean isClosed() {
		return closed;
	}

	public QueueDao getQueueDao() {
		return queueDao;
	}

	public MessageDao getMessageDao() {
		return messageDao;
	}

	public ClientDao getClientDao() {
		return clientDao;
	}
}
