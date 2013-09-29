package ch.ethz.mlmq.test;

import java.sql.Connection;
import java.sql.DriverManager;

import org.junit.Test;

import ch.ethz.mlmq.server.BrokerConfiguration;

public class DBConnectionTest {

	private String userName;
	private String password;
	private String url;

	@Test
	public void testDbConnection() throws Exception {

		BrokerConfiguration config = BrokerConfiguration.load("unittestconfig.properties");

		userName = config.getDbUserName();
		password = config.getDbPassword();
		url = config.getDbUrl();

		Class.forName("org.postgresql.Driver");
		Connection connection = DriverManager.getConnection(url, userName, password);
		connection.close();

	}

}
