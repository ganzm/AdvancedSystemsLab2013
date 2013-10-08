package ch.ethz.mlmq.net.request;

/**
 * Request is performed whenever a client calls register()
 * 
 * 
 * A RegistrationResponse is returned.
 * 
 * A ClientQueue is created on the database
 * 
 */
public class RegistrationRequest implements Request {

	private static final long serialVersionUID = 7742122658910533817L;
	private final String clientName;

	public RegistrationRequest(String clientName) {
		this.clientName = clientName;
	}

	public String getClientName() {
		return clientName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((clientName == null) ? 0 : clientName.hashCode());
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
		RegistrationRequest other = (RegistrationRequest) obj;
		if (clientName == null) {
			if (other.clientName != null)
				return false;
		} else if (!clientName.equals(other.clientName))
			return false;
		return true;
	}

}
