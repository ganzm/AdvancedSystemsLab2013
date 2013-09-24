package ch.ethz.mlmq.http.response;

import ch.ethz.mlmq.dto.ClientDto;

public class RegistrationResponse implements Response {

	private ClientDto clientDto;

	public ClientDto getClientDto() {
		return clientDto;
	}

}
