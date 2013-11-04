package ch.ethz.mlmq.log_analizer;

import java.util.ArrayList;
import java.util.List;

public class Bucket {

	private List<Double> values = new ArrayList<>();

	public int getCount() {
		return values.size();
	}

	public void addValue(int duration) {
		values.add((double) duration); // Could be done better?
	}

}
