package ch.ethz.mlmq.net.request;

import java.nio.ByteBuffer;

import ch.ethz.mlmq.dto.QueueDto;
import ch.ethz.mlmq.util.ByteBufferUtil;

public class DeleteQueueRequest implements Request {

	static final long serialVersionUID = -689440274911871604L;

	private QueueDto queue;

	public DeleteQueueRequest() {

	}

	public DeleteQueueRequest(long queueId) {
		queue = new QueueDto(queueId);
	}

	public long getQueueId() {
		return queue.getId();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((queue == null) ? 0 : queue.hashCode());
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
		DeleteQueueRequest other = (DeleteQueueRequest) obj;
		if (queue == null) {
			if (other.queue != null)
				return false;
		} else if (!queue.equals(other.queue))
			return false;
		return true;
	}

	@Override
	public void serialize(ByteBuffer buffer) {
		ByteBufferUtil.serialize(queue, buffer);
	}

	@Override
	public DeleteQueueRequest deserialize(ByteBuffer serializeBuffer) {
		queue = (QueueDto) ByteBufferUtil.deserialize(new QueueDto(), serializeBuffer);
		return this;
	}

	@Override
	public int getTypeId() {
		return (int) serialVersionUID;
	}
}
