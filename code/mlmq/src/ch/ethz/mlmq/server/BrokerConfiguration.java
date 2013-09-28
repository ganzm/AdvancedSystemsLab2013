package ch.ethz.mlmq.server;

/**
 * Holds configuration like database connection information, tuning parameters, etc
 */
public final class BrokerConfiguration {

	private int listenPort = 8099;
	private int workerThreadCount = 5;
	private int dbConnectionPoolSize = 3;
	private String dbUserName = "postgres";
	private String dbPassword = "postgres";
	private String dbUrl = "jdbc:postgresql://localhost:5432";

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
