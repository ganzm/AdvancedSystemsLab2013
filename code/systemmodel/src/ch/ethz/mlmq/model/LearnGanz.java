package ch.ethz.mlmq.model;

import java.math.BigDecimal;

import ch.ethz.mlmq.model.queue.QueueMMmB;

public class LearnGanz {

	public static void main(String[] args) {

		QueueMMmB queue = new QueueMMmB("Network Receive", 5, 11, 2, 10);

		System.out.println("Queue: " + queue);
		System.out.println("Traffic Intensity: " + queue.getTraficIntensity());
		System.out.print("Probability of n Jobs in the System:\n");

		BigDecimal sum = BigDecimal.ZERO;
		for (int i = 0; i < 30; i++) {
			BigDecimal tmp = queue.getProbabilityNumJobsInSystem(i);

			sum = sum.add(tmp);
			System.out.print("\t" + tmp + "\n");
		}
		System.out.println("Total (should be 1)" + sum);
		// System.out.println("Probability of Queueing " + queue.getProbabilityOfQueueing());
		System.out.println("MeanNrOfJobsInSystem " + queue.getMeanNrOfJobsInSystem());
		System.out.println("MeanNrOfJobsInQueue " + queue.getMeanNrOfJobsInQueue());

	}
}
