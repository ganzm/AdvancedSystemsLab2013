package ch.ethz.mlmq.lukaselmer.mean_value_analyzer.solver;

import java.math.BigDecimal;
import java.math.MathContext;
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

	public void calculateNetwork(int n, double thinkTimeDouble, boolean printBottleneck, boolean printResponseTime) {
		BigDecimal thinkTime = new BigDecimal(thinkTimeDouble);
		for (int i = 1; i <= n; i++) {

			BigDecimal responseTimeTotal = new BigDecimal(0.0);
			// calc response time for each queue
			for (int m = 0; m < queues.size(); m++) {
				BigDecimal responseTime = queues.get(m).updateResponseTime(i);
				BigDecimal visits = queues.get(m).getVisits();
				responseTimeTotal = responseTimeTotal.add(responseTime.multiply(visits));
			}

			// calc throughput for overall system
			BigDecimal throughput = new BigDecimal(i).divide(thinkTime.add(responseTimeTotal), MathContext.DECIMAL128);

			if (i == n) {
				if (printResponseTime)
					printNumber(responseTimeTotal);
				else
					printNumber(throughput.multiply(new BigDecimal(1000)));
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

	private void printNumber(BigDecimal number) {
		System.out.println(("" + number.doubleValue()).replace('.', ','));
	}

	public void calcBottleneck(int n, BigDecimal throughput) {
		Queue bottleneckQueue = null;
		BigDecimal curBottleneck = new BigDecimal(0.0);
		Iterator<Queue> it = queues.iterator();
		while (it.hasNext()) {
			Queue queue = it.next();

			// calc device throughputs
			BigDecimal throughputDevice = queue.getVisits().multiply(throughput);

			// calc device utilizations
			BigDecimal utilization = queue.getVisits().multiply(throughput).multiply(queue.getServiceTime());
			if (utilization.compareTo(curBottleneck) == 1) {
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
