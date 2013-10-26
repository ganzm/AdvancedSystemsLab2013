package ch.ethz.mlmq.net.request;

import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import ch.ethz.mlmq.net.response.DeleteQueueResponse;
import ch.ethz.mlmq.net.response.ExceptionResponse;
import ch.ethz.mlmq.net.response.MessageResponse;
import ch.ethz.mlmq.net.response.QueueResponse;
import ch.ethz.mlmq.net.response.QueuesWithPendingMessagesResponse;
import ch.ethz.mlmq.net.response.RegistrationResponse;
import ch.ethz.mlmq.net.response.Response;
import ch.ethz.mlmq.net.response.SendClientMessageResponse;
import ch.ethz.mlmq.net.response.SendMessageResponse;

/**
 * 
 * Request and Response Serialisations
 */
public class RequestResponseFactory {

	private Map<Integer, Class<? extends Request>> requestTypMap = new HashMap<>();
	private Map<Integer, Class<? extends Response>> responseTypMap = new HashMap<>();

	public RequestResponseFactory() {
		registerRequests();
	}

	private void registerRequests() {
		registerRequest(CreateQueueRequest.class);
		registerRequest(DeleteQueueRequest.class);
		registerRequest(DequeueMessageRequest.class);
		registerRequest(PeekMessageRequest.class);
		registerRequest(QueuesWithPendingMessagesRequest.class);
		registerRequest(RegistrationRequest.class);
		registerRequest(SendClientMessageRequest.class);
		registerRequest(SendMessageRequest.class);
		registerRequest(LookupQueueRequest.class);

		registerResponse(QueueResponse.class);
		registerResponse(DeleteQueueResponse.class);
		registerResponse(ExceptionResponse.class);
		registerResponse(MessageResponse.class);
		registerResponse(QueuesWithPendingMessagesResponse.class);
		registerResponse(RegistrationResponse.class);
		registerResponse(SendClientMessageResponse.class);
		registerResponse(SendMessageResponse.class);
	}

	private void registerRequest(Class<? extends Request> clazz) {
		try {
			int key = getSerialVersionUIDFromClass(clazz);
			Class<? extends Request> result = requestTypMap.put(key, clazz);
			if (result != null) {
				throw new RuntimeException(clazz + " conflicts with " + result + " - serialVersionUID from both classes are identical (if casted to int)");
			}
		} catch (Exception e) {
			throw new RuntimeException("Error while registering Request " + clazz, e);
		}
	}

	private void registerResponse(Class<? extends Response> clazz) {
		try {
			int key = getSerialVersionUIDFromClass(clazz);
			Class<? extends Response> result = responseTypMap.put(key, clazz);
			if (result != null) {
				throw new RuntimeException(clazz + " conflicts with " + result + " - serialVersionUID from both classes are identical (if casted to int)");
			}
		} catch (Exception e) {
			throw new RuntimeException("Error while registering Request " + clazz, e);
		}
	}

	private int getSerialVersionUIDFromClass(Class<?> clazz) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		String fieldName = "serialVersionUID";
		Field serialVersionUIDField = clazz.getDeclaredField(fieldName);
		return (int) serialVersionUIDField.getLong(null);
	}

	/**
	 * @param serializeBuffer
	 * @return
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public Request deserializeRequest(ByteBuffer serializeBuffer) {
		int requestType = 0;
		Class<? extends Request> requestClass = null;
		try {
			requestType = serializeBuffer.getInt();
			requestClass = requestTypMap.get(requestType);
			Request instance = requestClass.newInstance();

			instance.deserialize(serializeBuffer);
			return instance;
		} catch (Exception e) {
			throw new RuntimeException("Error while deserializing Request Type [" + requestType + "] Class[" + requestClass + "]", e);
		}
	}

	public Response deserializeResponse(ByteBuffer serializeBuffer) {
		Class<? extends Response> responseClass = null;
		int responseType = 0;
		try {
			responseType = serializeBuffer.getInt();
			responseClass = responseTypMap.get(responseType);
			Response instance = responseClass.newInstance();

			instance.deserialize(serializeBuffer);
			return instance;
		} catch (Exception e) {
			throw new RuntimeException("Error while deserializing Request Type[" + responseType + "] Class[" + responseClass + "]", e);
		}
	}

	public void serializeRequest(Request request, ByteBuffer serializeBuffer) {
		serializeBuffer.putInt(request.getTypeId());
		request.serialize(serializeBuffer);
	}

	public void serializeResponse(Response response, ByteBuffer serializeBuffer) {
		serializeBuffer.putInt(response.getTypeId());
		response.serialize(serializeBuffer);
	}

	/**
	 * writes the length field and serialized request to the buffer
	 * 
	 * @param request
	 * @param buffer
	 */
	public void serializeRequestWithHeader(Request request, ByteBuffer buffer) {
		int startPosition = buffer.position();

		// write 4 bytes as placeholder
		buffer.putInt(0);

		int startPayload = buffer.position(); // should be startPosition + 4

		// serialize the message to the buffer
		buffer.putInt(request.getTypeId());
		request.serialize(buffer);

		int numBytes = buffer.position() - startPayload;
		int endPosition = buffer.position();
		buffer.position(startPosition);

		// write payload length to position 0-3
		buffer.putInt(numBytes);
		buffer.position(endPosition);
	}

	public void serializeResponseWithHeader(Response response, ByteBuffer buffer) {
		int startPosition = buffer.position();

		// write 4 bytes as placeholder
		buffer.putInt(0);

		int startPayload = buffer.position(); // should be startPosition + 4

		// serialize the message to the buffer
		buffer.putInt(response.getTypeId());
		response.serialize(buffer);

		int numBytes = buffer.position() - startPayload;
		int endPosition = buffer.position();
		buffer.position(startPosition);

		// write payload length to position 0-3
		buffer.putInt(numBytes);
		buffer.position(endPosition);
	}

}
