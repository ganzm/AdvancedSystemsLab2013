package ch.ethz.mlmq.dto;

import java.io.Serializable;

/**
 * Data Transfer Object
 * 
 * used for data transport between Database and broker
 * 
 */
public class QueueDto implements Serializable {
	private static final long serialVersionUID = -27123459784219113L;

	private long id;

	public QueueDto(long id) {
		this.setId(id);
	}

	public long getId() {
		return id;
	}

	private void setId(long id) {
		this.id = id;
	}

}
