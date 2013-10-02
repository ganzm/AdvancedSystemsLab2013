package ch.ethz.mlmq.server.processing;

import java.util.ArrayList;
import java.util.List;

import ch.ethz.mlmq.server.BrokerConfiguration;

public class WorkerPool {

	private final List<Worker> workers;
	private final int workerThreadCount;

	public WorkerPool(BrokerConfiguration config) {
		this.workerThreadCount = config.getWorkerThreadCount();
		this.workers = new ArrayList<>(workerThreadCount);
	}

	public void init() {
		for (int i = 0; i < workerThreadCount; i++) {
			// TODO
			Worker worker = new Worker();
			workers.add(worker);
		}
	}

	public void close() {
		// TODO Auto-generated method stub

	}

}
