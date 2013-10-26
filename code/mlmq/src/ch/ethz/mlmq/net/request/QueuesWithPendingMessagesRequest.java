package ch.ethz.mlmq.net.request;

import java.nio.ByteBuffer;

import ch.ethz.mlmq.net.MlmqSerializable;

public class QueuesWithPendingMessagesRequest implements Request {

	static final long serialVersionUID = -4299769935652705291L;

	/**
	 * Limit result to a certain number of queues which contain messages
	 */
	private int maxNumQueues;

	public QueuesWithPendingMessagesRequest() {

	}

	public QueuesWithPendingMessagesRequest(int maxNumQueues) {
		this.maxNumQueues = maxNumQueues;
	}

	public int getMaxNumQueues() {
		return maxNumQueues;
	}

	public void setMaxNumQueues(int maxNumQueues) {
		this.maxNumQueues = maxNumQueues;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + maxNumQueues;
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
		QueuesWithPendingMessagesRequest other = (QueuesWithPendingMessagesRequest) obj;
		if (maxNumQueues != other.maxNumQueues)
			return false;
		return true;
	}

	@Override
	public void serialize(ByteBuffer buffer) {
		buffer.putInt(maxNumQueues);
	}

	@Override
	public MlmqSerializable deserialize(ByteBuffer buffer) {
		maxNumQueues = buffer.getInt();
		return this;
	}

	@Override
	public int getTypeId() {
		return (int) serialVersionUID;
	}

}
