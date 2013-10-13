package ch.ethz.mlmq.test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Calendar;

import org.junit.Assert;
import org.junit.Test;

import ch.ethz.mlmq.logging.PerformanceLoggerConfig;
import ch.ethz.mlmq.logging.PerformanceLoggerImpl;

public class PerformanceLoggerTest {

	@Test
	public void testPerformanceLogger() throws FileNotFoundException, IOException {
		PerformanceLoggerConfig config = new PerformanceLoggerConfig("log");
		try {
			PerformanceLoggerImpl logger = new PerformanceLoggerImpl(config) {
				@Override
				protected Calendar getCurrentDate() {
					Calendar c = Calendar.getInstance();
					c.set(2013, 10, 23, 11, 37, 22);
					return c;
				}

				@Override
				protected void write(String logMessage) {
					Assert.assertEquals("100;20131123113722;bla\n", logMessage);
					super.write(logMessage);
				}
			};

			logger.log(100, "bla");
			logger.close();

			dumpFile(config.getFileName());
		} finally {
			File f = new File(config.getFileName());
			f.delete();
			File d = new File("log");
			d.delete();
		}
	}

	@Test
	public void testLogRotateConfig() throws FileNotFoundException, IOException {
		PerformanceLoggerConfig config = new PerformanceLoggerConfig("log");
		PerformanceLoggerImpl logger = new PerformanceLoggerImpl(config);

		logger.log(100, "huhu");
		logger.close();

		dumpFile(config.getFileName());
	}

	@Test
	public void testLogRotateLogger() {
		PerformanceLoggerConfig config = new PerformanceLoggerConfig("log");
		String filename = config.getFileName();

		String filenameAfter1Rotation = config.logRotate();
		Assert.assertNotEquals(filename, filenameAfter1Rotation);
		String filenameAfter2Rotations = config.logRotate();
		Assert.assertNotEquals(filename, filenameAfter2Rotations);
		Assert.assertNotEquals(filenameAfter1Rotation, filenameAfter2Rotations);
	}

	private void dumpFile(String fileName) throws IOException, FileNotFoundException {
		try (FileInputStream fin = new FileInputStream(fileName)) {
			ByteArrayOutputStream bOut = new ByteArrayOutputStream();
			int numBytes;
			byte[] buffer = new byte[512];
			while ((numBytes = fin.read(buffer)) > 0) {
				bOut.write(buffer, 0, numBytes);
			}

			System.out.println(new String(bOut.toByteArray()));
		}
	}
}
