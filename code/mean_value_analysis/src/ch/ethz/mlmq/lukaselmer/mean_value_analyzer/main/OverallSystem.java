package ch.ethz.mlmq.lukaselmer.mean_value_analyzer.main;

import ch.ethz.mlmq.lukaselmer.mean_value_analyzer.queue.Queue;
import ch.ethz.mlmq.lukaselmer.mean_value_analyzer.queue.QueueType;
import ch.ethz.mlmq.lukaselmer.mean_value_analyzer.solver.MeanValueAnalyzer;

public class OverallSystem {

	public static void main(String[] args) {

		boolean printResponseTime = true;
		double middlewareTime = 0.042;
		int maxClients = 300;
		double Z = 10;

		double tcpTime = 2.617;
		int middlewareCount = 80;
		int dbWorkers = 8;
		double dbServiceTime = 4.506; // fixed value (good): 4.506, estimated value (bad): 5.106

		MeanValueAnalyzer mva = new MeanValueAnalyzer();

		for (int numClients = 1; numClients <= maxClients; numClients++) {
			mva.addQueue(new Queue("TCP 1", tcpTime, 1, QueueType.delayCenter, 1, maxClients + 1));
			mva.addQueue(new Queue("TCP 2", tcpTime, 1, QueueType.delayCenter, 1, maxClients + 1));
			mva.addQueue(new Queue("TCP 3", tcpTime, 1, QueueType.delayCenter, 1, maxClients + 1));
			mva.addQueue(new Queue("TCP 4", tcpTime, 1, QueueType.delayCenter, 1, maxClients + 1));

			for (int m = 1; m <= middlewareCount; m++) {
				mva.addQueue(new Queue("Middleware " + m, middlewareTime, 1.0 / middlewareCount, QueueType.loadDependent, 1, maxClients + 1));
			}
			mva.addQueue(new Queue("Database", dbServiceTime, 1, QueueType.loadDependent, dbWorkers, maxClients + 1));
			mva.calculateNetwork(numClients, Z, numClients == maxClients, printResponseTime);
			mva.clearQueues();
		}
	}
}
