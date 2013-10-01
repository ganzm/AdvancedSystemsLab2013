package ch.ethz.mlmq.nio;

import java.util.concurrent.ArrayBlockingQueue;

import ch.ethz.mlmq.net.response.Response;
import ch.ethz.mlmq.server.processing.ResponseQueue;

/**
 * Used by BrokerNetworkInterface
 * 
 * thats where works on the broker post their responses
 * 
 */
public class NetworkIntefaceResponseQueue implements ResponseQueue {

	protected NetworkIntefaceResponseQueue() {
	}

	private ArrayBlockingQueue<Response> queue = new ArrayBlockingQueue<Response>(100);

	@Override
	public void enqueue(Response response) {
		queue.add(response);

		// wake up reactor

		// reactor
	}

}
