/**
 * 
 * @author sara
 * 
 */
public class Queue {

	private double serviceTime;
	private double visits;
	private Type type;
	private double responseTime;
	private double queueLength;
	private String name;

	private final double[] P;
	private int numberOfServices;

	enum Type {
		delayCenter, fixedCapacity, loadDependent
	}

	public Queue(String name, double serviceTime, double visits, Type type, int numberOfServices, int size) {
		setName(name);
		P = new double[size];
		setServiceTime(serviceTime);
		setVisits(visits);
		setType(type);
		responseTime = serviceTime;
		queueLength = 0;
		setNumberOfServices(numberOfServices);
		P[0] = 1.0;

	}

	public double updateResponseTime(int n) {
		if (type == Type.fixedCapacity) {
			responseTime = serviceTime * (1 + queueLength);
		} else if (type == Type.loadDependent) {
			double sum = 0;
			for (int i = 1; i <= n; i++) {
				sum += P[i - 1] * i / getMu(i);
			}
			responseTime = sum;
		}
		return responseTime;
	}

	private double getMu(int i) {

		if (i < numberOfServices) {
			return i / serviceTime;
		} else {
			return numberOfServices / serviceTime;
		}
		/*
		 * if (i == 1) return 0.32; else if (i == 2) return 0.39; else return 0.42;
		 */
	}

	public void updateQueueLength(double throughput, int n) {
		if (type == Type.loadDependent) {
			for (int j = n; j >= 1; j--) {
				P[j] = (throughput / getMu(j)) * P[j - 1];
				if (P[j] < 0) {
					P[j] = 0;
				}
			}
			double sum = 0;
			for (int j = 1; j <= n; j++) {
				sum += P[j];
			}
			if (P[0] > 0) {
				P[0] = 1 - sum;
				if (P[0] < 0) {
					P[0] = 0;
				}
			}
			int G = 0;
		} else {
			queueLength = throughput * visits * responseTime;
		}
	}

	public double getServiceTime() {
		return serviceTime;
	}

	public void setServiceTime(double d) {
		this.serviceTime = d;
	}

	public double getVisits() {
		return visits;
	}

	public void setVisits(double visits) {
		this.visits = visits;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public double getResponseTime() {
		return responseTime;
	}

	public void setResponseTime(double responseTime) {
		this.responseTime = responseTime;
	}

	public double getQueueLength() {
		return queueLength;
	}

	public void setQueueLength(double queueLength) {
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
