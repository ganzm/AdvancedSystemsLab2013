package ch.ethz.mlmq.net.response;

import java.nio.ByteBuffer;

import ch.ethz.mlmq.dto.QueueDto;
import ch.ethz.mlmq.net.request.CreateQueueRequest;
import ch.ethz.mlmq.net.request.LookupQueueRequest;
import ch.ethz.mlmq.util.ByteBufferUtil;

/**
 * @see CreateQueueRequest
 * @see LookupQueueRequest
 */
public class QueueResponse implements Response {

	public static final long serialVersionUID = 1533054714047954400L;

	private QueueDto queue;

	public QueueResponse() {

	}

	public QueueResponse(QueueDto queue) {
		this.queue = queue;
	}

	public QueueDto getQueueDto() {
		return queue;
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
		QueueResponse other = (QueueResponse) obj;
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
	public QueueResponse deserialize(ByteBuffer buffer) {
		queue = (QueueDto) ByteBufferUtil.deserialize(new QueueDto(), buffer);
		return this;
	}

	@Override
	public int getTypeId() {
		return (int) serialVersionUID;
	}
}
