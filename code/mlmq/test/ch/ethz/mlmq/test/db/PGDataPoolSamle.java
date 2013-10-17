package ch.ethz.mlmq.test.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.postgresql.ds.PGPoolingDataSource;

public class PGDataPoolSamle {

	public static void main(String[] args) throws SQLException {

		PGPoolingDataSource source = null;
		try {
			source = new PGPoolingDataSource();

			source.setDataSourceName("MLMQ-DataSource");
			source.setServerName("localhost");
			source.setDatabaseName("mlmq");
			source.setPortNumber(5432);
			source.setUser("postgres");
			source.setPassword("postgres");
			source.setMaxConnections(10);

			try (Connection conn = source.getConnection()) {

				// use connection
				try (

				Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery("Select * from client limit 10");

				) {
					while (rs.next()) {
						System.out.println(rs.getInt(1) + " " + rs.getString(2));
					}
				}
			}
		} finally {
			if (source != null) {
				source.close();
			}
		}
	}
}
