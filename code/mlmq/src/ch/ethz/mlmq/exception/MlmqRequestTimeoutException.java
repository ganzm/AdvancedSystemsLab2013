package ch.ethz.mlmq.exception;

/**
 * Is thrown when a request on a client times out
 * 
 * the connection then is closed
 */
public class MlmqRequestTimeoutException extends MlmqException {

	private static final long serialVersionUID = 8438180066159240225L;

	public MlmqRequestTimeoutException(String msg, Throwable t) {
		super(msg, t);
	}
}
