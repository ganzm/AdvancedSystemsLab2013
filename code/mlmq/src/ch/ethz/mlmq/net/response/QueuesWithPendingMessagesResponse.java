package ch.ethz.mlmq.net.response;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import ch.ethz.mlmq.dto.QueueDto;
import ch.ethz.mlmq.net.MlmqSerializable;
import ch.ethz.mlmq.util.ByteBufferUtil;

public class QueuesWithPendingMessagesResponse implements Response {

	public static final long serialVersionUID = 4798365845603757482L;

	/**
	 * public queues which contain messages
	 */
	private List<QueueDto> queues;

	/**
	 * Number of messages which are in the clients personal queue
	 */
	private int numMessagesInMyQueue;

	public QueuesWithPendingMessagesResponse() {
		this.queues = new ArrayList<>();
	}

	public List<QueueDto> getQueues() {
		return queues;
	}

	public void setQueues(List<QueueDto> queues) {
		this.queues = queues;
	}

	public int getNumMessagesInMyQueue() {
		return numMessagesInMyQueue;
	}

	public void setNumMessagesInMyQueue(int numMessagesInMyQueue) {
		this.numMessagesInMyQueue = numMessagesInMyQueue;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + numMessagesInMyQueue;
		result = prime * result + ((queues == null) ? 0 : queues.hashCode());
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
		QueuesWithPendingMessagesResponse other = (QueuesWithPendingMessagesResponse) obj;
		if (numMessagesInMyQueue != other.numMessagesInMyQueue)
			return false;
		if (queues == null) {
			if (other.queues != null)
				return false;
		} else if (!queues.equals(other.queues))
			return false;
		return true;
	}

	@Override
	public void serialize(ByteBuffer buffer) {
		buffer.putInt(numMessagesInMyQueue);
		buffer.putInt(queues.size());
		for (QueueDto queue : queues) {
			ByteBufferUtil.serialize(queue, buffer);
		}
	}

	@Override
	public MlmqSerializable deserialize(ByteBuffer buffer) {
		numMessagesInMyQueue = buffer.getInt();
		int listSize = buffer.getInt();
		for (int i = 0; i < listSize; i++) {
			queues.add((QueueDto) ByteBufferUtil.deserialize(new QueueDto(), buffer));
		}

		return this;
	}

	@Override
	public int getTypeId() {
		return (int) serialVersionUID;
	}

}
