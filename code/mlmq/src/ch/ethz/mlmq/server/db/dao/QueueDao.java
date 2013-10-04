package ch.ethz.mlmq.server.db.dao;

import java.io.Closeable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.logging.Logger;

import ch.ethz.mlmq.dto.ClientDto;
import ch.ethz.mlmq.dto.QueueDto;
import ch.ethz.mlmq.logging.LoggerUtil;

public class QueueDao implements Closeable {

	private static final Logger logger = Logger.getLogger(QueueDao.class.getSimpleName());

	private PreparedStatement createQueueStmt;
	private PreparedStatement deleteQueueStmt;

	public QueueDao() {

	}

	public void init(Connection connection) throws SQLException {

		// prepare statements
		createQueueStmt = connection.prepareStatement("SELECT createQueue(?)");

		deleteQueueStmt = connection.prepareStatement("DELETE FROM queue WHERE id = ?");
	}

	public void close() {
		try {
			createQueueStmt.close();
		} catch (SQLException e) {
			logger.severe("Error while closing createQueueStmt" + LoggerUtil.getStackTraceString(e));
		}
	}

	public QueueDto createQueue() throws SQLException {
		return createClientQueue(null);
	}

	public QueueDto createClientQueue(ClientDto client) throws SQLException {

		if (client == null) {
			createQueueStmt.setNull(1, Types.INTEGER);
		} else {
			createQueueStmt.setLong(1, client.getId());
		}

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
	public void deleteQueue(QueueDto queueToDelete) throws SQLException {
		deleteQueueStmt.setLong(1, queueToDelete.getId());
		deleteQueueStmt.execute();
	}
}
