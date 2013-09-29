package ch.ethz.mlmq.server;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * Holds configuration like database connection information, tuning parameters, etc
 */
public final class BrokerConfiguration {

	private static final Logger logger = Logger.getLogger("BrokerConfiguration");

	private static final String LISTENPORT2 = "listenport";
	private static final String WORKERTHREAD_COUNT = "workerthread.count";
	private static final String DB_CONNECTIONPOOL_SIZE = "db.connectionpool.size";
	private static final String DB_USERNAME = "db.username";
	private static final String DB_PASSWORD = "db.password";
	private static final String DB_URL = "db.url";

	private int listenPort = 8099;
	private int workerThreadCount = 5;
	private int dbConnectionPoolSize = 3;
	private String dbUserName = "postgres";
	private String dbPassword = "postgres";
	private String dbUrl = "jdbc:postgresql://localhost:5432";

	public static BrokerConfiguration load(String fileName) throws IOException {
		try (InputStream inStream = BrokerConfiguration.class.getClassLoader().getResourceAsStream(fileName)) {
			return load(inStream);
		}
	}

	public static BrokerConfiguration load(InputStream inStream) throws IOException {

		Properties props = new Properties();
		props.load(inStream);

		BrokerConfiguration config = new BrokerConfiguration();

		config.listenPort = Integer.parseInt(props.getProperty(LISTENPORT2));
		config.workerThreadCount = Integer.parseInt(props.getProperty(WORKERTHREAD_COUNT));
		config.dbConnectionPoolSize = Integer.parseInt(props.getProperty(DB_CONNECTIONPOOL_SIZE));
		config.dbUserName = props.getProperty(DB_USERNAME);
		config.dbPassword = props.getProperty(DB_PASSWORD);
		config.dbUrl = props.getProperty(DB_URL);

		return config;
	}

	public int getListenPort() {
		return listenPort;
	}

	public int getWorkerThreadCount() {
		return workerThreadCount;
	}

	public int getDbConnectionPoolSize() {
		return dbConnectionPoolSize;
	}

	public String getDbUserName() {
		return dbUserName;
	}

	public String getDbPassword() {
		return dbPassword;
	}

	public String getDbUrl() {
		return dbUrl;
	}

}
