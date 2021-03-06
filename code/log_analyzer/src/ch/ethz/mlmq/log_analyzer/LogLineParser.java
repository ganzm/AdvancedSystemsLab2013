package ch.ethz.mlmq.log_analyzer;

public class LogLineParser {

	public static LogLine parseLogLine(String firstLine) {
		if (firstLine == null)
			return null;

		String[] s = firstLine.split(";");

		if (s.length >= 3) {
			String s3 = s.length == 3 ? "" : s[3];
			return new LogLine(Integer.parseInt(s[0]), Long.parseLong(s[1]), s[2], s3);
		} else {
			return null;
		}
	}

}
