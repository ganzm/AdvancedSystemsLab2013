package ch.ethz.mlmq.db;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

public class ScriptRunner {

	private final Logger logger = Logger.getLogger("ScriptRunner");

	/**
	 * Executes an SQL script on an already established SQL Connection
	 * 
	 * @param scriptStream
	 * @param createTableConnection
	 * @throws IOException
	 * @throws SQLException
	 */
	public void execute(InputStream scriptStream, Connection createTableConnection) throws IOException, SQLException {

		String fileContentString = streamToString(scriptStream);

		String[] sqlCommands = fileContentString.split(";");

		for (String sqlCommand : sqlCommands) {
			String sqlCommandTrimed = sqlCommand.trim();
			logger.info("Executing " + sqlCommandTrimed);

			try (Statement stmt = createTableConnection.createStatement()) {
				stmt.execute(sqlCommandTrimed);
			}
		}
	}

	private String streamToString(InputStream scriptStream) throws IOException {
		byte[] buffer = new byte[1024];

		ByteArrayOutputStream bOut = new ByteArrayOutputStream();
		try (BufferedInputStream stream = new BufferedInputStream(scriptStream)) {
			int numBytes = -1;
			while ((numBytes = stream.read(buffer)) > 0) {
				bOut.write(buffer, 0, numBytes);
			}
		}

		return new String(bOut.toByteArray(), Charset.forName("UTF-8"));
	}
}
