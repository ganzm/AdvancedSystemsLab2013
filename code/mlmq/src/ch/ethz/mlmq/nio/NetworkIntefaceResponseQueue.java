package ch.ethz.mlmq.nio;

import java.util.ArrayList;
import java.util.List;
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
	public void enqueue(WorkerTask workerTask) {
		queue.add(workerTask);

		// wake up reactor
		if (wakeupReactorRunnable == null) {
			logger.warning("No wakeupReactorRunnable attached to NetworkIntefaceResponseQueue");
		} else {
			wakeupReactorRunnable.run();
		}
	}

	public WorkerTask peek() {
		return queue.peek();
	}

	/**
	 * TODO
	 * 
	 * returns ClientIds of messages which have been enqueue since our last call
	 * 
	 * @return
	 */
	public List<Integer> getNewIds() {
		return new ArrayList<>();

	}

}
