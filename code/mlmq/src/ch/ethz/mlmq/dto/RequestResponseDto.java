package ch.ethz.mlmq.dto;

/**
 * Data Transfer Object
 * 
 * used for data transport between Database and broker
 * 
 */
public class RequestResponseDto {
	private long id;
	private MessageDto request;
	private MessageDto response;

	public long getId() {
		return id;
	}

	public MessageDto getRequest() {
		return request;
	}

	public MessageDto getResponse() {
		return response;
	}

}
