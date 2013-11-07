package ch.ethz.mlmq.log_analyzer;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;

public class GnuPlotPrinter {

	private ArrayList<Bucket> buckets;

	/**
	 * where we write the .gnu file
	 */
	private PrintStream out;
	private boolean formatAsPng;
	private String outputFile;

	private String diagramTitle = "title";

	private String xLabel = "xLabel";

	private String yLabel = "yLabel";

	private String meanLabel = "";

	private String interpolatedLabel = "";

	/**
	 * 
	 * @param buckets
	 * @param out
	 *            Stream where we write the gnu file to
	 * @param formatAsPng
	 * @param outputFile
	 *            optional file where the plot is stored when the generated gnu file is run, if null the plot is shown on the screen
	 */
	public GnuPlotPrinter(ArrayList<Bucket> buckets, PrintStream out, boolean formatAsPng, String outputFile) {
		this.buckets = buckets;
		this.out = out;
		this.formatAsPng = formatAsPng;
		this.outputFile = outputFile;
	}

	public void print() {
		long t0 = 0;
		if (buckets.size() > 0) {
			t0 = buckets.get(0).getTime();
		}

		try (PrintWriter writer = new PrintWriter(out)) {

			if (outputFile != null) {
				if (formatAsPng) {
					writer.println("set terminal pngcairo  transparent enhanced font \"arial,10\" fontscale 1.0 size 500, 350");
				} else {
					writer.println("set term postscript eps color blacktext \"Helvetica\" 24");
				}
				writer.println("set output \'" + outputFile + "'");
			}

			// set key inside right top vertical Right noreverse enhanced autotitles box linetype -1 linewidth 1.000
			writer.println("set title \"" + diagramTitle + "\"");
			writer.println("set xlabel \"" + xLabel + "\"");
			writer.println("set ylabel \"" + yLabel + "\"");
			writer.println("plot \"-\" t \"" + meanLabel + "\" with errorb, \"-\" t \"" + interpolatedLabel + "\" smooth csplines");

			for (Bucket b : buckets) {
				writer.println(formatTime(b.getTime(), t0) + " " + b.mean() + " " + b.variance());
			}
			writer.println("e");

			for (Bucket b : buckets) {
				writer.println(formatTime(b.getTime(), t0) + " " + b.mean() + " " + b.variance());
			}
			writer.println("e");

			if (outputFile != null) {
				writer.println("set output");
			}

			writer.println("quit");
		}
	}

	private String formatTime(long time, long t0) {
		return ((time - t0) / 1000) + "";
	}

	public void setDiagramTitle(String diagramTitle) {
		this.diagramTitle = diagramTitle;
	}

	public void setXLabel(String xLabel) {
		this.xLabel = xLabel;
	}

	public void setYLabel(String yLabel) {
		this.yLabel = yLabel;
	}

}
