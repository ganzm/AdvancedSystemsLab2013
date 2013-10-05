package ch.ethz.mlmq.dto;

import java.io.Serializable;

/**
 * Data Transfer Object
 * 
 * used for data transport between Database and broker
 * 
 */
public class ClientDto implements Serializable {

	private static final long serialVersionUID = 6719281939591243370L;

	private final long id;

	public ClientDto(long id) {
		this.id = id;
	}

	public long getId() {
		return id;
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
		ClientDto other = (ClientDto) obj;
		if (id != other.id)
			return false;
		return true;
	}

}
