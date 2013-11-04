package ch.ethz.mlmq.log_analizer;

public class LogLine {

	public LogLine(int duration, long timestamp, String name, String addition) {
		super();
		this.duration = duration;
		this.name = name;
		this.addition = addition;
		this.timestamp = timestamp;
	}

	private int duration;
	private String name;
	private String addition;
	private long timestamp;

	public int getDuration() {
		// TODO Auto-generated method stub
		return duration;
	}

	public String getName() {
		// TODO Auto-generated method stub
		return name;
	}

	public String getAddition() {
		// TODO Auto-generated method stub
		return addition;
	}

	public long getTimestamp() {
		// TODO Auto-generated method stub
		return timestamp;
	}

}
