package ch.ethz.mlmq.server.db.dao;

import java.io.Closeable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

import ch.ethz.mlmq.dto.ClientDto;
import ch.ethz.mlmq.dto.MessageDto;
import ch.ethz.mlmq.dto.MessageQueryInfoDto;
import ch.ethz.mlmq.dto.QueueDto;
import ch.ethz.mlmq.logging.LoggerUtil;
import ch.ethz.mlmq.net.request.SendMessageRequest;
import ch.ethz.mlmq.server.ClientApplicationContext;

public class MessageDao implements Closeable {

	private static final Logger logger = Logger.getLogger(MessageDao.class.getSimpleName());

	private PreparedStatement insertMessageStmt;
	private PreparedStatement peekMessageStmt;
	private PreparedStatement deleteMessageStmt;

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

		String peekMessageSqlStmt = "SELECT id, queue_id, client_sender_id, content, prio,sent_at FROM peekMessage(?, ?, ?)";
		peekMessageStmt = connection.prepareStatement(peekMessageSqlStmt);

		String deleteMessageSqlStmt = "DELETE FROM message WHERE id = ?";
		deleteMessageStmt = connection.prepareStatement(deleteMessageSqlStmt);

	}

	public void close() {
		try {
			insertMessageStmt.close();
		} catch (SQLException e) {
			logger.severe("Error while closing insertMessageStmt" + LoggerUtil.getStackTraceString(e));
		}

		try {
			peekMessageStmt.close();
		} catch (SQLException e) {
			logger.severe("Error while closing peekMessageStmt" + LoggerUtil.getStackTraceString(e));
		}
		try {
			deleteMessageStmt.close();
		} catch (SQLException e) {
			logger.severe("Error while closing deleteMessageStmt" + LoggerUtil.getStackTraceString(e));
		}
	}

	public void insertMessage(SendMessageRequest request, ClientApplicationContext clientContext) throws SQLException {
		insertMessageStmt.setLong(1, request.getQueueId());
		insertMessageStmt.setLong(2, clientContext.getClientId());
		insertMessageStmt.setBytes(3, request.getContent());
		insertMessageStmt.setInt(4, request.getPrio());

		insertMessageStmt.execute();
	}

	public MessageDto dequeueMessage(MessageQueryInfoDto queryInfo) throws SQLException {
		MessageDto message = peekMessage(queryInfo);
		if (message == null) {
			return null;
		}

		deleteMessageStmt.setLong(1, message.getId());
		int result = deleteMessageStmt.executeUpdate();

		if (result != 1) {
			throw new SQLException("Dequeue did not work - Number of dequeued Messages " + result);
		}

		return message;
	}

	public MessageDto peekMessage(MessageQueryInfoDto queryInfo) throws SQLException {

		peekMessageStmt.setInt(1, (int) queryInfo.getQueue().getId());
		peekMessageStmt.setInt(2, (int) queryInfo.getSender().getId());
		peekMessageStmt.setBoolean(3, queryInfo.shouldOrderByPriority());

		try (ResultSet rs = peekMessageStmt.executeQuery()) {
			if (rs.next()) {
				MessageDto message = new MessageDto();

				message.setId(rs.getLong(1));
				message.setQueue(new QueueDto(rs.getLong(2)));
				message.setSender(new ClientDto(rs.getLong(3)));
				message.setContent(rs.getBytes(4));
				message.setPrio(rs.getInt(5));

				return message;
			}
		}

		return null;
	}
}
