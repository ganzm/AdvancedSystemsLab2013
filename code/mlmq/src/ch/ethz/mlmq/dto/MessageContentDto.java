package ch.ethz.mlmq.dto;

/**
 * Data Transfer Object
 * 
 * used for data transport between Database and broker
 * 
 */
public class MessageContentDto {

	private long id;
	private byte[] content;
	private int prio;
	private ClientDto sender;

}
