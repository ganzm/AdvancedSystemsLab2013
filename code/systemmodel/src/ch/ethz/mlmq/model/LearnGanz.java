package ch.ethz.mlmq.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import ch.ethz.mlmq.model.queue.Queue;
import ch.ethz.mlmq.model.queue.QueueMMm;
import ch.ethz.mlmq.model.queue.QueueMMmB;

public class LearnGanz {

	private static void createQueues(List<Queue> queues, List<Integer> visitCounts) {
		int workerThreadCount = 20;
		int brokerCount = 8;

		// arrivalrate jobs/ms
		double lambda1 = 1.5d;
		// service time per job
		double s1 = 3;

		// M/M/8 Queue
		QueueMMm queue1 = new QueueMMm("Network Receive", lambda1 / ((double) brokerCount), s1, brokerCount);

		// WorkerThreadCount
		int queueSize = brokerCount * 100;
		int totalWorker = brokerCount * workerThreadCount;
		// Service Time
		double s2 = 1;
		QueueMMmB queue2 = new QueueMMmB("ProcessRequest", lambda1 / ((double) brokerCount), s2, totalWorker, queueSize);

		// M/M/2

		// db service time
		double s3 = 7;
		QueueMMm queue3 = new QueueMMm("Database", lambda1, s3, 12);

		// M/M/8
		QueueMMm queue4 = new QueueMMm("Network Send", lambda1 / ((double) brokerCount), s1, brokerCount);

		queues.add(queue1);
		visitCounts.add(1);

		queues.add(queue2);
		visitCounts.add(1);

		queues.add(queue3);
		visitCounts.add(1);

		queues.add(queue4);
		visitCounts.add(1);
	}

	public static void main(String[] args) {

		List<Queue> queues = new ArrayList<>();
		List<Integer> visitCounts = new ArrayList<>();
		createQueues(queues, visitCounts);

		// evaluateQueues(queues);

		int thinktime = 15;
		int N = 100;
		performMVA(queues, visitCounts, thinktime, N);
	}

	private static void evaluateQueues(List<Queue> queues) {

		for (Queue queue : queues) {
			int numItems = 30;

			if (queue instanceof QueueMMmB) {
				QueueMMmB bQueue = (QueueMMmB) queue;

				numItems = bQueue.getQueueSize();
			}

			System.out.println("======================================================");
			System.out.println("Queue: " + queue);
			System.out.print("Probability of n Jobs in the System:\n");

			BigDecimal sum = BigDecimal.ZERO;
			for (int i = 0; i < numItems; i++) {
				BigDecimal tmp = queue.getProbabilityNumJobsInSystem(i);

				sum = sum.add(tmp);
				System.out.print("\t" + tmp + "\n");
			}
			System.out.println("Total (should be 1)" + sum);
			System.out.println("Trafic Intensity ($\\rho$)& \\numprint{" + queue.getTraficIntensity() + "}\\\\");
			System.out.println("Probability of Queueing & \\numprint{" + queue.getProbabilityOfQueueing() + "}\\\\");
			System.out.println("Mean \\# of Jobs in the System & \\numprint{" + queue.getMeanNrOfJobsInSystem() + "}\\\\");
			System.out.println("Mean \\# of Jobs in the Queue & \\numprint{" + queue.getMeanNrOfJobsInQueue() + "}\\\\");
		}
	}

