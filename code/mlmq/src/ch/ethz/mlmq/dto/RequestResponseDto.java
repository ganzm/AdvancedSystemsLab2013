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

}
