package ch.ethz.mlmq.nio;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.logging.Logger;

import ch.ethz.mlmq.net.response.Response;
import ch.ethz.mlmq.server.processing.ResponseQueue;

/**
 * Used by BrokerNetworkInterface
 * 
 * thats where works on the broker post their responses
 * 
 */
public class NetworkIntefaceResponseQueue implements ResponseQueue {
	private final Logger logger = Logger.getLogger(NetworkIntefaceResponseQueue.class.getSimpleName());
	private ArrayBlockingQueue<Response> queue = new ArrayBlockingQueue<Response>(100);
	private Runnable wakeupReactorRunnable;

	protected NetworkIntefaceResponseQueue() {
	}

	public void setWakeupReactorRunnable(Runnable wakeupReactorRunnable) {
		this.wakeupReactorRunnable = wakeupReactorRunnable;
	}

	@Override
	public void enqueue(Response response) {
		queue.add(response);

		// wake up reactor
		if (wakeupReactorRunnable == null) {
			logger.warning("No wakeupReactorRunnable attached to NetworkIntefaceResponseQueue");
		} else {
			wakeupReactorRunnable.run();
		}
	}
}
