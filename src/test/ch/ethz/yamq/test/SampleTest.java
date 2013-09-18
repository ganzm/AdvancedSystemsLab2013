package ch.ethz.yamq.test;

import java.sql.Connection;
import java.sql.DriverManager;

import org.junit.Assert;
import org.junit.Test;

public class SampleTest {

	@Test
	public void testDbConnection() throws Exception {

		Class.forName("org.postgresql.Driver");
		Connection connection = null;
		connection = DriverManager.getConnection(
				"jdbc:postgresql://localhost:5432/dbname", "username",
				"password");
		connection.close();

	}

}
