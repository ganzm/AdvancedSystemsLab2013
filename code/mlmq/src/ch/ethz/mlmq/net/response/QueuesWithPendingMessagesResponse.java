package ch.ethz.mlmq.net.response;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import ch.ethz.mlmq.dto.QueueDto;
import ch.ethz.mlmq.net.HomeMadeSerializable;
import ch.ethz.mlmq.util.ByteBufferUtil;

public class QueuesWithPendingMessagesResponse implements Response {

	public static final long serialVersionUID = 4798365845603757482L;

	private List<QueueDto> queues;

	public QueuesWithPendingMessagesResponse() {
		this.queues = new ArrayList<>();
	}

	public List<QueueDto> getQueues() {
		return queues;
	}

	public void setQueues(List<QueueDto> queues) {
		this.queues = queues;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
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
		if (queues == null) {
			if (other.queues != null)
				return false;
		} else if (!queues.equals(other.queues))
			return false;
		return true;
	}

	@Override
	public void serialize(ByteBuffer buffer) {
		buffer.putInt(queues.size());
		for (QueueDto queue : queues) {
			ByteBufferUtil.serialize(queue, buffer);
		}
	}

	@Override
	public HomeMadeSerializable deserialize(ByteBuffer buffer) {
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
