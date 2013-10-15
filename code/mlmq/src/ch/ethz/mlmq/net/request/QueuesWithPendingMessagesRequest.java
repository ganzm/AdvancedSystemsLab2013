package ch.ethz.mlmq.net.request;

import java.nio.ByteBuffer;

import ch.ethz.mlmq.net.HomeMadeSerializable;

public class QueuesWithPendingMessagesRequest implements Request {

	static final long serialVersionUID = -4299769935652705291L;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result;
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
		return true;
	}

	@Override
	public void serialize(ByteBuffer buffer) {
	}

	@Override
	public HomeMadeSerializable deserialize(ByteBuffer buffer) {
		return this;
	}

	@Override
	public int getTypeId() {
		return (int) serialVersionUID;
	}
}
