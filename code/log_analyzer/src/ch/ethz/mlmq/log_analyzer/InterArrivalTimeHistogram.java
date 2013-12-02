package ch.ethz.mlmq.log_analyzer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class InterArrivalTimeHistogram {

	public static void main(String[] args) {
		ArgUtil argUtil = ArgUtil.parseArgs(args);

		String directoryToLogFiles = argUtil.getMandatory("directory_to_log_files");
		String messageType = "BRcvReq"; // argUtil.getOptional("message_type", "");

		List<File> files = Main.getAllLogFiles(directoryToLogFiles);

		int[] histogramData = new int[10000];
		for (File file : files) {
			try (BufferedReader din = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {
				String header = din.readLine();

				if (header.startsWith("BrokerConfiguration") == false) {
					continue;
				}

				long lastTimestamp = Long.MAX_VALUE;
				String line = null;
				while ((line = din.readLine()) != null) {
					LogLine l = LogLineParser.parseLogLine(line);

					long timestamp = l.getTimestamp();
					String name = l.getName();
					if (messageType.equals(name)) {
						long interArrival = timestamp - lastTimestamp;
						lastTimestamp = timestamp;
						if (interArrival >= 0) {
							int interrArrivalInt = (int) interArrival;
							histogramData[interrArrivalInt]++;
						}
					}
				}
			} catch (IOException ex) {
				throw new RuntimeException(ex);
			}
		}

		writeCsv(histogramData);
	}

	private static void writeCsv(int[] histogramData) {
		System.out.println("interarrivaltime, count");
		for (int i = 0; i < histogramData.length; i++) {
			System.out.println(i + ", " + histogramData[i]);
		}
	}
}
