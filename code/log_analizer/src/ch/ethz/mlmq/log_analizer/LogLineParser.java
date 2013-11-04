package ch.ethz.mlmq.log_analizer;

public class LogLineParser {

	public static LogLine parseLogLine(String firstLine) {
		String[] s = firstLine.split(";");

		return new LogLine(Integer.parseInt(s[0]), Long.parseLong(s[1]), s[2], s[3]);
	}

}
