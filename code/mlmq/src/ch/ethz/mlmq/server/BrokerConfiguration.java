package ch.ethz.mlmq.server;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import ch.ethz.mlmq.common.BrokerScenarioMapping;
import ch.ethz.mlmq.common.Configuration;
import ch.ethz.mlmq.common.ScenarioMapping;
import ch.ethz.mlmq.util.ConfigurationUtil;

/**
 * Holds configuration like database connection information, tuning parameters, etc for one single broker
 */
public class BrokerConfiguration extends Configuration {

	public static final String WORKERTHREAD_COUNT = "broker.workerthread.count";
	public static final String REQUESTQUEUE_SIZE = "broker.requestqueue.size";
	public static final String RESPONSEQUEUE_SIZE = "broker.responsequeue.size";
	public static final String MAX_MESSAGE_SIZE = "broker.message.maxsize";
	public static final String DB_CONNECTIONPOOL_SIZE = "broker.db.connectionpool.size";

	public static final String DB_USERNAME = "broker.db.username";
	public static final String DB_PASSWORD = "broker.db.password";
	public static final String DB_URL = "broker.db.url";
	public static final String DB_NAME = "broker.db.name";

	public BrokerConfiguration(Properties props) {
		super(props);
	}

	public static BrokerConfiguration loadFromJar(String fileName) throws IOException {
		return new BrokerConfiguration(ConfigurationUtil.loadPropertiesFromJar(fileName));
	}

	public static BrokerConfiguration load(InputStream inStream) throws IOException {
		Properties props = new Properties();
		props.load(inStream);

		return new BrokerConfiguration(props);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append(getClass().getSimpleName());
		sb.append(" Scenario[");
		sb.append(getMyBrokerMapping().getScenarioClassName());
		sb.append("] Port[");
		sb.append(getListenPort());
		sb.append("] WorkerThreadCount[");
		sb.append(getWorkerThreadCount());
		sb.append("] DbConnectionPoolSize[");
		sb.append(getDbConnectionPoolSize());
		sb.append("]");

		return sb.toString();
	}

	public BrokerScenarioMapping getMyBrokerMapping() {
		ScenarioMapping mapping = getMyMapping();
		if (mapping instanceof BrokerScenarioMapping) {
			return (BrokerScenarioMapping) mapping;
		} else {
			throw new RuntimeException("Could not get MyBrokermapping - my ScenarioMapping is " + mapping);
		}
	}

	public int getListenPort() {
		BrokerScenarioMapping mapping = getMyBrokerMapping();
		return mapping.getPort();
	}

	public int getWorkerThreadCount() {
		return getIntConfig(WORKERTHREAD_COUNT);
	}

	public int getDbConnectionPoolSize() {
		return getIntConfig(DB_CONNECTIONPOOL_SIZE);
	}

	public String getDbUserName() {
		return getStringConfig(DB_USERNAME);
	}

	public String getDbPassword() {
		return getStringConfig(DB_PASSWORD);
	}

	public String getDbUrl() {
		return getStringConfig(DB_URL);
	}

	public String getDbName() {
		return getStringConfig(DB_NAME);
	}

	public String getDbUrlWithDbName() {
		String dbUrl = getDbUrl();
		String dbName = getDbName();

		if (dbUrl.endsWith("/")) {
			return dbUrl + dbName;
		}
		return dbUrl + "/" + dbName;
	}

	/**
	 * Size of the Queue where the Broker Network Interface puts received Requests
	 * 
	 * @return
	 */
	public int getRequestQueueSize() {
		return getIntConfig(REQUESTQUEUE_SIZE);
	}

	public int getMaxMessageSize() {
		return getIntConfig(MAX_MESSAGE_SIZE);
	}

	/**
	 * Size of the Queue where Workers put their Responses which are ready to be sent back to the clients
	 * 
	 * @return
	 */
	public int getResponseQueueSize() {
		return getIntConfig(RESPONSEQUEUE_SIZE);
	}

}
