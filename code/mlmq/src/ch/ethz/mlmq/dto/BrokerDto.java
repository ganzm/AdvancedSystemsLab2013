package ch.ethz.mlmq.dto;

import java.io.Serializable;

/**
 * Data Transfer Object
 * 
 * used for data transport between Database and broker
 * 
 */
public class BrokerDto implements Serializable {
	private static final long serialVersionUID = 2397258276459710559L;

	private String host;
	private int port;

	public BrokerDto(String host, int port) {
		this.host = host;
		this.port = port;
	}

	public BrokerDto() {
	}

	@Override
	public String toString() {
		return host + ":" + port;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((host == null) ? 0 : host.hashCode());
		result = prime * result + port;
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
		BrokerDto other = (BrokerDto) obj;
		if (host == null) {
			if (other.host != null)
				return false;
		} else if (!host.equals(other.host))
			return false;
		if (port != other.port)
			return false;
		return true;
	}
}
