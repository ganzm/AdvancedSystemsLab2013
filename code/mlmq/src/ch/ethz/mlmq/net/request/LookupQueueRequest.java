package ch.ethz.mlmq.net.request;

import java.nio.ByteBuffer;

import ch.ethz.mlmq.net.MlmqSerializable;
import ch.ethz.mlmq.util.ByteBufferUtil;

public class LookupQueueRequest implements Request {

	static final long serialVersionUID = -7648694499744635352L;

	/**
	 * The id of the client we want to find the Queue from
	 */
	private Long clientId = null;

	/**
	 * The name of the queue we want to find
	 */
	private String queueName = null;

	/**
	 * Serialisation/Deserialisation Constructor don't use this
	 */
	public LookupQueueRequest() {
	}

	public LookupQueueRequest(String queueName) {
		this.queueName = queueName;
	}

	public LookupQueueRequest(long clientId) {
		this.clientId = clientId;
	}

	public Long getClientId() {
		return clientId;
	}

	public void setClientId(Long clientId) {
		this.clientId = clientId;
	}

	public String getQueueName() {
		return queueName;
	}

	public void setQueueName(String queueName) {
		this.queueName = queueName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((clientId == null) ? 0 : clientId.hashCode());
		result = prime * result + ((queueName == null) ? 0 : queueName.hashCode());
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
		LookupQueueRequest other = (LookupQueueRequest) obj;
		if (clientId == null) {
			if (other.clientId != null)
				return false;
		} else if (!clientId.equals(other.clientId))
			return false;
		if (queueName == null) {
			if (other.queueName != null)
				return false;
		} else if (!queueName.equals(other.queueName))
			return false;
		return true;
	}

	@Override
	public void serialize(ByteBuffer buffer) {
		ByteBufferUtil.putString(buffer, queueName);
		ByteBufferUtil.putLong(clientId, buffer);
	}

	@Override
	public MlmqSerializable deserialize(ByteBuffer buffer) {
		queueName = ByteBufferUtil.getString(buffer);
		clientId = ByteBufferUtil.getLong(buffer);

		return this;
	}

	@Override
	public int getTypeId() {
		return (int) serialVersionUID;
	}

}
