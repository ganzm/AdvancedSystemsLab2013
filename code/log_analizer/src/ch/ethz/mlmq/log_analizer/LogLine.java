package ch.ethz.mlmq.log_analizer;

public class LogLine {

	private final int duration;
	private final String name;
	private final String addition;
	private final long timestamp;

	public LogLine(int duration, long timestamp, String name, String addition) {
		this.duration = duration;
		this.name = name;
		this.addition = addition;
		this.timestamp = timestamp;
	}

	public int getDuration() {
		return duration;
	}

	public String getName() {
		return name;
	}

	public String getAddition() {
		return addition;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public int getBucketPosition(long startBucketPosition, long windowSize) {
		long l = (timestamp - startBucketPosition) / windowSize;
		int i = (int) l;
		if (i != l)
			throw new RuntimeException("Overflow: " + l + " cannot be converted to int");
		return (int) l;
	}

}
