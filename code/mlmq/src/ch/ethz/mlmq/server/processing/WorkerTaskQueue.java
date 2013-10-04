package ch.ethz.mlmq.server.processing;

public interface WorkerTaskQueue {
	boolean enqueue(WorkerTask workerTask);
}
