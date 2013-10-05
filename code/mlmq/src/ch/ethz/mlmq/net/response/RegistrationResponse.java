package ch.ethz.mlmq.net.response;

import ch.ethz.mlmq.dto.ClientDto;

public class RegistrationResponse implements Response {

	private static final long serialVersionUID = -3027176762048543256L;

	private final ClientDto clientDto;

	public RegistrationResponse(ClientDto clientDto) {
		this.clientDto = clientDto;
	}

	public ClientDto getClientDto() {
		return clientDto;
	}
}
