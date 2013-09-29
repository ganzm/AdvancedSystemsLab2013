package ch.ethz.mlmq.test.db;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import ch.ethz.mlmq.dto.QueueDto;
import ch.ethz.mlmq.logging.LoggerUtil;
import ch.ethz.mlmq.server.BrokerConfiguration;
import ch.ethz.mlmq.server.db.dao.QueueDao;

public class DaoTest {

	private static BrokerConfiguration config;

	private static final Logger logger = Logger.getLogger("DaoTest");

	@BeforeClass
	public static void beforeClass() throws IOException {
		LoggerUtil.initConsoleDebug();

		config = BrokerConfiguration.load("unittestconfig.properties");
	}

	@Test
	public void testQueueDao() throws SQLException {

		String url = config.getDbUrlWithDbName();
		String userName = config.getDbUserName();
		String password = config.getDbPassword();

		try (Connection connection = DriverManager.getConnection(url, userName, password); QueueDao queueDao = new QueueDao();) {

			queueDao.init(connection);

			int beforeCreate = getQueueCount(connection);
			QueueDto queue = queueDao.createQueue();
			int afterCreate = getQueueCount(connection);

			Assert.assertNotNull(queue);
			logger.info("Created Queue with id " + queue.getId());

			queueDao.deleteQueue(queue);
			int afterDelete = getQueueCount(connection);

			Assert.assertEquals(beforeCreate + 1, afterCreate);
			Assert.assertEquals(beforeCreate, afterDelete);
		}
	}

	private int getQueueCount(Connection connection) throws SQLException {
		try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM queue");) {
			Assert.assertTrue(rs.next());
			return rs.getInt(1);
		}
	}
}
