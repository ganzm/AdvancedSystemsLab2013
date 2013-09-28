package ch.ethz.mlmq.server.processing;

import ch.ethz.mlmq.net.request.Request;

/**
 * Request which is processed by a Worker on the broker
 */
public class RequestJob implements Runnable {

	private final Request request;

	/**
	 * Queue where we put our response when we are finished processing the request
	 */
	private final ResponseQueue responseQueue;

	public RequestJob(Request request, ResponseQueue responseQueue) {
		this.request = request;
		this.responseQueue = responseQueue;
	}

	public void run() {

	}
}
