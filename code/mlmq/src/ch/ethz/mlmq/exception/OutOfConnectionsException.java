package ch.ethz.mlmq.exception;

import java.sql.SQLException;

public class OutOfConnectionsException extends MlmqException {

	private static final long serialVersionUID = -2577917504906800323L;

	public OutOfConnectionsException(String message) {
		super(message);
	}

	public OutOfConnectionsException(String message, SQLException exception) {
		super(message, exception);
	}

}
