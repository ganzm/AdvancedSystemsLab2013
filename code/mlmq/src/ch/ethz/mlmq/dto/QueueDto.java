package ch.ethz.mlmq.dto;

/**
 * Data Transfer Object
 * 
 * used for data transport between Database and broker
 * 
 */
public class QueueDto {
	public QueueDto(long id) {
		this.setId(id);
	}

	public long getId() {
		return id;
	}

	private void setId(long id) {
		this.id = id;
	}

	private long id;
}
