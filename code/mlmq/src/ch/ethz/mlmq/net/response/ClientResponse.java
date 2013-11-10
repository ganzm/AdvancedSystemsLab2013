package ch.ethz.mlmq.net.response;

import java.nio.ByteBuffer;

import ch.ethz.mlmq.dto.ClientDto;
import ch.ethz.mlmq.util.ByteBufferUtil;

public class ClientResponse implements Response {

	public static final long serialVersionUID = 1333054714047954400L;

	private ClientDto client;

	public ClientResponse() {

	}

	public ClientResponse(ClientDto client) {
		this.client = client;
	}

	public ClientDto getClientDto() {
		return client;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((client == null) ? 0 : client.hashCode());
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
		ClientResponse other = (ClientResponse) obj;
		if (client == null) {
			if (other.client != null)
				return false;
		} else if (!client.equals(other.client))
			return false;
		return true;
	}

	@Override
	public void serialize(ByteBuffer buffer) {
		ByteBufferUtil.serialize(client, buffer);
	}

	@Override
	public ClientResponse deserialize(ByteBuffer buffer) {
		client = (ClientDto) ByteBufferUtil.deserialize(new ClientDto(), buffer);
		return this;
	}

	@Override
	public int getTypeId() {
		return (int) serialVersionUID;
	}
}
