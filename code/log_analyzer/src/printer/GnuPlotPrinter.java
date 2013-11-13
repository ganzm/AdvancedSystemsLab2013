package printer;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;

import ch.ethz.mlmq.log_analyzer.Bucket;
import ch.ethz.mlmq.log_analyzer.DiagramType;

public class GnuPlotPrinter {

	private ArrayList<ArrayList<Bucket>> multiBuckets;

	/**
	 * where we write the .gnu file
	 */
	private PrintStream out;
	private boolean formatAsPng;
	private String outputFile;

	private String diagramTitle = "title";

	private String xLabel = "xLabel";

	private String yLabel = "yLabel";

	private String[] lineLabels = new String[] { "A", "B", "C", "D", "D" };

	private DiagramType diagramType;

	private boolean plotMedian;

	private boolean plotPercentile;

	private double percentile;

	/**
	 * @param buckets
	 * @param diagramType
	 * @param out
	 *            Stream where we write the gnu file to
	 * @param formatAsPng
	 * @param outputFile
	 *            optional file where the plot is stored when the generated gnu file is run, if null the plot is shown on the screen
	 * @param plotMedian
	 * @param percentile
	 * @param plotPercentile
	 */
	public GnuPlotPrinter(ArrayList<ArrayList<Bucket>> buckets, DiagramType diagramType, PrintStream out, boolean formatAsPng, String outputFile,
			boolean plotMedian, boolean plotPercentile, double percentile) {
		this.multiBuckets = buckets;
		this.diagramType = diagramType;
		this.out = out;
		this.formatAsPng = formatAsPng;
		this.outputFile = outputFile;
		this.plotMedian = plotMedian;
		this.plotPercentile = plotPercentile;
		this.percentile = percentile;
	}

	public void print() {
		long t0 = 0;
		if (multiBuckets.size() > 0) {
			t0 = multiBuckets.get(0).get(0).getTime();
		}

		try (PrintWriter writer = new PrintWriter(out)) {
			if (formatAsPng) {
				// Fontscale doesn't work, invalid argument...
				// writer.println("set terminal pngcairo  transparent enhanced font \"arial,10\" fontscale 1.0 size 500, 350");
				writer.println("set terminal pngcairo transparent enhanced font \"arial,10\" size 1000, 700");
			} else {
				writer.println("set term postscript eps color blacktext \"Helvetica\" 24");
			}

			if (outputFile != null) {
				writer.println("set output \'" + outputFile + "'");
			}

			// set key inside right top vertical Right noreverse enhanced autotitles box linetype -1 linewidth 1.000
			writer.println("set title \"" + diagramTitle + "\"");
			writer.println("set xlabel \"" + xLabel + "\"");
			writer.println("set ylabel \"" + yLabel + "\"");
			// TODO limit y scale of the plot - use set yrange [0:1000]

			writer.println("set style line 1 lc rgb '#0060ad' lt 1 lw 2 pt 7 ps 1.5");
			writer.println("set style line 2 lc rgb '#dd181f' lt 2 lw 2 pt 5 ps 1.5");
			writer.println("set style line 3 lc rgb 'green' lt 3 lw 2 pt 3 ps 1.5");
			writer.println("set style line 4 lc rgb '#cccccc' lt 4 lw 2 pt 5 ps 1.5");
			writer.println("set style line 5 lc rgb 'black' lt 5 lw 2 pt 5 ps 1.5");
			writer.print("plot ");
			for (int i = 0; i < multiBuckets.size(); i++) {
				String lineLabel = getLineLabel(i);
				if (i != 0)
					writer.print(", ");
				// String istr = "0" + i;
				// index " + istr + ":" + istr + "
				writer.print("\"-\" with errorlines title \"" + lineLabel + "\" ls " + (i + 1) + "");
			}
			writer.println();

			for (ArrayList<Bucket> b : multiBuckets) {
				writeBuckets(t0, writer, b);
			}

			if (outputFile != null) {
				writer.println("set output");
			}

			writer.println("quit");
		}
	}

	private void writeBuckets(long t0, PrintWriter writer, ArrayList<Bucket> buckets) {
		for (Bucket b : buckets) {
			if (diagramType == DiagramType.ResponseTime)
				writer.println(formatTime(b.getTime(), t0) + " " + medianOrMean(b) + " " + percentileOrStddev(b, false) + " " + percentileOrStddev(b, true));
			else
				writer.println(formatTime(b.getTime(), t0) + " " + b.count());
		}
		writer.println("e");
	}

	private double percentileOrStddev(Bucket b, boolean upperError) {
		double x = medianOrMean(b);

		if (plotPercentile)
			if (upperError) {
				return b.percentile(100.0 - percentile);
			} else {
				return b.percentile(percentile);
			}
		else {
			return upperError ? x - b.stddev() : x + b.stddev();
		}
	}

	private double medianOrMean(Bucket b) {
		return plotMedian ? b.median() : b.mean();
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

	public void setLineLabels(String lineLabelsString) {
		this.lineLabels = lineLabelsString.split(",");
	}

	private String getLineLabel(int index) {
		String[] tmp = lineLabels;
		if (tmp.length <= index)
			return "UNDEFINED";
		return tmp[index].trim();
	}

}
