package ch.ethz.mlmq.net.request;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ch.ethz.mlmq.net.MlmqSerializable;
import ch.ethz.mlmq.util.ByteBufferUtil;

public class SendMessageRequest implements Request {

	static final long serialVersionUID = -8471979592749732204L;

	/**
	 * Target queues
	 */
	private List<Long> queueIds;

	private byte[] content;

	private int prio;

	public SendMessageRequest() {
		this.queueIds = new ArrayList<>();
	}

	public SendMessageRequest(long queueId, byte[] content, int prio) {
		this.queueIds = new ArrayList<>();
		this.content = content;
		this.prio = prio;

		this.queueIds.add(queueId);
	}

	public SendMessageRequest(List<Long> queueIds, byte[] content, int prio) {
		this.queueIds = queueIds;
		this.content = content;
		this.prio = prio;
	}

	public byte[] getContent() {
		return content;
	}

	public int getPrio() {
		return prio;
	}

	public List<Long> getQueueIds() {
		return queueIds;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(content);
		result = prime * result + prio;
		result = prime * result + ((queueIds == null) ? 0 : queueIds.hashCode());
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
		SendMessageRequest other = (SendMessageRequest) obj;
		if (!Arrays.equals(content, other.content))
			return false;
		if (prio != other.prio)
			return false;
		if (queueIds == null) {
			if (other.queueIds != null)
				return false;
		} else if (!queueIds.equals(other.queueIds))
			return false;
		return true;
	}

	@Override
	public void serialize(ByteBuffer buffer) {
		buffer.putInt(queueIds.size());
		for (Long l : queueIds) {
			ByteBufferUtil.putLong(l, buffer);
		}

		ByteBufferUtil.putByteArray(content, buffer);
		buffer.putInt(prio);

	}

	@Override
	public MlmqSerializable deserialize(ByteBuffer buffer) {
		int queueIdsSize = buffer.getInt();
		for (int i = 0; i < queueIdsSize; i++) {
			queueIds.add(ByteBufferUtil.getLong(buffer));
		}

		content = ByteBufferUtil.getByteArray(buffer);
		prio = buffer.getInt();

		return this;
	}

	@Override
	public int getTypeId() {
		return (int) serialVersionUID;
	}

}
