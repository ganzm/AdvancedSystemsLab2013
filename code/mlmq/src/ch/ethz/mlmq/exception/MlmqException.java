package ch.ethz.mlmq.exception;

public class MlmqException extends Exception {

	private static final long serialVersionUID = 1062440408012876553L;

	public MlmqException(String message, Throwable t) {
		super(message, t);
	}
}
