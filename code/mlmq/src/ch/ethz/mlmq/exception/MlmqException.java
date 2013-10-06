package ch.ethz.mlmq.exception;

public class MlmqException extends Exception {

	private static final long serialVersionUID = 1062440408012876553L;

	public MlmqException(String message, Throwable t) {
		super(message, t);
	}

	public MlmqException(String message) {
		super(message);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getMessage() == null) ? 0 : getMessage().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MlmqException other = (MlmqException) obj;
		if (getMessage() == null) {
			if (other.getMessage() != null)
				return false;
		} else if (!getMessage().equals(other.getMessage()))
			return false;
		return true;
	}
}
