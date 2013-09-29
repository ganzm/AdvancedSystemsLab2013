package ch.ethz.mlmq.test.db;

import java.sql.Connection;
import java.sql.DriverManager;

import org.junit.Test;

import ch.ethz.mlmq.server.BrokerConfiguration;

public class DBConnectionTest {

	@Test
	public void testDbConnection() throws Exception {

		BrokerConfiguration config = BrokerConfiguration.load("unittestconfig.properties");

		String userName = config.getDbUserName();
		String password = config.getDbPassword();
		String url = config.getDbUrl();

		Class.forName("org.postgresql.Driver");
		Connection connection = DriverManager.getConnection(url, userName, password);
		connection.close();

	}

}
