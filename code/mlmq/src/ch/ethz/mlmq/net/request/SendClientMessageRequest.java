package ch.ethz.mlmq.net.request;

import java.nio.ByteBuffer;
import java.util.Arrays;

import ch.ethz.mlmq.net.MlmqSerializable;
import ch.ethz.mlmq.util.ByteBufferUtil;

public class SendClientMessageRequest implements Request {

	static final long serialVersionUID = 5365826671308061202L;

	/**
	 * Receiving client
	 */
	private long clientId;

	private byte[] content;

	private int prio;

	/**
	 * indicates whether this is a Request/Response Conversation between two clients or not
	 * 
	 * If this flag is set to true, the corresponding SendclientMessageResponse returns a conversationContext
	 */
	private boolean conversation;

	/**
	 * optional conversation context
	 */
	private Long conversationContext;

	public SendClientMessageRequest() {

	}

	public SendClientMessageRequest(long clientId, byte[] content, int prio) {
		this(clientId, content, prio, false);
	}

	/**
	 * Use this constructor if you want to start a request response conversation with an other client
	 * 
	 * @param clientId
	 * @param content
	 * @param prio
	 * @param conversation
	 */
	public SendClientMessageRequest(long clientId, byte[] content, int prio, boolean conversation) {
		this.clientId = clientId;
		this.content = content;
		this.prio = prio;
		this.conversation = conversation;

	}

	/**
	 * Use this constructor if you want to respond to a request response message sent from an other client
	 * 
	 * @param clientId
	 * @param content
	 * @param prio
	 * @param context
	 */
	public SendClientMessageRequest(long clientId, byte[] content, int prio, long context) {
		this(clientId, content, prio, true, context);
	}

	private SendClientMessageRequest(long clientId, byte[] content, int prio, boolean conversation, Long conversationContext) {
		this.clientId = clientId;
		this.content = content;
		this.prio = prio;
		this.conversation = conversation;
		this.conversationContext = conversationContext;
	}

	public byte[] getContent() {
		return content;
	}

	public int getPrio() {
		return prio;
	}

	public long getClientId() {
		return clientId;
	}

	public boolean isConversation() {
		return conversation;
	}

	public Long getConversationContext() {
		return conversationContext;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (clientId ^ (clientId >>> 32));
		result = prime * result + Arrays.hashCode(content);
		result = prime * result + (conversation ? 1231 : 1237);
		result = prime * result + ((conversationContext == null) ? 0 : conversationContext.hashCode());
		result = prime * result + prio;
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
		SendClientMessageRequest other = (SendClientMessageRequest) obj;
		if (clientId != other.clientId)
			return false;
		if (!Arrays.equals(content, other.content))
			return false;
		if (conversation != other.conversation)
			return false;
		if (conversationContext == null) {
			if (other.conversationContext != null)
				return false;
		} else if (!conversationContext.equals(other.conversationContext))
			return false;
		if (prio != other.prio)
			return false;
		return true;
	}

	@Override
	public void serialize(ByteBuffer buffer) {
		buffer.putLong(clientId);
		buffer.putInt(prio);
		ByteBufferUtil.putBoolean(conversation, buffer);
		ByteBufferUtil.putLong(conversationContext, buffer);
		ByteBufferUtil.putByteArray(content, buffer);
	}

	@Override
	public MlmqSerializable deserialize(ByteBuffer buffer) {
		clientId = buffer.getLong();
		prio = buffer.getInt();
		conversation = ByteBufferUtil.getBoolean(buffer);
		conversationContext = ByteBufferUtil.getLong(buffer);
		content = ByteBufferUtil.getByteArray(buffer);

		return this;
	}

	@Override
	public int getTypeId() {
		return (int) serialVersionUID;
	}
}
