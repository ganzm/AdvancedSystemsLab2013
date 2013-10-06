package ch.ethz.mlmq.net.response;

import ch.ethz.mlmq.dto.BrokerDto;

public class HostForQueueResponse implements Response {

	private static final long serialVersionUID = 2459768961059725727L;

	private BrokerDto broker;

	public BrokerDto getBrokerDto() {
		return broker;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((broker == null) ? 0 : broker.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		HostForQueueResponse other = (HostForQueueResponse) obj;
		if (broker == null) {
			if (other.broker != null)
				return false;
		} else if (!broker.equals(other.broker))
			return false;
		return true;
	}

}
