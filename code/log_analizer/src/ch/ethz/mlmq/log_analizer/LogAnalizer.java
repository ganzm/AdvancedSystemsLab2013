package ch.ethz.mlmq.log_analizer;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;

public class LogAnalizer {

	private Map<String, ArrayList<Bucket>> bucketsByName = new HashMap<String, ArrayList<Bucket>>();
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
		return crunch(messageType);
	}

	private ArrayList<Bucket> crunch(String messageType) {

		return bucketsByName.get(messageType);
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
