package ch.ethz.mlmq.mva;

public class MiddlewareModel {

	public static void main(String[] args) {
		Mva mva = new Mva();
		int codeTime = 2;
		int maxClients = 1000;
		int maxMid = 10;
		int Z = codeTime;

		for (int M = 8; M <= maxMid; M += 1) {
			System.out.println("");
			for (int N = 1; N <= maxClients; N += 1) {
				System.out.print(M + " ");
				double tcpTime = 5;
				Queue TCP1 = new Queue("TCP1", tcpTime, 1, Queue.Type.delayCenter, 1, maxClients + 1);
				Queue TCP2 = new Queue("TCP2", tcpTime, 1, Queue.Type.delayCenter, 1, maxClients + 1);
				Queue TCP3 = new Queue("TCP3", tcpTime, 1, Queue.Type.delayCenter, 1, maxClients + 1);
				mva.addQueue(TCP1);
				mva.addQueue(TCP2);
				mva.addQueue(TCP3);

				for (int q = 1; q <= N; q++) {
					Queue queue = new Queue("Queue" + q, codeTime, 1.0 / N, Queue.Type.fixedCapacity, 1, maxClients + 1);
					mva.addQueue(queue);
				}

				Queue database = new Queue("DB put1", 60, 0.5, Queue.Type.loadDependent, 4 * 20, maxClients + 1);
				mva.addQueue(database);

				Queue database2 = new Queue("DB retrieve1", 70, 0.5, Queue.Type.loadDependent, 4 * 20, maxClients + 1);
				mva.addQueue(database2);

				for (int m = 1; m <= M; m++) {
					Queue middleware = new Queue("Middleware" + m, codeTime, 1.0 / M, Queue.Type.fixedCapacity, 1, maxClients + 1);
					mva.addQueue(middleware);
				}

				if (N == maxClients)
					mva.calculateNetwork(N, Z, true, true);
				else
					mva.calculateNetwork(N, Z, false, true);
				mva.clearQueues();
			}
		}
	}
}
