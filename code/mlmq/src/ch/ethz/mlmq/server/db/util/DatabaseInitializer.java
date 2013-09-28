package ch.ethz.mlmq.server.db.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

/**
 * Helper to initialize, update and drop a Database Schema
 * 
 */
public class DatabaseInitializer {

	private static final Logger logger = Logger.getLogger("default");

	/**
	 * DB connection string for examle jdbc:postgresql://localhost:5432/
	 */
	private final String url;
	private final String userName;
	private final String password;
	private final String databaseName;

	/**
	 * This Initializer maintains it's own database connection
	 */
	private Connection connection = null;

	public DatabaseInitializer(String url, String userName, String password, String databaseName) {
		this.password = password;
		this.userName = userName;
		this.url = url;
		this.databaseName = databaseName.toLowerCase();
	}

	/**
	 * Establish a database connection
	 * 
	 * @throws SQLException
	 */
	public void connect() throws SQLException {
		try {
			Class.forName("org.postgresql.Driver");
			connection = DriverManager.getConnection(url, userName, password);
		} catch (ClassNotFoundException e) {
			throw new SQLException(e);
		}
	}

	public void disconnect() throws SQLException {
		if (connection != null) {
			connection.close();
		}
	}

	public void createDatabase() throws SQLException {
		//@formatter:off
		String sqlStatement = "" 
				+ " CREATE DATABASE " + databaseName 
				+ " WITH OWNER = " + userName
				+ " ENCODING = 'UTF8'"
				+ " TABLESPACE = pg_default"
				+ " LC_COLLATE = 'German_Switzerland.1252'"
				+ " LC_CTYPE = 'German_Switzerland.1252'"
				+ " CONNECTION LIMIT = -1;";
		//@formatter:on

		try (Statement stmt = connection.createStatement()) {
			logger.info("Executing " + sqlStatement);
			stmt.execute(sqlStatement);
		}
	}

	public void deleteDatabase() throws SQLException {
		//@formatter:off
		String sqlStatement = "" 
				+ " DROP DATABASE " + databaseName
				+ ";";
		//@formatter:on

		try (Statement stmt = connection.createStatement()) {
			logger.info("Executing " + sqlStatement);
			stmt.execute(sqlStatement);
		}
	}

	public void createTables() throws SQLException {
		String scriptFile = "db/001_table_create.sql";
		logger.info("Create Tables - exec " + scriptFile);

		String urlToDatabase = url + "/" + databaseName;
		logger.info("Connect to Database with URL: " + urlToDatabase);

		try (InputStream scriptStream = this.getClass().getClassLoader().getResourceAsStream(scriptFile);
				Connection createTableConnection = DriverManager.getConnection(urlToDatabase, userName, password);

		) {
			ScriptRunner runner = new ScriptRunner();
			runner.execute(scriptStream, createTableConnection);
		} catch (IOException e) {
			throw new SQLException("Eror reading " + scriptFile, e);
		}
	}
}
