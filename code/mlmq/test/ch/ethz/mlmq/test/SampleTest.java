package ch.ethz.mlmq.test;

import java.sql.Connection;
import java.sql.DriverManager;

import org.junit.Assert;
import org.junit.Test;

public class SampleTest {
	private String userName = "postgres";
	private String password = "postgres";

	@Test
	public void testDbConnection() throws Exception {

		Class.forName("org.postgresql.Driver");
		Connection connection = null;
		connection = DriverManager.getConnection(
				"jdbc:postgresql://localhost:5432/yams", userName, password);
		connection.close();

	}

}
