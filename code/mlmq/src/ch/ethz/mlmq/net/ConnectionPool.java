package ch.ethz.mlmq.net;

import java.io.Closeable;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Logger;

import ch.ethz.mlmq.dto.BrokerDto;
import ch.ethz.mlmq.logging.LoggerUtil;

/**
 * This is a socket connection pool for clients
 */
public class ConnectionPool implements Closeable {

	private final Logger logger = Logger.getLogger("ConnectionPool");

	private final Map<BrokerDto, ClientConnection> connections = new HashMap<>();

	public ClientConnection getConnection(BrokerDto broker) throws IOException {

		ClientConnection connection = connections.get(broker);
		if (connection == null) {

			connection = createConnection(broker);
			connections.put(broker, connection);
		}
		return connection;
	}

	private ClientConnection createConnection(BrokerDto broker) throws IOException {
		try {
			// TODO CONFIG - move to config
			long responseTimeoutTime = 2000;

			ClientConnection connection = new ClientConnection(broker.getHost(), broker.getPort(), responseTimeoutTime);
			connection.connect();
			return connection;
		} catch (UnknownHostException e) {
			throw new IOException(e);
		}
	}

	@Override
	public void close() throws IOException {
		logger.info("Closing ConnectionPool");
		Set<Entry<BrokerDto, ClientConnection>> entries = connections.entrySet();
		for (Entry<BrokerDto, ClientConnection> entry : entries) {
			try {
				entry.getValue().close();
			} catch (Exception ex) {
				logger.severe("Error while closing connection to Broker " + entry.getKey() + " " + LoggerUtil.getStackTraceString(ex));
			}
		}

		entries.clear();
	}
}
