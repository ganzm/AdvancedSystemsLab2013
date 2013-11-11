package printer;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

import ch.ethz.mlmq.log_analyzer.Bucket;

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
			Double[] percentiles = new Double[] { 0.5, 1.0, 2.5, 5.0, 10.0, 90.0, 95.0, 97.5, 99.0, 99.5 };
			ArrayList<String> titles = getTitles(percentiles);
			writer.println(StringUtils.join(titles, CSV_SEPERATOR));
			for (Bucket b : buckets) {
				ArrayList<String> values = getValues(percentiles, b);
				writer.println(StringUtils.join(values, CSV_SEPERATOR));
			}
		}
	}

	private ArrayList<String> getValues(Double[] percentiles, Bucket b) {
		ArrayList<String> values = new ArrayList<String>();
		values.add("\"" + fmt.format(new Date(b.getTime())) + "\"");
		values.add("" + b.getTime());
		values.add("" + b.variance());
		values.add("" + b.stddev());
		values.add("" + b.mean());
		values.add("" + b.count());
		values.add("" + b.min());
		values.add("" + b.max());
		values.add("" + b.median());
		for (Double d : percentiles) {
			values.add("" + b.percentile(d));
		}
		return values;
	}

	private ArrayList<String> getTitles(Double[] percentiles) {
		String[] titlesRaw = new String[] { "Time", "Timestamp", "Variance", "StdDev", "Mean", "Count", "Min", "Max", "Median" };

		ArrayList<String> titles = new ArrayList<String>();
		for (String t : titlesRaw) {
			titles.add(t);
		}

		for (Double d : percentiles) {
			titles.add("Percentile " + d);
		}
		return titles;
	}
}
