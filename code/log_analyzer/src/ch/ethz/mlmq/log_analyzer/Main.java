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
			+ "-directory_to_log_files <directory_to_log_files>\n"
			+ "-message_type <message_type>\n"
			+ "-window_size <window-size(ms) optional>\n"
			+ "-out (out_file optional)\n"
			+ "-output_format output format default csv(csv|gnu-png|gnu-eps)\n"
			+ "-x_axis_label xAxis label (optional)\n"
			+ "-y_axis_label yAxis label (optional)\n"
			+ "-diagram_title diagram title (optional)";
	//@formatter:on

	public static void main(String[] args) throws FileNotFoundException {

		ArgUtil argUtil = ArgUtil.parseArgs(args);
		argUtil.getArgMap();
		LogAnalizer l = new LogAnalizer();

		if (argUtil.hasKey("?") || !argUtil.hasKey("directory_to_log_files")) {
			System.out.println(usageString);
			return;
		}

		String directoryToLogFiles = argUtil.getMandatory("directory_to_log_files");
		String messageType = argUtil.getOptional("message_type", "");
		String formatString = argUtil.getOptional("output_format", "csv").toLowerCase();
		int windowSize = Integer.parseInt(argUtil.getOptional("window_size", "" + (1000 * 60 * 1)));

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
			addOptionalGnuPlotParams(gnuP, argUtil);
			gnuP.print();
		} else if ("gnu-eps".equals(formatString)) {
			GnuPlotPrinter gnuP = new GnuPlotPrinter(buckets, out, false, null);
			addOptionalGnuPlotParams(gnuP, argUtil);
			gnuP.print();
		}
	}

	private static void addOptionalGnuPlotParams(GnuPlotPrinter gnuP, ArgUtil argUtil) {
		if (argUtil.hasKey("diagram_title"))
			gnuP.setDiagramTitle(argUtil.getMandatory("diagram_title"));
		if (argUtil.hasKey("x_axis_label"))
			gnuP.setXLabel(argUtil.getMandatory("x_axis_label"));
		if (argUtil.hasKey("y_axis_label"))
			gnuP.setYLabel(argUtil.getMandatory("y_axis_label"));
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
