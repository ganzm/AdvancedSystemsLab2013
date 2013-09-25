package ch.ethz.mlmq.net.response;

import ch.ethz.mlmq.dto.BrokerDto;

public class HostForQueueResponse implements Response {

	private BrokerDto broker;

	public BrokerDto getBrokerDto() {
		return broker;
	}

}
