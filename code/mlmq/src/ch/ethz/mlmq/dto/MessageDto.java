package ch.ethz.mlmq.dto;

/**
 * Data Transfer Object
 * 
 * used for data transport between Database and broker
 * 
 */
public class MessageDto {

	private long id;
	private MessageContentDto messageContent;
	private QueueDto queue;

}
