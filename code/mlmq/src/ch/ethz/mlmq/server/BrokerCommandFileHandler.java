package ch.ethz.mlmq.server;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.logging.Logger;

import ch.ethz.mlmq.logging.LoggerUtil;

public class BrokerCommandFileHandler implements Runnable {

	private static final Logger logger = Logger.getLogger(BrokerCommandFileHandler.class.getSimpleName());

	public static final String COMMAND_SHUTDOWN = "shutdown";
	public static final String COMMAND_LOG_STACKTRACE = "logstacktrace";
	public static final String COMMAND_LOG_MEMORY = "logmemory";

	private final String pathToFileToWatch;

	private File fileToWatch;

	private Thread thread = null;

	private volatile boolean running = false;

	private long lastModified;

	/**
	 * Intervall in ms
	 */
	private final long fileCheckIntervall;

	private final CommandListener commandListener;

	public BrokerCommandFileHandler(String pathToFileToWatch, long fileCheckIntervall, CommandListener commandListener) {
		this.pathToFileToWatch = pathToFileToWatch;
		this.fileCheckIntervall = fileCheckIntervall;
		this.commandListener = commandListener;
	}

	public void start() {
		logger.info("Start BrokerCommandFileHandler on " + pathToFileToWatch);
		thread = new Thread(this);
		thread.setName(this.getClass().getSimpleName());
		thread.setDaemon(true);
		thread.start();
	}

	public void stop() {
		running = false;
		thread.interrupt();
	}

	@Override
	public void run() {

		running = true;
		logger.info("BrokerCommandFileHandler is running");
		try {

			prepareFile();

			while (running) {
				try {
					Thread.sleep(fileCheckIntervall);

					checkFile();
				} catch (InterruptedException ex) {
					// ignore
				}
			}
		} catch (IOException e) {
			logger.severe("Exception " + LoggerUtil.getStackTraceString(e));
		} finally {
			running = false;
			logger.info("BrokerCommandFileHandler shut down");
		}
	}

	private void prepareFile() throws IOException {

		fileToWatch = new File(pathToFileToWatch);

		if (!fileToWatch.exists()) {

			String absPath = fileToWatch.getAbsolutePath();

			int stopIndex = absPath.lastIndexOf(File.separator);

			String parentpath = absPath.substring(0, stopIndex + 1);
			File parentDir = new File(parentpath);
			if (!parentDir.exists()) {
				if (!parentDir.mkdirs()) {
					throw new IOException("Could not create Directory " + parentpath);
				}
			}

			if (!fileToWatch.createNewFile()) {
				throw new IOException("Could not create File " + absPath);
			}
		}

		lastModified = fileToWatch.lastModified();
		logger.info("Last CommandFile Modification " + new Date(lastModified));
	}

	private void checkFile() throws FileNotFoundException, IOException {

		if (fileToWatch.lastModified() > lastModified) {

			ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
			try (FileInputStream fin = new FileInputStream(fileToWatch)) {
				int numBytes = 0;
				byte[] buffer = new byte[512];
				while ((numBytes = fin.read(buffer)) > 0) {
					byteOut.write(buffer, 0, numBytes);
				}

				byte[] fileContent = byteOut.toByteArray();
				String fileContentString = new String(fileContent);
				commandListener.onCommand(fileContentString);
			}

			lastModified = fileToWatch.lastModified();
		}
	}

}
