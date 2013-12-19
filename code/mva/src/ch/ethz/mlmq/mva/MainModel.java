package ch.ethz.mlmq.mva;

public class MainModel {

	public static void main(String[] args) {
		Mva mva = new Mva();
		double middlewareTime = 0.042;
		int maxClients = 300;
		// double Z = codeTime;
		double Z = 10;

		double tcpTime = 2.617;
		int middlewareCount = 80;
		// int dbWorkers = 4 * 15;
		int dbWorkers = 8;
		double dbServiceTime = 4.506;
		// fixed value: 4.506
		// estimated value: 5.106

		for (int N = 1; N <= maxClients; N++) {

			// Queue TCP1 = new Queue("TCP1", tcpTime, 1, Queue.Type.delayCenter, 1, maxClients + 1);
			// Queue TCP1 = new Queue("TCP1", tcpTime, 1, Queue.Type.delayCenter, 1, maxClients + 1);
			Queue TCP1 = new Queue("TCP1", tcpTime, 1, Queue.Type.delayCenter, 1, maxClients + 1);
			Queue TCP2 = new Queue("TCP2", tcpTime, 1, Queue.Type.delayCenter, 1, maxClients + 1);
			Queue TCP3 = new Queue("TCP3", tcpTime, 1, Queue.Type.delayCenter, 1, maxClients + 1);
			Queue TCP4 = new Queue("TCP4", tcpTime, 1, Queue.Type.delayCenter, 1, maxClients + 1);
			mva.addQueue(TCP1);
			mva.addQueue(TCP2);
			mva.addQueue(TCP3);
			mva.addQueue(TCP4);

			for (int m = 1; m <= middlewareCount; m++) {
				// Queue middleware = new Queue("Middleware" + m, middlewareTime, 1.0 / middlewareCount, Queue.Type.fixedCapacity, 1, maxClients + 1);
				Queue middleware = new Queue("Middleware" + m, middlewareTime, 1.0 / middlewareCount, Queue.Type.loadDependent, 1, maxClients + 1);
				mva.addQueue(middleware);
			}

			Queue database = new Queue("DB", dbServiceTime, 1, Queue.Type.loadDependent, dbWorkers, maxClients + 1);
			// Queue database = new Queue("DB", 5106, 1, Queue.Type.loadDependent, 1, maxClients + 1);
			mva.addQueue(database);

			/*
			 * for (int q = 1; q <= N; q++) { Queue queue = new Queue("Queue" + q, codeTime, 1.0 / N, Queue.Type.fixedCapacity, 1, maxClients + 1);
			 * mva.addQueue(queue); }
			 */

			/*
			 * Queue database = new Queue("DB put1", 60, 0.5, Queue.Type.loadDependent, 4 * 20, maxClients + 1); mva.addQueue(database);
			 * 
			 * Queue database2 = new Queue("DB retrieve1", 70, 0.5, Queue.Type.loadDependent, 4 * 20, maxClients + 1); mva.addQueue(database2);
			 */

			boolean printResponseTime = true;

			if (N == maxClients) {
				// mva.calculateNetwork(N, Z, true, true);
				mva.calculateNetwork(N, Z, true, printResponseTime);
			} else {
				// mva.calculateNetwork(N, Z, false, true);
				mva.calculateNetwork(N, Z, false, printResponseTime);
			}

			mva.clearQueues();
		}
	}
}
