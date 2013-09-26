package ch.ethz.mlmq.net.request;


public class SendMessageRequest implements QueueRequest {

	private static final long serialVersionUID = 5365826671308061202L;

	private long queueId;

	public SendMessageRequest(long queueId, byte[] content, int prio) {
		this.queueId = queueId;
	}

	@Override
	public long getQueueId() {
		return queueId;
	}

}
