package ch.ethz.mlmq.net.request;

public class QueuesWithPendingMessagesRequest implements Request {

	private static final long serialVersionUID = -4299769935652705291L;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result;
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
		return true;
	}
}
