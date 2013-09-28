package ch.ethz.mlmq.server.processing;

import ch.ethz.mlmq.net.response.Response;

public interface ResponseQueue {

	public void enqueue(Response response);
}
