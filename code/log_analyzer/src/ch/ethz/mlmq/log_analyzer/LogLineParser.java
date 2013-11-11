package ch.ethz.mlmq.log_analyzer;

public class LogLineParser {

	public static LogLine parseLogLine(String firstLine) {
		String[] s = firstLine.split(";");

		if (s.length >= 3) {
			String s3 = s.length == 3 ? "" : s[3];
			return new LogLine(Integer.parseInt(s[0]), Long.parseLong(s[1]), s[2], s3);
		} else {
			System.out.println("Unable to parse log line " + firstLine);
			return new LogLine(0, Long.MIN_VALUE, "", "");
		}
	}

}
