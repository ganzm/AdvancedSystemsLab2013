package ch.ethz.mlmq.model.queue;

import java.math.BigDecimal;

public abstract class Queue {

	final int PRECISION = 50;

	final int ROUND = BigDecimal.ROUND_HALF_UP;

	/**
	 * Queue name
	 */
	protected final String name;

	/**
	 * arrivalrate
	 */
	protected final double lambda;

	/**
	 * Service Rate
	 */
	protected final double mu;

	public Queue(String name, double lambda, double mu) {
		this.name = name;
		this.lambda = lambda;
		this.mu = mu;

	}

	public String getName() {
		return name;
	}

	public static BigDecimal factorial(int m) {
		BigDecimal result = BigDecimal.ONE;
		BigDecimal bigM = new BigDecimal(m);
		for (int i = 2; i <= m; i++) {
			result = result.multiply(bigM);
		}

		return result;
	}
}
