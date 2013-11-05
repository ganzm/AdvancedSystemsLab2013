package ch.ethz.mlmq.exception;

import java.nio.channels.AsynchronousCloseException;

/**
 * Is thrown when a request on a client times out
 * 
 * the connection then is closed
 */
public class MlmqRequestTimeoutException extends MlmqException {

	private static final long serialVersionUID = 8438180066159240225L;

	public MlmqRequestTimeoutException(String msg, AsynchronousCloseException e) {
		super(msg, e);
	}
}
