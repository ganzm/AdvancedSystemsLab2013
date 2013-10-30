package ch.ethz.mlmq.nio;

import java.util.NoSuchElementException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.logging.Logger;

import ch.ethz.mlmq.server.processing.WorkerTask;
import ch.ethz.mlmq.server.processing.WorkerTaskQueue;

/**
 * Belongs to the BrokerNetworkInterface
 * 
 * thats where works on the broker post their responses.
 * 
 * @see BrokerNetworkInterface
 */
public class NetworkIntefaceResponseQueue implements WorkerTaskQueue {

	private final Logger logger = Logger.getLogger(NetworkIntefaceResponseQueue.class.getSimpleName());

	private final ArrayBlockingQueue<WorkerTask> queue;

	private Runnable wakeupReactorRunnable;

	protected NetworkIntefaceResponseQueue(int queueSize) {
		this.queue = new ArrayBlockingQueue<WorkerTask>(queueSize);
	}

	public void setWakeupReactorRunnable(Runnable wakeupReactorRunnable) {
		this.wakeupReactorRunnable = wakeupReactorRunnable;
	}

	@Override
	public boolean enqueue(WorkerTask workerTask) {
		boolean success = false;
		try {
			success = queue.add(workerTask);
		} catch (IllegalStateException e) {
			// queue full
			success = false;
		}
		// wake up reactor
		if (wakeupReactorRunnable == null) {
			logger.warning("No wakeupReactorRunnable attached to NetworkIntefaceResponseQueue");
		} else {
			wakeupReactorRunnable.run();
		}

		return success;
	}

	/**
	 * Tries to remove the head of the queue, returns null if the queue is empty. Does not wait
	 * 
	 * @return null if queue is empty
	 */
	public WorkerTask dequeue() {
		try {
			return queue.remove();
		} catch (NoSuchElementException ex) {
			// queue empty
			return null;
		}
	}
}
