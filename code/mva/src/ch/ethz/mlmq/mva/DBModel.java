package ch.ethz.mlmq.mva;

public class DBModel {

	public static void main(String[] args) {
		Mva mva = new Mva();
		int codeTime = 2;
		int maxClients = 1000;
		int maxDB = 10;
		int Z = codeTime;

		for (int D = 8; D <= maxDB; D += 1) {
			System.out.println("");
			for (int N = 1; N <= maxClients; N += 1) {

				System.out.print(D + " ");

				double tcpTime = 5;
				Queue TCP1 = new Queue("TCP1", tcpTime, 1, Queue.Type.delayCenter, 1, maxClients + 1);
				Queue TCP2 = new Queue("TCP2", tcpTime, 1, Queue.Type.delayCenter, 1, maxClients + 1);
				Queue TCP3 = new Queue("TCP3", tcpTime, 1, Queue.Type.delayCenter, 1, maxClients + 1);
				mva.addQueue(TCP1);
				mva.addQueue(TCP2);
				mva.addQueue(TCP3);

				for (int m = 1; m <= 8; m++) {
					Queue middleware = new Queue("Middleware" + m, codeTime, 1.0 / 8.0, Queue.Type.fixedCapacity, 1, maxClients + 1);
					mva.addQueue(middleware);
				}

				for (int q = 1; q <= N; q++) {
					Queue queue = new Queue("Queue" + q, codeTime, 1.0 / N, Queue.Type.fixedCapacity, 1, maxClients + 1);
					mva.addQueue(queue);
				}

				for (int d = 1; d <= D; d++) {
					Queue database = new Queue("DB put" + d, 60, 1.0 / (2.0 * D), Queue.Type.loadDependent, D * 4 * 20, maxClients + 1);
					mva.addQueue(database);

					Queue database2 = new Queue("DB retrieve" + d, 70, 1.0 / (2.0 * D), Queue.Type.loadDependent, D * 4 * 20, maxClients + 1);
					mva.addQueue(database2);
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