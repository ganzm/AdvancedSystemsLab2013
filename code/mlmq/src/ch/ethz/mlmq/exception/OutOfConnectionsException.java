package ch.ethz.mlmq.exception;

public class OutOfConnectionsException extends MlmqException {

	private static final long serialVersionUID = -2577917504906800323L;

	public OutOfConnectionsException(String message, Throwable t) {
		super(message, t);
	}

}
