package ch.ethz.mlmq.net.response;

public class ExceptionResponse implements Response {

	private static final long serialVersionUID = 5159033818555051347L;

	private final Exception exception;

	public ExceptionResponse(Exception ex) {
		this.exception = ex;
	}

	public Exception getException() {
		return exception;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((exception == null) ? 0 : exception.hashCode());
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
		ExceptionResponse other = (ExceptionResponse) obj;
		if (exception == null) {
			if (other.exception != null)
				return false;
		} else if (!exception.equals(other.exception))
			return false;
		return true;
	}

}
