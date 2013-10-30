package ch.ethz.mlmq.server.processing;

public interface WorkerTaskQueue {

	/**
	 * 
	 * @param workerTask
	 * @return true if WorkerTask was successfully enqueued, false if queue was full
	 */
	boolean enqueue(WorkerTask workerTask);
}
