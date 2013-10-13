package ch.ethz.mlmq.test.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.logging.Logger;

import org.junit.Test;

import ch.ethz.mlmq.server.BrokerConfiguration;

public class DBConnectionTest {

	private static final Logger logger = Logger.getLogger(DBConnectionTest.class.getSimpleName());

	@Test
	public void testDbConnection() throws Exception {

		BrokerConfiguration config = BrokerConfiguration.loadFromJar("brokerconfig.properties");

		String userName = config.getDbUserName();
		String password = config.getDbPassword();
		String url = config.getDbUrl();

		Class.forName("org.postgresql.Driver");
		logger.info("Connecting to URL [" + url + "] with username [" + userName + "] und password [" + password + "]");
		Connection connection = DriverManager.getConnection(url + "/postgres", userName, password);
		connection.close();

	}

}
