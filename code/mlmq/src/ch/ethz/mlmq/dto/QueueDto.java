package ch.ethz.mlmq.dto;

/**
 * Data Transfer Object
 * 
 * used for data transport between Database and broker
 * 
 */
public class QueueDto {
	public QueueDto(long id) {
		this.id = id;
	}

	private long id;
}
