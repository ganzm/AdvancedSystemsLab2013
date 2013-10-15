package ch.ethz.mlmq.net.response;

import java.nio.ByteBuffer;

import ch.ethz.mlmq.dto.ClientDto;
import ch.ethz.mlmq.net.HomeMadeSerializable;
import ch.ethz.mlmq.util.ByteBufferUtil;

public class RegistrationResponse implements Response {

	public static final long serialVersionUID = -3027176762048543256L;

	private ClientDto clientDto;

	/**
	 * indicates whether this client was seen the first time or not
	 */
	private boolean isNewClient;

	public RegistrationResponse() {

	}

	public RegistrationResponse(ClientDto clientDto) {
		this.clientDto = clientDto;
	}

	public ClientDto getClientDto() {
		return clientDto;
	}

	public boolean isNewClient() {
		return isNewClient;
	}

	public void setNewClient(boolean isNewClient) {
		this.isNewClient = isNewClient;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((clientDto == null) ? 0 : clientDto.hashCode());
		result = prime * result + (isNewClient ? 1231 : 1237);
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
		RegistrationResponse other = (RegistrationResponse) obj;
		if (clientDto == null) {
			if (other.clientDto != null)
				return false;
		} else if (!clientDto.equals(other.clientDto))
			return false;
		if (isNewClient != other.isNewClient)
			return false;
		return true;
	}

	@Override
	public void serialize(ByteBuffer buffer) {
		ByteBufferUtil.serialize(clientDto, buffer);
		ByteBufferUtil.putBoolean(isNewClient, buffer);
	}

	@Override
	public HomeMadeSerializable deserialize(ByteBuffer buffer) {
		clientDto = (ClientDto) ByteBufferUtil.deserialize(new ClientDto(), buffer);
		isNewClient = ByteBufferUtil.getBoolean(buffer);
		return this;
	}

	@Override
	public int getTypeId() {
		return (int) serialVersionUID;
	}
}
