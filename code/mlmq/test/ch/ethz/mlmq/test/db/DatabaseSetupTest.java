package ch.ethz.mlmq.test.db;

import java.sql.SQLException;

import org.junit.Test;

import ch.ethz.mlmq.db.DatabaseInitializer;

public class DatabaseSetupTest {

	private String userName = "postgres";
	private String password = "postgres";

	private String url = "jdbc:postgresql://localhost:5432";

	@Test
	public void testSetup() throws SQLException {
		DatabaseInitializer dbInitializer = new DatabaseInitializer(url, userName, password, "mlmqUnitTest" + System.currentTimeMillis());

		dbInitializer.connect();
		dbInitializer.createSchema();
		dbInitializer.deleteSchema();
	}
}
