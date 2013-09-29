package ch.ethz.mlmq.test.db;

import java.sql.SQLException;

import org.junit.BeforeClass;
import org.junit.Test;

import ch.ethz.mlmq.logging.LoggerUtil;
import ch.ethz.mlmq.server.db.util.DatabaseInitializer;

public class DatabaseSetupTest {

	private String userName = "postgres";
	private String password = "postgres";

	private String url = "jdbc:postgresql://localhost:5432";

	@BeforeClass
	public static void beforeClass() {
		LoggerUtil.initConsoleDebug();
	}

	@Test
	public void testSetup() throws SQLException {
		DatabaseInitializer dbInitializer = new DatabaseInitializer(url, userName, password, "mlmqUnitTest" + System.currentTimeMillis());

		dbInitializer.connect();
		dbInitializer.createDatabase();
		dbInitializer.createTables();
		dbInitializer.deleteDatabase();
	}
}
