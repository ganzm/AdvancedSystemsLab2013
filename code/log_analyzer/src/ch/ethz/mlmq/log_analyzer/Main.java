package ch.ethz.mlmq.log_analyzer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

public class Main {

	//@formatter:off
	private static String usageString = "usage: java -jar target.jar\n"
			+ "-d <directory_to_log_files>\n"
			+ "-type <message_type>\n"
			+ "-w <window-size(ms) optional>\n"
			+ "-out (out_file optional)\n"
			+ "-fmt output format default csv(csv|gnu-png|gnu-eps)";
	//@formatter:on

	public static void main(String[] args) throws FileNotFoundException {

		ArgUtil argUtil = ArgUtil.parseArgs(args);
		argUtil.getArgMap();
		LogAnalizer l = new LogAnalizer();

		if (argUtil.hasKey("?") || !argUtil.hasKey("d") || !argUtil.hasKey("type")) {
			System.out.println(usageString);
			return;
		}

		String directoryToLogFiles = argUtil.getMandatory("d");
		String messageType = argUtil.getMandatory("type");
		String formatString = argUtil.getOptional("fmt", "csv").toLowerCase();
		int windowSize = Integer.parseInt(argUtil.getOptional("w", "" + (1000 * 60 * 1)));

		PrintStream out;
		if (argUtil.hasKey("out")) {
			out = new PrintStream(FileUtils.getFile(argUtil.getMandatory("out"))); // Should close the printstream after usage...
		} else {
			out = System.out;
		}

		List<File> files = getPerformanceLogFiles(directoryToLogFiles);
		for (File file : files) {
			l.addFile(file);
		}

		ArrayList<Bucket> buckets = l.getBuckets(messageType, windowSize);

		if ("csv".equals(formatString)) {
			CSVPrinter p = new CSVPrinter(buckets, out);
			p.print();
		} else if ("gnu-png".equals(formatString)) {
			GnuPlotPrinter gnuP = new GnuPlotPrinter(buckets, out, true, null);
			gnuP.print();
		} else if ("gnu-eps".equals(formatString)) {
			GnuPlotPrinter gnuP = new GnuPlotPrinter(buckets, out, false, null);
			gnuP.print();
		}
	}

	private static List<File> getPerformanceLogFiles(String directoryToLogFiles) {
		File parent = FileUtils.getFile(directoryToLogFiles);

		List<File> files = new ArrayList<>();

		for (File file : FileUtils.listFiles(parent, new String[] { "log" }, true))
			if (file.getAbsolutePath().contains("performance_log"))
				files.add(file);

		return files;
	}
}
