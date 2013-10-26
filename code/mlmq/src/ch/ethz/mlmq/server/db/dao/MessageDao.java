package ch.ethz.mlmq.server.db.dao;

import java.io.Closeable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
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
	private PreparedStatement generateNewConversationContextStmt;
	private PreparedStatement getPublicQueuesContainingMessagesStmt;
	private PreparedStatement getNumMsgPerQueueStmt;

	public MessageDao() {

	}

	public void init(Connection connection) throws SQLException {
		// prepare statements

		//@formatter:off
		String insertSqlStatement = "INSERT INTO message("
				+ "queue_id, client_sender_id, content, prio, context)"
				+ "VALUES (?, ?, ?, ?, ?)";
		//@formatter:on
		insertMessageStmt = connection.prepareStatement(insertSqlStatement);

		String peekMessageSqlStmt = "SELECT id, queue_id, client_sender_id, content, prio, sent_at, context FROM peekMessage(?, ?, ?, ?)";
		peekMessageStmt = connection.prepareStatement(peekMessageSqlStmt);

		String deleteMessageSqlStmt = "DELETE FROM message WHERE id = ?";
		deleteMessageStmt = connection.prepareStatement(deleteMessageSqlStmt);

		generateNewConversationContextStmt = connection.prepareStatement("SELECT nextval('message_context')");

		String getNumMsgPerQueueSqlStmt = "SELECT count(*) FROM message WHERE queue_id = ?";
		getNumMsgPerQueueStmt = connection.prepareStatement(getNumMsgPerQueueSqlStmt);

		//@formatter:off
		String getPublicQueuesContainingMessagesSqlStmt = ""
				+ "SELECT * FROM queue q "
				+ "WHERE client_id IS NULL "
				+ "AND EXISTS (SELECT 1 FROM message WHERE queue_id = q.id LIMIT 1) "
				+ "LIMIT ?";
		//@formatter:on
		getPublicQueuesContainingMessagesStmt = connection.prepareStatement(getPublicQueuesContainingMessagesSqlStmt);

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

		try {
			generateNewConversationContextStmt.close();
		} catch (SQLException e) {
			logger.severe("Error while closing generateNewConversationContextStmt" + LoggerUtil.getStackTraceString(e));
		}

		try {
			getNumMsgPerQueueStmt.close();
		} catch (SQLException e) {
			logger.severe("Error while closing getNumMsgPerQueueStmt" + LoggerUtil.getStackTraceString(e));
		}

		try {
			getPublicQueuesContainingMessagesStmt.close();
		} catch (SQLException e) {
			logger.severe("Error while closing getPublicQueuesContainingMessagesStmt" + LoggerUtil.getStackTraceString(e));
		}
	}

	public void insertMessage(SendMessageRequest request, ClientApplicationContext clientContext) throws SQLException {

		for (long queueId : request.getQueueIds()) {
			insertMessageStmt.setLong(1, queueId);
			insertMessageStmt.setLong(2, clientContext.getClient().getId());
			insertMessageStmt.setBytes(3, request.getContent());
			insertMessageStmt.setInt(4, request.getPrio());
			insertMessageStmt.setNull(5, Types.INTEGER);

			insertMessageStmt.execute();
		}

	}

	public void insertMessage(long queueId, long clientId, byte[] content, int prio, Long clientContext) throws SQLException {
		insertMessageStmt.setLong(1, queueId);
		insertMessageStmt.setLong(2, clientId);
		insertMessageStmt.setBytes(3, content);
		insertMessageStmt.setInt(4, prio);

		if (clientContext == null) {
			insertMessageStmt.setNull(5, Types.INTEGER);
		} else {
			insertMessageStmt.setLong(5, clientContext);
		}

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

		if (queryInfo.getQueue() == null) {
			peekMessageStmt.setNull(1, Types.INTEGER);
		} else {
			peekMessageStmt.setInt(1, (int) queryInfo.getQueue().getId());
		}

		if (queryInfo.getSender() == null) {
			peekMessageStmt.setNull(2, Types.INTEGER);
		} else {
			peekMessageStmt.setInt(2, (int) queryInfo.getSender().getId());
		}

		peekMessageStmt.setBoolean(3, queryInfo.shouldOrderByPriority());

		Integer expectedContext = queryInfo.getConversationContext();
		if (expectedContext == null) {
			peekMessageStmt.setNull(4, Types.INTEGER);
		} else {
			peekMessageStmt.setInt(4, ((int) (long) queryInfo.getConversationContext()));
		}

		try (ResultSet rs = peekMessageStmt.executeQuery()) {
			if (rs.next()) {
				MessageDto message = new MessageDto();

				message.setId(rs.getLong(1));
				message.setQueue(new QueueDto(rs.getLong(2)));
				message.setSender(new ClientDto(rs.getLong(3)));
				message.setContent(rs.getBytes(4));
				message.setPrio(rs.getInt(5));

				int context = rs.getInt(7);
				if (!rs.wasNull()) {
					message.setConversationContext(context);
				}

				return message;
			}
		}

		return null;
	}

	public long generateNewConversationContext() throws SQLException {
		try (ResultSet rs = generateNewConversationContextStmt.executeQuery()) {
			if (rs.next()) {
				return rs.getLong(1);
			}
			throw new SQLException("No Value found for generateNewConversationContext");
		}
	}

	/**
	 * 
	 * @param maxNumQueues
	 *            constrains the maximum results returned
	 * @return
	 * @throws SQLException
	 */
	public List<QueueDto> getPublicQueuesContainingMessages(int maxNumQueues) throws SQLException {
		List<QueueDto> result = new ArrayList<>();

		getPublicQueuesContainingMessagesStmt.setInt(1, maxNumQueues);
		try (ResultSet rs = getPublicQueuesContainingMessagesStmt.executeQuery()) {
			while (rs.next()) {
				long queueId = rs.getLong(1);
				QueueDto queue = new QueueDto(queueId);
				result.add(queue);
			}
		}

		return result;
	}

	public int getNumberOfMessages(long queueId) throws SQLException {
		getNumMsgPerQueueStmt.setLong(1, queueId);
		try (ResultSet rs = getNumMsgPerQueueStmt.executeQuery()) {
			if (rs.next()) {
				return rs.getInt(1);
			}
			throw new SQLException("No Value found for getNumMsgPerQueueStmt");
		}
	}
}
