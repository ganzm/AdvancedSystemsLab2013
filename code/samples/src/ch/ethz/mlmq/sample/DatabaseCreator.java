package ch.ethz.mlmq.sample;

import java.io.IOException;
import java.sql.SQLException;

import ch.ethz.mlmq.server.BrokerConfiguration;
import ch.ethz.mlmq.server.db.util.DatabaseInitializer;

public class DatabaseCreator {

	public static void main(String[] args) throws IOException, SQLException {

		BrokerConfiguration config = BrokerConfiguration.load("unittestconfig.example.properties");

		DatabaseInitializer dbInitializer = new DatabaseInitializer(config.getDbUrl(), config.getDbUserName(), config.getDbPassword(), "mlmq");
		dbInitializer.connect();

		// dbInitializer.deleteDatabase();
		dbInitializer.createDatabase();
		dbInitializer.createTables();
	}

}
