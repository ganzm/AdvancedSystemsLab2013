package printer;

import java.io.PrintStream;
import java.util.ArrayList;

import ch.ethz.mlmq.log_analyzer.Bucket;

public class TextPrinter {

	private final ArrayList<Bucket> buckets;
	private final PrintStream out;

	public TextPrinter(ArrayList<Bucket> buckets, PrintStream out) {
		this.buckets = buckets;
		this.out = out;
	}

	public void print() {
		if (buckets.size() != 1)
			throw new RuntimeException("buckets.size() != 1");

		Bucket b = buckets.get(0);
		out.println("Mean: " + b.mean() + ", Stddev: " + b.stddev() + ", Count: " + b.count() + ", Min: " + b.min() + ", Max: " + b.max());
	}

}
