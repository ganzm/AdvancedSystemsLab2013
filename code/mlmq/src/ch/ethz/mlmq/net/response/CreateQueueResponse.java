package ch.ethz.mlmq.net.response;

import ch.ethz.mlmq.dto.QueueDto;

public class CreateQueueResponse implements Response {

	private static final long serialVersionUID = 1533054714047954400L;

	private QueueDto queue;

	public QueueDto getQueueDto() {
		// TODO Auto-generated method stub
		return queue;
	}

}
