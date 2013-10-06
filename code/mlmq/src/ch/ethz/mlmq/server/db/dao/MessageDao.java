package ch.ethz.mlmq.server.db.dao;

import java.io.Closeable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Logger;

import ch.ethz.mlmq.logging.LoggerUtil;
import ch.ethz.mlmq.net.request.DequeueMessageRequest;
import ch.ethz.mlmq.net.request.SendMessageRequest;
import ch.ethz.mlmq.net.response.Response;
import ch.ethz.mlmq.server.ClientApplicationContext;

public class MessageDao implements Closeable {

	private static final Logger logger = Logger.getLogger(MessageDao.class.getSimpleName());

	private PreparedStatement insertMessageStmt;

	public MessageDao() {

	}

	public void init(Connection connection) throws SQLException {

		// prepare statements
		//@formatter:off
		String insertSqlStatement = "INSERT INTO message("
				+ "queue_id, client_sender_id, content, prio)"
				+ "VALUES (?, ?, ?, ?)";
		//@formatter:on
		insertMessageStmt = connection.prepareStatement(insertSqlStatement);

	}

	public void close() {
		try {
			insertMessageStmt.close();
		} catch (SQLException e) {
			logger.severe("Error while closing insertMessageStmt" + LoggerUtil.getStackTraceString(e));
		}
	}

	public void insertMessage(SendMessageRequest request, ClientApplicationContext clientContext) throws SQLException {
		insertMessageStmt.setLong(1, request.getQueueId());
		insertMessageStmt.setLong(2, clientContext.getClientId());
		insertMessageStmt.setBytes(3, request.getContent());
		insertMessageStmt.setInt(4, request.getPrio());

		insertMessageStmt.execute();
	}

	public Response dequeueMessage(DequeueMessageRequest request) throws SQLException {
		throw new RuntimeException("TODO");
	}
}
