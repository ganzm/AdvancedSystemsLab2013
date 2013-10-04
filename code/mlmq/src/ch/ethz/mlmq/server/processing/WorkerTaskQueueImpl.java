package ch.ethz.mlmq.server.processing;

import java.util.concurrent.ArrayBlockingQueue;

public class WorkerTaskQueueImpl implements WorkerTaskQueue {

	private final ArrayBlockingQueue<WorkerTask> queue;

	public WorkerTaskQueueImpl(int queueSize) {
		this.queue = new ArrayBlockingQueue<>(queueSize);
	}

	/**
	 * Thats where the BrokerNetowkrInterface queues incoming requests
	 */
	@Override
	public boolean enqueue(WorkerTask workerTask) {
		try {
			queue.add(workerTask);
			return true;
		} catch (IllegalStateException ex) {
			// happens when the queue is full
			return false;
		}
	}

	/**
	 * Called bye the Workers.
	 * 
	 * If the Queue is empty a Worker is blocked until it gets a WorkerTask
	 * 
	 * @return
	 */
	public WorkerTask blockingDequeue() {
		try {
			return queue.take();
		} catch (InterruptedException e) {
			return null;
		}
	}

}
