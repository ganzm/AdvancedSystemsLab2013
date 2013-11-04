package ch.ethz.mlmq.log_analizer;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math.stat.descriptive.moment.Mean;
import org.apache.commons.math.stat.descriptive.moment.Variance;

public class Bucket {

	private List<Double> values = new ArrayList<>();
	private double[] primitiveValuesCache;

	public int count() {
		return values.size();
	}

	public void addValue(int duration) {
		if (primitiveValuesCache != null)
			primitiveValuesCache = null;

		values.add((double) duration); // Could do this better...
	}

	public double mean() {
		Mean m = new Mean();
		return m.evaluate(getPrimitiveValues());
	}

	public double variance() {
		Variance v = new Variance();
		return v.evaluate(getPrimitiveValues());
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

}
