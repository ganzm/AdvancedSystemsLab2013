package ch.ethz.mlmq.lukaselmer.mean_value_analyzer.solver;

import java.util.ArrayList;
import java.util.Iterator;

import ch.ethz.mlmq.lukaselmer.mean_value_analyzer.queue.Queue;

public class MeanValueAnalyzer {

	private final ArrayList<Queue> queues = new ArrayList<Queue>();

	public void addQueue(Queue iQueue) {
		queues.add(iQueue);
	}

	public void clearQueues() {
		queues.clear();
	}

	public void calculateNetwork(int n, double thinkTime, boolean printBottleneck, boolean printResponseTime) {
		for (int i = 1; i <= n; i++) {

			double responseTimeTotal = 0.0;
			// calc response time for each queue
			for (int m = 0; m < queues.size(); m++) {
				double responseTime = queues.get(m).updateResponseTime(i);
				double visits = queues.get(m).getVisits();
				responseTimeTotal += (responseTime * visits);
			}

			// calc throughput for overall system
			double throughput = i / (thinkTime + responseTimeTotal);

			if (i == n) {
				if (printResponseTime)
					printNumber(responseTimeTotal);
				else
					printNumber(throughput * 1000);
			}

			// calc queue length, for each queue
			for (int m = 0; m < queues.size(); m++) {
				queues.get(m).updateQueueLength(throughput, i);
			}

			if (printBottleneck && i == n) {
				calcBottleneck(i, throughput);
			}
		}
	}

	private void printNumber(double number) {
		System.out.println(("" + number).replace('.', ','));
	}

	public void calcBottleneck(int n, double throughput) {
		Queue bottleneckQueue = null;
		Double curBottleneck = 0.0;
		Iterator<Queue> it = queues.iterator();
		while (it.hasNext()) {
			Queue queue = it.next();

			// calc device throughputs
			double throughputDevice = queue.getVisits() * throughput;

			// calc device utilizations
			double utilization = queue.getVisits() * throughput * queue.getServiceTime();
			if (utilization > curBottleneck) {
				bottleneckQueue = queue;
				curBottleneck = utilization;
			}
			// if (queue.getName().equals("Middleware 1")) {
			// System.out.println(n + " " + utilizationDevice);
			// }
			// System.out.println("  throughput  (" + queue.getName() + "): " +
			// throughputDevice);
			// System.out.println(utilizationDevice);
			// }
		}
		System.out.println("Bottleneck: " + bottleneckQueue.getName());
	}
}
