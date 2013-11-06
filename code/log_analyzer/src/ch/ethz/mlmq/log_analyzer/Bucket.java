package ch.ethz.mlmq.log_analyzer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.math.stat.descriptive.moment.Mean;
import org.apache.commons.math.stat.descriptive.moment.Variance;
import org.apache.commons.math.stat.descriptive.rank.Percentile;

public class Bucket {

	private List<Double> values = new ArrayList<>();
	private double[] primitiveValuesCache;

	/**
	 * Timestamp of the first measurement assigned to this bucket
	 */
	private long startTimestamp;

	public Bucket() {
		this.startTimestamp = Long.MAX_VALUE;
	}

	public int count() {
		return values.size();
	}

	public void addValue(int duration) {
		if (primitiveValuesCache != null)
			primitiveValuesCache = null;

		values.add((double) duration); // Could do this better...
	}

	public void addTimestamp(long timestamp) {
		startTimestamp = Math.min(startTimestamp, timestamp);
	}

	public double mean() {
		Mean m = new Mean();
		return m.evaluate(getPrimitiveValues());
	}

	public double variance() {
		Variance v = new Variance();
		return v.evaluate(getPrimitiveValues());
	}

	public double percentile(double percentile) {
		Percentile p = new Percentile();
		p.setData(getPrimitiveValues());
		return p.evaluate(percentile);
	}

	private double[] getPrimitiveValues() {
		if (primitiveValuesCache != null)
			return primitiveValuesCache;

		primitiveValuesCache = new double[values.size()];
		for (int i = 0; i < values.size(); i++) {
			primitiveValuesCache[i] = values.get(i);
		}
		return primitiveValuesCache;
	}

	public double min() {
		return Collections.min(values);
	}

	public double max() {
		return Collections.max(values);
	}

	public long getTime() {
		return startTimestamp;
	}
}
