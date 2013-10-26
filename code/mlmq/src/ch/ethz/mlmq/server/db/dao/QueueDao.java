package ch.ethz.mlmq.server.db.dao;

import java.io.Closeable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.logging.Logger;

import ch.ethz.mlmq.dto.QueueDto;
import ch.ethz.mlmq.logging.LoggerUtil;

public class QueueDao implements Closeable {

	private static final Logger logger = Logger.getLogger(QueueDao.class.getSimpleName());

	private PreparedStatement createQueueStmt;
	private PreparedStatement deleteQueueStmt;
	private PreparedStatement queryQueueByClientIdStmt;
	private PreparedStatement queryQueueByQueueNameStmt;

	public QueueDao() {

	}

	public void init(Connection connection) throws SQLException {

		// prepare statements
		createQueueStmt = connection.prepareStatement("SELECT createQueue(?, ?)");

		deleteQueueStmt = connection.prepareStatement("DELETE FROM queue WHERE id = ?");

		queryQueueByClientIdStmt = connection.prepareStatement("SELECT id, name FROM queue WHERE client_id = ?");

		queryQueueByQueueNameStmt = connection.prepareStatement("SELECT id, name FROM queue WHERE name = ?");
	}

	public void close() {
		try {
			createQueueStmt.close();
		} catch (SQLException e) {
			logger.severe("Error while closing createQueueStmt" + LoggerUtil.getStackTraceString(e));
		}

		try {
			deleteQueueStmt.close();
		} catch (SQLException e) {
			logger.severe("Error while closing deleteQueueStmt" + LoggerUtil.getStackTraceString(e));
		}

		try {
			queryQueueByClientIdStmt.close();
		} catch (SQLException e) {
			logger.severe("Error while closing queryQueueIdByClientIdStmt" + LoggerUtil.getStackTraceString(e));
		}

		try {
			queryQueueByQueueNameStmt.close();
		} catch (SQLException e) {
			logger.severe("Error while closing queryQueueByQueueNameStmt" + LoggerUtil.getStackTraceString(e));
		}
	}

	public QueueDto createQueue(String name) throws SQLException {
		return createClientQueue(null, name);
	}

	public QueueDto createClientQueue(Integer clientId, String name) throws SQLException {

		if (clientId == null) {
			createQueueStmt.setNull(1, Types.INTEGER);
		} else {
			createQueueStmt.setInt(1, clientId);
		}

		createQueueStmt.setString(2, name);

		try (ResultSet rs = createQueueStmt.executeQuery()) {
			if (!rs.next()) {
				throw new SQLException("Expected single column result from " + createQueueStmt);
			}

			int queueId = rs.getInt(1);
			return new QueueDto(queueId);
		}
	}

	/**
	 * Deletes a Queue on the database
	 * 
	 * @param queueToDelete
	 * @throws SQLException
	 */
	public void deleteQueue(long queueIdToDelete) throws SQLException {
		deleteQueueStmt.setLong(1, queueIdToDelete);
		deleteQueueStmt.execute();
	}

	public QueueDto getQueueByClientId(long clientId) throws SQLException {

		queryQueueByClientIdStmt.setLong(1, clientId);
		try (ResultSet rs = queryQueueByClientIdStmt.executeQuery()) {

			if (rs.next()) {
				long queueId = rs.getLong(1);
				String name = rs.getString(2);
				return new QueueDto(queueId, name);
			}

			throw new SQLException("ClientQueue not found for ClientId [" + clientId + "]");
		}
	}

	public QueueDto getQueueByName(String queueName) throws SQLException {
		queryQueueByQueueNameStmt.setString(1, queueName);
		try (ResultSet rs = queryQueueByQueueNameStmt.executeQuery()) {

			if (rs.next()) {
				long queueId = rs.getLong(1);
				String name = rs.getString(2);
				return new QueueDto(queueId, name);
			}

			throw new SQLException("Queue not found with Name [" + queueName + "]");
		}
	}
}
