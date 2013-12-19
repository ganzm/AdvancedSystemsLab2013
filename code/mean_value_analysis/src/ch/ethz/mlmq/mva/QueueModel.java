package ch.ethz.mlmq.mva;

public class QueueModel {

	public static void main(String[] args) {
		Mva mva = new Mva();
		int codeTime = 2;
		int maxClients = 256;
		int maxQ = 100;
		int Z = codeTime;

		for (int Q = 1; Q <= maxQ; Q += 1) {
			System.out.print(Q + " ");
			for (int N = 256; N <= maxClients; N += 32) {

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

				Queue database = new Queue("DB put", 60, 0.5, Queue.Type.loadDependent, 4 * 20, maxClients + 1);
				mva.addQueue(database);

				Queue database2 = new Queue("DB retrieve", 70, 0.5, Queue.Type.loadDependent, 4 * 20, maxClients + 1);
				mva.addQueue(database2);

				for (int q = 1; q <= Q; q++) {
					Queue queue = new Queue("Queue" + 1, codeTime, 1.0 / Q, Queue.Type.fixedCapacity, 1, maxClients + 1);
					mva.addQueue(queue);
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
