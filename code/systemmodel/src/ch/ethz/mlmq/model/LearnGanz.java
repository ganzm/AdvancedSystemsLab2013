package ch.ethz.mlmq.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import ch.ethz.mlmq.model.queue.Queue;
import ch.ethz.mlmq.model.queue.QueueMMm;
import ch.ethz.mlmq.model.queue.QueueMMmB;

public class LearnGanz {

	public static void main(String[] args) {

		QueueMMm queue = new QueueMMm("First Queue", 5, 11, 2);
		QueueMMmB queueB = new QueueMMmB("Network Receive", 5, 11, 2, 10);

		System.out.println("Queue: " + queueB);
		System.out.println("Traffic Intensity: " + queueB.getTraficIntensity());
		System.out.print("Probability of n Jobs in the System:\n");

		BigDecimal sum = BigDecimal.ZERO;
		for (int i = 0; i < 30; i++) {
			BigDecimal tmp = queueB.getProbabilityNumJobsInSystem(i);

			sum = sum.add(tmp);
			System.out.print("\t" + tmp + "\n");
		}
		System.out.println("Total (should be 1)" + sum);
		// System.out.println("Probability of Queueing " + queue.getProbabilityOfQueueing());
		System.out.println("MeanNrOfJobsInSystem " + queueB.getMeanNrOfJobsInSystem());
		System.out.println("MeanNrOfJobsInQueue " + queueB.getMeanNrOfJobsInQueue());

		//
		List<Queue> queues = new ArrayList<>();
		List<Integer> visitCounts = new ArrayList<>();

		queues.add(queue);
		visitCounts.add(1000);

		queues.add(queueB);
		visitCounts.add(1000);

		int thinktime = 15;
		int N = 100;
		performMVA(queues, visitCounts, thinktime, N);
	}

	private static void performMVA(List<Queue> queues, List<Integer> visitCounts, int thinktime, int N) {

		int M = queues.size();

		// Q_i - Average Number of Jobs per Queue
		List<BigDecimal> qPerQueue = new ArrayList<>(queues.size());

		// P_i(J) probability of j jobs at the system
		List<List<BigDecimal>> pJPerQueue = new ArrayList<>(queues.size());

		initializeMVA(queues, qPerQueue, pJPerQueue);

		logResultHeader();

		for (int n = 1; n <= N; n++) {

			// calculate Response Time R (per Queue)
			BigDecimal[] responseTimePerQueue = new BigDecimal[M];
			BigDecimal[] uPerQueue = new BigDecimal[M];
			BigDecimal[] rPerQueue = new BigDecimal[M];
			BigDecimal[] xPerQueue = new BigDecimal[M];

			// loop through queues
			for (int i = 0; i < M; i++) {
				Queue queue_i = queues.get(i);

				BigDecimal r = calculateResponseTime(qPerQueue, pJPerQueue, n, i, queue_i);

				responseTimePerQueue[i] = r;
			}

			// calculate total Response Time
			BigDecimal rTotal = BigDecimal.ZERO;
			for (int i = 0; i < M; i++) {
				BigDecimal r_i = responseTimePerQueue[i];
				BigDecimal v_i = new BigDecimal(visitCounts.get(i));
				rTotal = rTotal.add(r_i.multiply(v_i));
			}

			// Calculate total Throughput
			BigDecimal tmp = new BigDecimal(thinktime).add(rTotal);
			BigDecimal X = new BigDecimal(N).divide(tmp, Queue.PRECISION, Queue.ROUND);

			//
			for (int i = 0; i < M; i++) {
				Queue queue_i = queues.get(i);
				if (queue_i instanceof QueueMMmB) {
					// load dependent
					List<BigDecimal> pJ = pJPerQueue.get(i);

					for (int j = n; j > 0; j--) {

						BigDecimal mu_j = queue_i.getServiceRateWithNJobs(j);

						BigDecimal tmp1 = pJ.get(j - 1);
						tmp1 = X.multiply(tmp1);
						tmp1 = tmp1.divide(mu_j, Queue.PRECISION, Queue.ROUND);

						pJ.add(tmp1);
					}

					BigDecimal newPi0 = BigDecimal.ONE;

					assert n == pJ.size();
					for (int j = 1; j < pJ.size(); j++) {
						newPi0 = newPi0.subtract(pJ.get(j));
					}

					pJ.set(0, newPi0);

				} else {
					// fixed or delay
					BigDecimal q_i = X.multiply(new BigDecimal(visitCounts.get(i)));
					q_i = q_i.multiply(responseTimePerQueue[i]);
					qPerQueue.set(i, q_i);
				}
			}

			//

			for (int i = 0; i < M; i++) {
				Queue queue_i = queues.get(i);
				List<BigDecimal> pJ = pJPerQueue.get(i);

				BigDecimal v_i = new BigDecimal(visitCounts.get(i));
				xPerQueue[i] = X.multiply(v_i);

				if (queue_i instanceof QueueMMmB) {
					// load dependent
					uPerQueue[i] = BigDecimal.ONE.subtract(pJ.get(0));
				} else {
					// Fixed capacity or delay center
					uPerQueue[i] = X.multiply(queue_i.getMeanServiceTime()).multiply(v_i);
				}
			}

			logResult(n, X, rTotal, uPerQueue, rPerQueue, qPerQueue, pJPerQueue);
		}
	}

	private static void logResultHeader() {

	}

	private static void logResult(int n, BigDecimal X, BigDecimal R, BigDecimal[] uPerQueue, BigDecimal[] rPerQueue, List<BigDecimal> qPerQueue,
			List<List<BigDecimal>> pJPerQueue) {

		System.out.println("Number of Users N;System Througput X; System Response Time R;");

		System.out.println(n + ";" + X + ";" + R);

	}

	private static void initializeMVA(List<Queue> queues, List<BigDecimal> qPerQueue, List<List<BigDecimal>> pJPerQueue) {
		for (int i = 0; i < queues.size(); i++) {
			// Q_i = 0
			qPerQueue.add(BigDecimal.ZERO);

			// P_i(0) = 1
			List<BigDecimal> list = new ArrayList<>();
			list.add(BigDecimal.ONE);
			pJPerQueue.add(list);
		}
	}

	private static BigDecimal calculateResponseTime(List<BigDecimal> qPerQueue, List<List<BigDecimal>> pJPerQueue, int n, int i, Queue queue_i) {

		BigDecimal r = null;
		if (queue_i instanceof QueueMMm) {
			// fixed capacity queue
			BigDecimal q_i = qPerQueue.get(i);
			r = queue_i.getMeanServiceTime();
			r = r.multiply(BigDecimal.ONE.add(q_i));
		} else if (queue_i instanceof QueueMMmB) {
			// load dependent queue

			r = BigDecimal.ZERO;
			for (int j = 1; j <= n; j++) {

				BigDecimal mu_j = queue_i.getServiceRateWithNJobs(j);

				List<BigDecimal> pJList = pJPerQueue.get(i);
				BigDecimal pJ = pJList.get(j - 1);

				BigDecimal tmp = pJ.multiply(new BigDecimal(j)).divide(mu_j, Queue.PRECISION, Queue.ROUND);
				r = r.add(tmp);
			}

		} else {
			// delay center
			r = queue_i.getMeanServiceTime();
		}
		return r;
	}
}
