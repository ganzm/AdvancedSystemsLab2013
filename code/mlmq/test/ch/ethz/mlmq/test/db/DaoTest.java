package ch.ethz.mlmq.test.db;

import java.sql.SQLException;

import org.junit.BeforeClass;
import org.junit.Test;

import ch.ethz.mlmq.logging.LoggerUtil;

public class DaoTest {

	@BeforeClass
	public static void beforeClass() {
		LoggerUtil.initConsoleDebug();
	}

	@Test
	public void testQueueDao() throws SQLException {

	}

}
