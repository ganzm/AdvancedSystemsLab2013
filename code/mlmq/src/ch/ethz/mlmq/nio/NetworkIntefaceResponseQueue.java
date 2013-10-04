package ch.ethz.mlmq.nio;

import java.util.NoSuchElementException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.logging.Logger;

import ch.ethz.mlmq.server.processing.WorkerTask;
import ch.ethz.mlmq.server.processing.WorkerTaskQueue;

/**
 * 
 * Belongs to BrokerNetworkInterface
 * 
 * thats where works on the broker post their responses.
 * 
 */
public class NetworkIntefaceResponseQueue implements WorkerTaskQueue {

	private static final int QUEUE_SIZE = 100;

	private final Logger logger = Logger.getLogger(NetworkIntefaceResponseQueue.class.getSimpleName());
	private ArrayBlockingQueue<WorkerTask> queue = new ArrayBlockingQueue<WorkerTask>(QUEUE_SIZE);
	private Runnable wakeupReactorRunnable;

	protected NetworkIntefaceResponseQueue() {
	}

	public void setWakeupReactorRunnable(Runnable wakeupReactorRunnable) {
		this.wakeupReactorRunnable = wakeupReactorRunnable;
	}

	@Override
	public boolean enqueue(WorkerTask workerTask) {
		queue.add(workerTask);

		// wake up reactor
		if (wakeupReactorRunnable == null) {
			logger.warning("No wakeupReactorRunnable attached to NetworkIntefaceResponseQueue");
		} else {
			wakeupReactorRunnable.run();
		}

		return true;
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
