package ch.ethz.mlmq.net.response;

import ch.ethz.mlmq.dto.ClientDto;

public class RegistrationResponse implements Response {

	private static final long serialVersionUID = -3027176762048543256L;

	private final ClientDto clientDto;

	/**
	 * indicates whether this client was seen the first time or not
	 */
	private boolean isNewClient;

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

}
