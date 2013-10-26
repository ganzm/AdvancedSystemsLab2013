package ch.ethz.mlmq.dto;

import java.io.Serializable;
import java.nio.ByteBuffer;

import ch.ethz.mlmq.net.MlmqSerializable;

/**
 * Data Transfer Object
 * 
 * used for data transport between Database and broker
 * 
 */
public class QueueDto implements Serializable, MlmqSerializable {

	private static final long serialVersionUID = -27123459784219113L;

	private long id;

	public QueueDto() {
		// deserialisation constructor
	}

	public QueueDto(long id) {
		this.setId(id);
	}

	public long getId() {
		return id;
	}

	private void setId(long id) {
		this.id = id;
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
	}

	@Override
	public QueueDto deserialize(ByteBuffer serializeBuffer) {
		id = serializeBuffer.getLong();
		return this;
	}

	@Override
	public String toString() {
		return "Queue Id[" + id + "]";
	}
}
