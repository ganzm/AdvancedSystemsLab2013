package ch.ethz.mlmq.mva;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * @author Lukas Elmer
 */
public class Mva {

	ArrayList<Queue> queues = new ArrayList<Queue>();

	public void addQueue(Queue iQueue) {
		queues.add(iQueue);
	}

	public void clearQueues() {
		queues = new ArrayList<Queue>();
	}

	public void calculateNetwork(int N, double Z, boolean bottleneck, boolean printResponseTime) {
		for (int n = 1; n <= N; n++) {

			// calculate response time for entire system
			double responseTimeTotal = 0.0;
			// calculate response time for each queue
			for (int i = 0; i < queues.size(); i++) {
				double responseTime = queues.get(i).updateResponseTime(n);
				double visits = queues.get(i).getVisits();
				responseTimeTotal += (responseTime * visits);
			}

			// System.out.println("responseTime: " + responseTimeTotal + "ms");

			// calculate throughput for entire system
			double throughput = n / (Z + responseTimeTotal);

			if (n == N) {
				// System.out.print(n + ": ");
				if (printResponseTime)
					System.out.println(responseTimeTotal);
				else
					System.out.println(throughput * 1000);
			}

			// System.out.println("throughput: " + throughput * 1000 * 60 +
			// "/minute");

			// if (n == N) {
			// System.out.println(throughput * 1000 * 60);
			// }

			// calculate new queue length for each queue
			for (int i = 0; i < queues.size(); i++) {
				queues.get(i).updateQueueLength(throughput, n);
			}

			if (bottleneck && n == N) {
				calculateBottleneck(n, throughput);
			}
		}
	}

	public void calculateBottleneck(int n, double throughput) {
		String bottleneckDevice = "";
		Double curBottleneck = 0.0;
		Iterator<Queue> it = queues.iterator();
		while (it.hasNext()) {
			Queue queue = it.next();
			if (true || queue.getName().endsWith("1")) {
				if (true || queue.getName().equals("Queue1") || !queue.getName().contains("Queue")) {

					// calculate device throughputs
					double throughputDevice = queue.getVisits() * throughput;

					// calculate device utilizations
					double utilizationDevice = queue.getVisits() * throughput * queue.getServiceTime();
					if (utilizationDevice > curBottleneck) {
						bottleneckDevice = queue.getName();
						curBottleneck = utilizationDevice;
					}

					if (queue.getName().contains("Qu")) {
						// System.out.println(n + " " + utilizationDevice);
						// }
						// System.out.println("  throughput  (" + queue.getName() + "): " +
						// throughputDevice);
						// System.out.println(utilizationDevice);
					}
				}
			}
		}
		System.out.println("Bottleneck: " + bottleneckDevice);
	}
}
