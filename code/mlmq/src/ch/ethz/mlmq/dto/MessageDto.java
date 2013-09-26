package ch.ethz.mlmq.dto;

/**
 * Data Transfer Object
 * 
 * used for data transport between Database and broker
 * 
 */
public class MessageDto {

	@SuppressWarnings("unused")
	private long id;
	@SuppressWarnings("unused")
	private MessageContentDto messageContent;
	@SuppressWarnings("unused")
	private QueueDto queue;

}
