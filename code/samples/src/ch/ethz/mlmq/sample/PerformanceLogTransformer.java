package ch.ethz.mlmq.sample;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PerformanceLogTransformer {

	private final String inputFilePath;
	private final String outputFilePath;

	private final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");

	public PerformanceLogTransformer(String inputFilePath, String outputFilePath) {
		this.inputFilePath = inputFilePath;
		this.outputFilePath = outputFilePath;
	}

	public static void main(String[] args) throws Exception {
		String input = "C:\\Users\\mat\\Downloads\\test_run_4.csv";
		String output = "";

		new PerformanceLogTransformer(input, output).transform();
	}

	private void transform() throws Exception {

		try (FileInputStream fin = new FileInputStream(inputFilePath);
				InputStreamReader reader = new InputStreamReader(fin);
				BufferedReader bReader = new BufferedReader(reader);) {

			Date windowStart = null;
			String line = null;
			while ((line = bReader.readLine()) != null) {
				String[] lineParts = line.split(",");
				String eventType = lineParts[0];
				int duration = Integer.parseInt(lineParts[1]);
				Date timestamp = df.parse(lineParts[2]);

				if (windowStart == null) {
					windowStart = timestamp;
				}

			}
		}
	}
}
