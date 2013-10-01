package ch.ethz.mlmq.server.processing;

public interface WorkerTaskQueue {
	void enqueue(WorkerTask workerTask);
}
