package ch.ethz.mlmq.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Logger;

import ch.ethz.mlmq.server.BrokerConfiguration;

public class ConfigurationUtil {

	private static final Logger logger = Logger.getLogger(ConfigurationUtil.class.getSimpleName());

	public static Properties loadPropertiesFromFile(String fileName) throws IOException {

		logger.info("Load BrokerConfiguration from " + fileName);

		File f = new File(fileName);
		if (!f.exists()) {
			throw new IOException("File not found " + fileName);
		}

		try (InputStream inStream = new FileInputStream(f)) {
			Properties props = new Properties();
			props.load(inStream);

			return props;
		}
	}

	public static Properties loadPropertiesFromJar(String fileName) throws IOException {
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

}
