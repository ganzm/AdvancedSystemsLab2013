package ch.ethz.mlmq.net.request;

import java.nio.ByteBuffer;

import ch.ethz.mlmq.util.ByteBufferUtil;

/**
 * Request is performed whenever a client wants to create any new Message Queue
 * 
 * The queue created is not associated to any specific client, anyone can write or read from it
 * 
 * CreateQueueResponse is returned
 * 
 */
public class CreateQueueRequest implements Request {

	private static final long serialVersionUID = 5493920307572674123L;

	private String queueName;

	public CreateQueueRequest(String queueName) {
		this.queueName = queueName;
	}

	public String getQueueName() {
		return queueName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
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
		CreateQueueRequest other = (CreateQueueRequest) obj;
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
	}

	@Override
	public void deserialize(ByteBuffer serializeBuffer) {
		queueName = ByteBufferUtil.getString(serializeBuffer);
	}

	@Override
	public int getTypeId() {
		return (int) serialVersionUID;
	}

}
