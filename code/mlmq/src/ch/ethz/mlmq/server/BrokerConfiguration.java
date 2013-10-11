package ch.ethz.mlmq.server;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Logger;

import ch.ethz.mlmq.logging.PerformanceLoggerConfig;

/**
 * Holds configuration like database connection information, tuning parameters, etc for one single broker
 */
public class BrokerConfiguration {

	private static final Logger logger = Logger.getLogger("BrokerConfiguration");

	public static final String LISTENPORT2 = "listenport";
	public static final String WORKERTHREAD_COUNT = "workerthread.count";
	public static final String DB_CONNECTIONPOOL_SIZE = "db.connectionpool.size";
	public static final String DB_USERNAME = "db.username";
	public static final String DB_PASSWORD = "db.password";
	public static final String DB_URL = "db.url";
	public static final String DB_NAME = "db.name";
	public static final String REQUESTQUEUE_SIZE = "requestqueue.size";
	public static final String MAX_MESSAGE_SIZE = "message.maxsize";
	public static final String PERFORMANCELOGGER_PATH = "performancelogger.logfilepath";
	public static final String COMMANDOFILE_PATH = "commandofilepath";
	public static final String TESTRUN_ID = "testrun.id";

	protected int listenPort = 8099;
	protected int workerThreadCount = 5;
	protected int dbConnectionPoolSize = 3;
	protected String dbUserName = "postgres";
	protected String dbPassword = "postgres";
	protected String dbUrl = "jdbc:postgresql://localhost:5432";
	protected String dbName = "mlmq";
	protected int requestQueueSize = 10;
	protected int maxMessageSize = 4000;
	protected String commandoFilePath = "./brokercommando.txt";
	protected PerformanceLoggerConfig performanceLoggerConfig = new PerformanceLoggerConfig("log");

	/**
	 * describes the test scenario
	 */
	protected int testRunId = 1;

	public BrokerConfiguration() {
	}

	public BrokerConfiguration(Properties props) {
		initFromProps(props);
	}

	public static Properties loadProperties(String fileName) throws IOException {
		logger.info("Load BrokerConfiguration from " + fileName);

		try (InputStream inStream = BrokerConfiguration.class.getClassLoader().getResourceAsStream(fileName)) {
			if (inStream == null) {
				throw new IOException("FileNotFound " + fileName);
			}
			Properties props = new Properties();
			props.load(inStream);

			return props;
		}
	}

	public static BrokerConfiguration load(String fileName) throws IOException {
		return new BrokerConfiguration(loadProperties(fileName));
	}

	public static BrokerConfiguration load(InputStream inStream) throws IOException {
		Properties props = new Properties();
		props.load(inStream);

		BrokerConfiguration config = new BrokerConfiguration();
		config.initFromProps(props);
		return config;
	}

	private void initFromProps(Properties props) {
		listenPort = Integer.parseInt(props.getProperty(LISTENPORT2));
		workerThreadCount = Integer.parseInt(props.getProperty(WORKERTHREAD_COUNT));
		dbConnectionPoolSize = Integer.parseInt(props.getProperty(DB_CONNECTIONPOOL_SIZE));
		dbUserName = props.getProperty(DB_USERNAME);
		dbPassword = props.getProperty(DB_PASSWORD);
		dbUrl = props.getProperty(DB_URL);
		dbName = props.getProperty(DB_NAME);
		requestQueueSize = Integer.parseInt(props.getProperty(REQUESTQUEUE_SIZE));
		maxMessageSize = Integer.parseInt(props.getProperty(MAX_MESSAGE_SIZE));
		commandoFilePath = props.getProperty(COMMANDOFILE_PATH);
		testRunId = Integer.parseInt(props.getProperty(TESTRUN_ID));
		performanceLoggerConfig = new PerformanceLoggerConfig(props.getProperty(PERFORMANCELOGGER_PATH));

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

	public String getDbName() {
		return dbName;
	}

	public String getDbUrlWithDbName() {
		if (dbUrl.endsWith("/")) {
			return dbUrl + dbName;
		}
		return dbUrl + "/" + dbName;
	}

	public int getRequestQueueSize() {
		return requestQueueSize;
	}

	public int getMaxMessageSize() {
		return maxMessageSize;
	}

	public String getCommandoFilePath() {
		return commandoFilePath;
	}

	public PerformanceLoggerConfig getPerformanceLoggerConfig() {
		return performanceLoggerConfig;
	}

	public int getTestRunId() {
		return testRunId;
	}

}
