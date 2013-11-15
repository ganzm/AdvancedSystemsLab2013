package ch.ethz.mlmq.test.db;

import java.io.IOException;
import java.sql.SQLException;

import org.junit.BeforeClass;
import org.junit.Test;

import ch.ethz.mlmq.logging.LoggerUtil;
import ch.ethz.mlmq.server.BrokerConfiguration;
import ch.ethz.mlmq.server.db.util.DatabaseInitializer;

public class DatabaseSetupTest {

	private static BrokerConfiguration config;

	@BeforeClass
	public static void beforeClass() throws IOException {
		LoggerUtil.initConsoleDebug();

		config = BrokerConfiguration.loadFromJar("brokerconfig.properties");
	}

	@Test
	public void testSetup() throws SQLException {

		String dbName = "mlmqUnitTest" + System.nanoTime();
		DatabaseInitializer dbInitializer = new DatabaseInitializer(config.getDbUrl(), config.getDbUserName(), config.getDbPassword(), dbName);

		dbInitializer.connect();
		dbInitializer.createDatabase();
		dbInitializer.createTables();
		dbInitializer.deleteDatabase();
	}
}
