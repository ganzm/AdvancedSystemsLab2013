package ch.ethz.mlmq.server.db.dao;

import java.io.Closeable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

import ch.ethz.mlmq.logging.LoggerUtil;
import ch.ethz.mlmq.logging.PerformanceLogger;
import ch.ethz.mlmq.logging.PerformanceLoggerManager;

public class ClientDao implements Closeable {
	private static final Logger logger = Logger.getLogger(ClientDao.class.getSimpleName());

	private final PerformanceLogger perfLog = PerformanceLoggerManager.getLogger();

	private PreparedStatement insertNewClientStmt;
	private PreparedStatement getClientByNameStatement;

	public ClientDao() {

	}

	public void init(Connection connection) throws SQLException {

		// prepare statements
		insertNewClientStmt = connection.prepareStatement("SELECT createClient(?)");
		getClientByNameStatement = connection.prepareStatement("SELECT id, name FROM client where name = ?");
	}

	public void close() {
		try {
			insertNewClientStmt.close();
		} catch (SQLException e) {
			logger.severe("Error while closing insertNewClientStmt" + LoggerUtil.getStackTraceString(e));
		}

		try {
			getClientByNameStatement.close();
		} catch (SQLException e) {
			logger.severe("Error while closing getClientByNameStatement" + LoggerUtil.getStackTraceString(e));
		}
	}

	public Integer getClientId(String clientName) throws SQLException {
		long startTime = System.currentTimeMillis();

		getClientByNameStatement.setString(1, clientName);

		try (ResultSet rs = getClientByNameStatement.executeQuery()) {
			if (rs.next()) {
				return rs.getInt(1);
			}

			return null;
		} finally {
			perfLog.log(System.currentTimeMillis() - startTime, "BDb#getClientId");
		}
	}

	public int insertNewClient(String name) throws SQLException {
		long startTime = System.currentTimeMillis();

		insertNewClientStmt.setString(1, name);

		try (ResultSet rs = insertNewClientStmt.executeQuery()) {
			if (!rs.next()) {
				throw new SQLException("Expected single column result from " + insertNewClientStmt);
			}

			int clientId = rs.getInt(1);
			return clientId;
		} finally {
			perfLog.log(System.currentTimeMillis() - startTime, "BDb#insertNewClient");
		}
	}
}
