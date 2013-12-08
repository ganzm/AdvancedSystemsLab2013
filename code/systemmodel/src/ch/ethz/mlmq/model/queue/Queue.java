package ch.ethz.mlmq.model.queue;

import java.math.BigDecimal;

public abstract class Queue {

	public static final int PRECISION = 50;

	public static final int ROUND = BigDecimal.ROUND_HALF_UP;

	/**
	 * Queue name
	 */
	protected final String name;

	/**
	 * arrivalrate
	 */
	protected final double lambda;

	/**
	 * Mean Service Time per job
	 */
	protected double s;

	public Queue(String name, double lambda, double s) {
		this.name = name;
		this.lambda = lambda;
		this.s = s;
	}

	public BigDecimal getMeanServiceTime() {
		return new BigDecimal(s);
	}

	/**
	 * Mean Service Rate
	 * 
	 * mu
	 * 
	 * 
	 * @return
	 */
	public BigDecimal getMeanServiceRate() {
		return BigDecimal.ONE.divide(new BigDecimal(s), PRECISION, ROUND);
	}

	/**
	 * Service Rate with n jobs in the system
	 * 
	 * @param n
	 * @return
	 */
	public abstract BigDecimal getServiceRateWithNJobs(int n);

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
