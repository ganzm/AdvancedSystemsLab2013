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
public class ClientDto implements Serializable, MlmqSerializable {

	private static final long serialVersionUID = 6719281939591243370L;

	private long id;

	/**
	 * name of the client
	 */
	private String name;

	public ClientDto() {

	}

	public ClientDto(long id) {
		this.id = id;
	}

	public ClientDto(int id, String name) {
		this.id = id;
		this.name = name;
	}

	@Override
	public String toString() {
		return id + " " + name;
	}

	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		ClientDto other = (ClientDto) obj;
		if (id != other.id)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public void serialize(ByteBuffer buffer) {
		buffer.putLong(id);
		ByteBufferUtil.putString(buffer, name);
	}

	@Override
	public MlmqSerializable deserialize(ByteBuffer buffer) {
		id = buffer.getLong();
		name = ByteBufferUtil.getString(buffer);
		return this;
	}

}