	private static void performMVA(List<Queue> queues, List<Integer> visitCounts, int thinktime, int N) {

		int M = queues.size();

		// Q_i - Average Number of Jobs per Queue

		BigDecimal[] qPerQueue = new BigDecimal[queues.size()];

		// P_i(J) probability of j jobs at the system
		List<BigDecimal[]> pJPerQueue = new ArrayList<>(queues.size());

		initializeMVA(queues, qPerQueue, pJPerQueue, N);

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
			BigDecimal X = new BigDecimal(n).divide(tmp, Queue.PRECISION, Queue.ROUND);

			// calculate new Queue Length
			for (int i = 0; i < M; i++) {
				Queue queue_i = queues.get(i);
				if (queue_i.isLoadDependent()) {
					// load dependent

					BigDecimal[] pJ = pJPerQueue.get(i);

					for (int j = n; j > 0; j--) {

						BigDecimal mu_j = queue_i.getServiceRateWithNJobs(j);

						BigDecimal tmp1 = pJ[j - 1];
						tmp1 = X.multiply(tmp1);
						tmp1 = tmp1.divide(mu_j, Queue.PRECISION, Queue.ROUND);

						pJ[j] = tmp1;
					}

					BigDecimal newPi0 = BigDecimal.ONE;

					for (int j = 1; j < pJ.length; j++) {
						newPi0 = newPi0.subtract(pJ[j]);
					}

					pJ[0] = newPi0;

				} else if (queue_i.isFixedCapacityQueue() || queue_i.isDelayCenter()) {
					// fixed or delay
					BigDecimal q_i = X.multiply(new BigDecimal(visitCounts.get(i)));
					q_i = q_i.multiply(responseTimePerQueue[i]);
					qPerQueue[i] = q_i;
				}
			}

			//

			for (int i = 0; i < M; i++) {
				Queue queue_i = queues.get(i);
				BigDecimal[] pJ = pJPerQueue.get(i);

				BigDecimal v_i = new BigDecimal(visitCounts.get(i));

				// througput per Queue
				xPerQueue[i] = X.multiply(v_i);

				// utilisation
				uPerQueue[i] = xPerQueue[i].multiply(queue_i.getMeanServiceTime());

			}

			logResult(n, X, rTotal, uPerQueue, rPerQueue, qPerQueue, pJPerQueue);
		}
	}

	private static void logResultHeader() {
		System.out.println("Number of Users N;System Througput X; System Response Time R;");
	}

	private static void logResult(int n, BigDecimal X, BigDecimal R, BigDecimal[] uPerQueue, BigDecimal[] rPerQueue, BigDecimal[] qPerQueue,
			List<BigDecimal[]> pJPerQueue) {

		System.out.println(n + ";" + X + ";" + R);
	}

	private static void initializeMVA(List<Queue> queues, BigDecimal[] qPerQueue, List<BigDecimal[]> pJPerQueue, int N) {
		for (int i = 0; i < queues.size(); i++) {
			// Q_i = 0
			qPerQueue[i] = BigDecimal.ZERO;

			// P_i(0) = 1
			BigDecimal[] list = new BigDecimal[N + 1];
			for (int n = 0; n <= N; n++) {
				list[n] = BigDecimal.ZERO;

			}
			list[0] = BigDecimal.ONE;
			pJPerQueue.add(list);
		}
	}

	private static BigDecimal calculateResponseTime(BigDecimal[] qPerQueue, List<BigDecimal[]> pJPerQueue, int n, int i, Queue queue_i) {

		BigDecimal r = null;
		if (queue_i.isFixedCapacityQueue()) {
			// fixed capacity queue
			BigDecimal q_i = qPerQueue[i];
			r = queue_i.getMeanServiceTime();
			r = r.multiply(BigDecimal.ONE.add(q_i));
		} else if (queue_i.isLoadDependent()) {
			// load dependent queue
			BigDecimal[] pJList = pJPerQueue.get(i);

			r = BigDecimal.ZERO;
			for (int j = 1; j <= n; j++) {
				BigDecimal bigJ = new BigDecimal(j);
				BigDecimal mu_j = queue_i.getServiceRateWithNJobs(j);
				BigDecimal pJ = pJList[j - 1];

				BigDecimal tmp = pJ.multiply(bigJ).divide(mu_j, Queue.PRECISION, Queue.ROUND);
				r = r.add(tmp);
			}

		} else {
			// delay center
			r = queue_i.getMeanServiceTime();
		}
		return r;
	}
}
