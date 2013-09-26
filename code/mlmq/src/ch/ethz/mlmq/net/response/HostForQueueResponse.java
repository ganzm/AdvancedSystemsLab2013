package ch.ethz.mlmq.net.response;

import ch.ethz.mlmq.dto.BrokerDto;

public class HostForQueueResponse implements Response {

	private static final long serialVersionUID = 2459768961059725727L;

	private BrokerDto broker;

	public BrokerDto getBrokerDto() {
		return broker;
	}

}
