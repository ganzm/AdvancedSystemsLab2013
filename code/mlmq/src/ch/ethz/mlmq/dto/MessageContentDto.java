package ch.ethz.mlmq.dto;

/**
 * Data Transfer Object
 * 
 * used for data transport between Database and broker
 * 
 */
public class MessageContentDto {

	@SuppressWarnings("unused")
	private long id;
	@SuppressWarnings("unused")
	private byte[] content;
	@SuppressWarnings("unused")
	private int prio;
	@SuppressWarnings("unused")
	private ClientDto sender;

}
