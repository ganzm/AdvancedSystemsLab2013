package ch.ethz.mlmq.test.processing;

import java.io.IOException;
import java.sql.SQLException;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import ch.ethz.mlmq.exception.MlmqException;
import ch.ethz.mlmq.logging.LoggerUtil;
import ch.ethz.mlmq.net.request.RegistrationRequest;
import ch.ethz.mlmq.net.request.Request;
import ch.ethz.mlmq.server.BrokerConfiguration;
import ch.ethz.mlmq.server.db.DbConnectionPool;
import ch.ethz.mlmq.server.db.util.DatabaseInitializer;
import ch.ethz.mlmq.server.processing.RequestProcessor;

public class RequestProcessorTest {

	private static BrokerConfiguration config;

	private RequestProcessor processor;

	private static DatabaseInitializer dbInitializer;
	private static String dbName = "mlmqunittest" + System.currentTimeMillis();
	private static DbConnectionPool pool = new DbConnectionPool(1);

	@BeforeClass
	public static void beforeClass() throws IOException, SQLException {
		LoggerUtil.initConsoleDebug();

		config = BrokerConfiguration.load("unittestconfig.example.properties");

		dbInitializer = new DatabaseInitializer(config.getDbUrl(), config.getDbUserName(), config.getDbPassword(), dbName);

		dbInitializer.connect();
		dbInitializer.createDatabase();
		dbInitializer.createTables();

		pool.init();
	}

	@AfterClass
	public static void afterClass() throws SQLException {
		pool.close();

		dbInitializer.deleteDatabase();
	}

	@Before
	public void before() {
		processor = new RequestProcessor();
	}

	@Test
	public void testRegistrationRequest() throws MlmqException {
		Request request = new RegistrationRequest();
		processor.process(1, request, pool);
	}
}
