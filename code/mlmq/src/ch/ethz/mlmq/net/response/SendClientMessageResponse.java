package ch.ethz.mlmq.net.response;

public class SendClientMessageResponse implements Response {

	private static final long serialVersionUID = 6873497715742459652L;

	private Long conversationContext = null;

	public SendClientMessageResponse() {
	}

	public SendClientMessageResponse(long conversationContext) {
		this.conversationContext = conversationContext;
	}

	public void setConversationContext(Long conversationContext) {
		this.conversationContext = conversationContext;
	}

	public Long getConversationContext() {
		return conversationContext;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((conversationContext == null) ? 0 : conversationContext.hashCode());
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
		SendClientMessageResponse other = (SendClientMessageResponse) obj;
		if (conversationContext == null) {
			if (other.conversationContext != null)
				return false;
		} else if (!conversationContext.equals(other.conversationContext))
			return false;
		return true;
	}

}
