package ch.ethz.mlmq.test.db;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import ch.ethz.mlmq.logging.LoggerUtil;
import ch.ethz.mlmq.logging.PerformanceLoggerManager;
import ch.ethz.mlmq.server.BrokerConfiguration;
import ch.ethz.mlmq.server.db.util.DatabaseInitializer;

public class ConcurrentReadTest {

	private static BrokerConfiguration config;

	private static final Logger logger = Logger.getLogger("DaoTest");

	private static DatabaseInitializer dbInitializer;
	private static String dbName = "mlmqunittest" + System.currentTimeMillis();

	private Connection connection1;
	private Connection connection2;

	@BeforeClass
	public static void beforeClass() throws IOException, SQLException {
		PerformanceLoggerManager.configureDisabled();
		LoggerUtil.initConsoleDebug();

		config = BrokerConfiguration.loadFromJar("brokerconfig.properties");

		dbInitializer = new DatabaseInitializer(config.getDbUrl(), config.getDbUserName(), config.getDbPassword(), dbName);

		dbInitializer.connect();
		dbInitializer.createDatabase();
		dbInitializer.createTables();
	}

	@AfterClass
	public static void afterClass() throws SQLException {
		dbInitializer.deleteDatabase();
	}

	@Before
	public void before() throws SQLException {
		String url = config.getDbUrl() + "/" + dbName;
		String userName = config.getDbUserName();
		String password = config.getDbPassword();

		connection1 = DriverManager.getConnection(url, userName, password);
		connection2 = DriverManager.getConnection(url, userName, password);

	}

	@After
	public void after() throws SQLException {
		connection1.close();
		connection2.close();
	}

	@Ignore
	@Test
	public void testConcurrent() throws SQLException {
		System.out.println("=============================================");
		System.out.println("Test Concurrent");

		Statement stmt = connection1.createStatement();
		stmt.execute("INSERT INTO client(id, name) VALUES (1, 'test')");
		stmt.execute("INSERT INTO queue(id, name, client_id) VALUES (1, 'queue', 1)");
		stmt.execute("INSERT INTO message(id, queue_id, client_sender_id, prio )" + "VALUES (1,1, 1, 10);");
		stmt.execute("INSERT INTO message(id, queue_id, client_sender_id, prio )" + "VALUES (2,1, 1, 8);");

		System.out.println("Initial data is there");

		Runnable r1 = new Runnable() {

			@Override
			public void run() {
				try {
					selectAndDelete("Conn1", connection1, true);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		};

		Runnable r2 = new Runnable() {

			@Override
			public void run() {
				try {
					selectAndDelete("Conn2", connection2, true);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		};

		new Thread(r1).start();
		new Thread(r2).start();

		try {
			Thread.sleep(20000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void selectAndDelete(String myName, Connection connection, boolean isFirst) throws SQLException {
		// connection.setAutoCommit(false);
		try (Statement stmt2 = connection.createStatement()) {

			stmt2.execute("BEGIN TRANSACTION");

			System.out.println(myName + " Transaction opened");
			Thread.sleep(1000);

			if (!isFirst) {
				Thread.sleep(1000);
			}
			int id = -1;
			System.out.println(myName + " before Select");
			try (ResultSet rs = stmt2.executeQuery("Select * From message ORDER BY id LIMIT 1 FOR UPDATE")) {
				while (rs.next()) {
					id = rs.getInt(1);
					System.out.println(myName + " Select Message Id " + rs.getInt(1));
				}
			}

			System.out.println(myName + " after Select");
			if (isFirst) {
				Thread.sleep(2000);
			}

			System.out.println(myName + " befor Delete ");
			int result = stmt2.executeUpdate("DELETE FROM message WHERE id = " + id);
			System.out.println(myName + " after Delete with Result " + result);
			stmt2.execute("COMMIT TRANSACTION");
			System.out.println(myName + " after COMMIT");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
