package ch.ethz.mlmq.dto;

/**
 * Data Transfer Object
 * 
 * used to filter incoming messages.
 */
public class MessageQueryInfoDto {
	private boolean orderByPriority;
	private QueueDto queueFilter;
	private ClientDto clientFilter;
}
