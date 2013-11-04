package ch.ethz.mlmq.log_analizer;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;

public class CSVPrinter {

	private static final String CSV_SEPERATOR = ";";
	private final PrintStream out;
	private final ArrayList<Bucket> buckets;

	public CSVPrinter(ArrayList<Bucket> buckets, PrintStream out) {
		this.buckets = buckets;
		this.out = out;
	}

	public void print() {
		try (PrintWriter writer = new PrintWriter(out)) {
			for (Bucket b : buckets) {
				writer.println("" + b.mean() + CSV_SEPERATOR + b.variance() + CSV_SEPERATOR + b.count());
			}
		}
	}
}
