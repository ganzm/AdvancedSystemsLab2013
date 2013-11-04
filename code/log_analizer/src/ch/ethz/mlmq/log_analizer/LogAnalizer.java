package ch.ethz.mlmq.log_analizer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;

public class LogAnalizer {

	private ArrayList<File> files = new ArrayList<>();

	public void addFile(String testLogPath) {
		files.add(FileUtils.getFile(testLogPath));
	}

	/**
	 * @param string
	 * @param windowSize
	 *            in milliseconds
	 * @return
	 */
	public ArrayList<Bucket> getBuckets(String messageType, long windowSize) {
		ArrayList<Bucket> b = new ArrayList<>();
		long startBucketPosition = getStartBucketTime();

		for (File file : files) {
			try (BufferedReader din = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {
				din.readLine(); // skip header
				processLines(messageType, windowSize, b, startBucketPosition, din);
			} catch (IOException ex) {
				throw new RuntimeException(ex);
			}
		}

		return b;
	}

	private void processLines(String messageType, long windowSize, ArrayList<Bucket> b, long startBucketPosition, BufferedReader din) throws IOException {
		while (true) {
			String line = din.readLine();
			if (line == null)
				break;
			processLine(messageType, b, line, startBucketPosition, windowSize);
		}
	}

	private void processLine(String messageType, ArrayList<Bucket> b, String line, long startBucketPosition, long windowSize) {
		LogLine l = LogLineParser.parseLogLine(line);
		if (!l.getName().equals(messageType))
			return;

		int pos = l.getBucketPosition(startBucketPosition, windowSize);

		while (b.size() <= pos)
			b.add(new Bucket());

		b.get(pos).addValue(l.getDuration());
	}

	public long getStartBucketTime() {
		long minStartBucketTime = Long.MAX_VALUE;
		for (File file : files) {
			HeaderInfo i = new HeaderInfo(file);
			long x = i.getStartBucketTime();
			if (minStartBucketTime > x) {
				minStartBucketTime = x;
			}
		}
		return minStartBucketTime;
	}

}
