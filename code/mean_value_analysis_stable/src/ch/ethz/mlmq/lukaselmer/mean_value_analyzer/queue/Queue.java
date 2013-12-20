package ch.ethz.mlmq.lukaselmer.mean_value_analyzer.queue;

import java.math.BigDecimal;
import java.math.MathContext;

public class Queue {

	private BigDecimal serviceTime;
	private BigDecimal visits;
	private QueueType type;
	private BigDecimal responseTime;
	private BigDecimal queueLength;
	private String name;

	private final BigDecimal[] P;
	private int numberOfServices;

	public Queue(String name, double serviceTime, double visits, QueueType type, int numberOfServices, int size) {
		setName(name);
		P = new BigDecimal[size];
		setServiceTime(new BigDecimal(serviceTime));
		setVisits(new BigDecimal(visits));
		setType(type);
		responseTime = new BigDecimal(serviceTime);
		queueLength = new BigDecimal(0);
		setNumberOfServices(numberOfServices);
		P[0] = new BigDecimal(1.0);
	}

	public BigDecimal updateResponseTime(int n) {
		if (type == QueueType.fixedCapacity) {
			responseTime = serviceTime.multiply((new BigDecimal(1).add(queueLength)));
		} else if (type == QueueType.loadDependent) {
			BigDecimal sum = new BigDecimal(0);
			for (int i = 1; i <= n; i++) {
				sum = sum.add(P[i - 1].multiply(new BigDecimal(i)).divide(getMu(i), MathContext.DECIMAL128));
			}
			responseTime = sum;
		}
		return responseTime;
	}

	private BigDecimal getMu(int i) {
		if (i < numberOfServices) {
			return new BigDecimal(i).divide(serviceTime, MathContext.DECIMAL128);
		} else {
			return new BigDecimal(numberOfServices).divide(serviceTime, MathContext.DECIMAL128);
		}
	}

	public void updateQueueLength(BigDecimal throughput, int n) {
		if (type == QueueType.loadDependent) {
			for (int j = n; j >= 1; j--) {
				P[j] = (throughput.divide(getMu(j), MathContext.DECIMAL128)).multiply(P[j - 1]);
				if (P[j].compareTo(new BigDecimal(0)) == -1) {
					P[j] = new BigDecimal(0);
				}
			}
			BigDecimal sum = new BigDecimal(0);
			for (int j = 1; j <= n; j++) {
				sum = sum.add(P[j]);
			}
			if (P[0].compareTo(new BigDecimal(0)) == 1) {
				P[0] = new BigDecimal(1).subtract(sum);
				if (P[0].compareTo(new BigDecimal(0)) == -1) {
					P[0] = new BigDecimal(0);
				}
			}
			// int G = 0;
		} else {
			queueLength = throughput.multiply(visits).multiply(responseTime);
		}
	}

	public BigDecimal getServiceTime() {
		return serviceTime;
	}

	public void setServiceTime(BigDecimal d) {
		this.serviceTime = d;
	}

	public BigDecimal getVisits() {
		return visits;
	}

	public void setVisits(BigDecimal visits) {
		this.visits = visits;
	}

	public QueueType getType() {
		return type;
	}

	public void setType(QueueType type) {
		this.type = type;
	}

	public BigDecimal getResponseTime() {
		return responseTime;
	}

	public void setResponseTime(BigDecimal responseTime) {
		this.responseTime = responseTime;
	}

	public BigDecimal getQueueLength() {
		return queueLength;
	}

	public void setQueueLength(BigDecimal queueLength) {
		this.queueLength = queueLength;
	}

	public int getNumberOfServices() {
		return numberOfServices;
	}

	public void setNumberOfServices(int numberOfServices) {
		this.numberOfServices = numberOfServices;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
