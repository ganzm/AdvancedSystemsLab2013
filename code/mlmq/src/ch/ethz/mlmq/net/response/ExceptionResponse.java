package ch.ethz.mlmq.net.response;

import java.nio.ByteBuffer;

import ch.ethz.mlmq.net.HomeMadeSerializable;
import ch.ethz.mlmq.util.ByteBufferUtil;

public class ExceptionResponse implements Response {

	public static final long serialVersionUID = 5159033818555051347L;

	private Exception exception;

	public ExceptionResponse() {

	}

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

	@Override
	public void serialize(ByteBuffer buffer) {
		ByteBufferUtil.serializeAny(exception, buffer);
	}

	@Override
	public HomeMadeSerializable deserialize(ByteBuffer buffer) {
		exception = (Exception) ByteBufferUtil.deserializeAny(buffer);
		return this;
	}

	@Override
	public int getTypeId() {
		return (int) serialVersionUID;
	}

}
