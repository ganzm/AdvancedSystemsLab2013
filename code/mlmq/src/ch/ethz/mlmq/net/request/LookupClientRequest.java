package ch.ethz.mlmq.net.request;

import java.nio.ByteBuffer;

import ch.ethz.mlmq.net.MlmqSerializable;
import ch.ethz.mlmq.util.ByteBufferUtil;

public class LookupClientRequest implements Request {
	static final long serialVersionUID = -7648694499744635353L;

	/**
	 * The name of the cleitn we want to find
	 */
	private String clientName = null;

	/**
	 * Serialisation/Deserialisation Constructor don't use this
	 */
	public LookupClientRequest() {
	}

	public LookupClientRequest(String clientName) {
		this.clientName = clientName;
	}

	public String getClientName() {
		return clientName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((clientName == null) ? 0 : clientName.hashCode());
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
		LookupClientRequest other = (LookupClientRequest) obj;
		if (clientName == null) {
			if (other.clientName != null)
				return false;
		} else if (!clientName.equals(other.clientName))
			return false;
		return true;
	}

	@Override
	public void serialize(ByteBuffer buffer) {
		ByteBufferUtil.putString(buffer, clientName);
	}

	@Override
	public MlmqSerializable deserialize(ByteBuffer buffer) {
		clientName = ByteBufferUtil.getString(buffer);
		return this;
	}

	@Override
	public int getTypeId() {
		return (int) serialVersionUID;
	}
}
