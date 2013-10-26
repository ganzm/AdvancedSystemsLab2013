package ch.ethz.mlmq.dto;

import java.io.Serializable;
import java.nio.ByteBuffer;

import ch.ethz.mlmq.net.MlmqSerializable;
import ch.ethz.mlmq.util.ByteBufferUtil;

/**
 * Data Transfer Object
 * 
 * used for data transport between Database and broker
 * 
 */
public class QueueDto implements Serializable, MlmqSerializable {

	private static final long serialVersionUID = -27123459784219113L;

	private long id;

	private String name = null;

	public QueueDto() {
		// deserialisation constructor
	}

	public QueueDto(long id) {
		this.id = id;
	}

	public QueueDto(long queueId, String name) {
		this.id = queueId;
		this.name = name;
	}

	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
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
		QueueDto other = (QueueDto) obj;
		if (id != other.id)
			return false;
		return true;
	}

	public void serialize(ByteBuffer buffer) {
		buffer.putLong(id);
		ByteBufferUtil.putString(buffer, name);
	}

	@Override
	public QueueDto deserialize(ByteBuffer buffer) {
		id = buffer.getLong();
		name = ByteBufferUtil.getString(buffer);
		return this;
	}

	@Override
	public String toString() {
		return "Queue Id[" + id + "] Name[" + name + "]";
	}
}
