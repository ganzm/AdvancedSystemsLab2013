package ch.ethz.mlmq.log_analyzer;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CSVPrinter {

	private static final String CSV_SEPERATOR = ";";
	private final PrintStream out;
	private final ArrayList<Bucket> buckets;

	private SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

	public CSVPrinter(ArrayList<Bucket> buckets, PrintStream out) {
		this.buckets = buckets;
		this.out = out;
	}

	public void print() {
		try (PrintWriter writer = new PrintWriter(out)) {
			writer.println("Time" + CSV_SEPERATOR + "Mean" + CSV_SEPERATOR + "Variance" + CSV_SEPERATOR + "Count");
			for (Bucket b : buckets) {
				writer.println("\'" + fmt.format(new Date(b.getTime())) + "\'" + CSV_SEPERATOR + b.mean() + CSV_SEPERATOR + b.variance() + CSV_SEPERATOR
						+ b.count());
			}
		}
	}
}
