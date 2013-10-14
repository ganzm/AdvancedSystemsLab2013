package ch.ethz.mlmq.net.request;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import ch.ethz.mlmq.net.response.Response;

/**
 * TODO Performance - serialisation/deserialisation needs to be refactored - this is just a quick java serialisation implementation
 */
public class RequestResponseFactory {

	private Map<Integer, Class<? extends Request>> requestTypMap = new HashMap<>();
	private Map<Integer, Class<? extends Response>> responseTypMap = new HashMap<>();

	public RequestResponseFactory() {

		registerRequests();
	}

	private void registerRequests() {

	}

	/**
	 * TODO Performance - refactor serialisation/deserialisation - memory allocation is not necessary
	 * 
	 * @param serializeBuffer
	 * @return
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public Request deserializeRequest(ByteBuffer serializeBuffer) {
		try {
			int requestType = serializeBuffer.getInt();
			Class<? extends Request> requestClass = requestTypMap.get(requestType);
			Request instance = requestClass.newInstance();

			instance.deserialize(serializeBuffer);
			return instance;
		} catch (InstantiationException | IllegalAccessException e) {
			throw new RuntimeException("Error while deserializing Request", e);
		}
	}

	public Response deserializeResponse(ByteBuffer serializeBuffer) {
		try {
			int responseType = serializeBuffer.getInt();
			Class<? extends Response> responseClass = responseTypMap.get(responseType);
			Response instance = responseClass.newInstance();

			instance.deserialize(serializeBuffer);
			return instance;
		} catch (InstantiationException | IllegalAccessException e) {
			throw new RuntimeException("Error while deserializing Request", e);
		}
	}

	// private Object deserializeObject(ByteBuffer serializeBuffer) {
	// int numBytes = serializeBuffer.limit() - serializeBuffer.position();
	//
	// // allocate space and read data from buffer
	// byte[] rawdata = new byte[numBytes];
	// serializeBuffer.get(rawdata);
	//
	// try (ByteArrayInputStream bIn = new ByteArrayInputStream(rawdata); ObjectInputStream oIn = new ObjectInputStream(bIn);
	//
	// ) {
	//
	// return oIn.readObject();
	// } catch (IOException | ClassNotFoundException e) {
	// throw new RuntimeException(e);
	// }
	// }

	public void serializeRequest(Request request, ByteBuffer serializeBuffer) {
		serializeBuffer.putInt(request.getTypeId());
		request.serialize(serializeBuffer);
	}

	public void serializeResponse(Response response, ByteBuffer serializeBuffer) {
		serializeBuffer.putInt(response.getTypeId());
		response.serialize(serializeBuffer);
	}

	// /**
	// * TODO Performance - refactor serialisation/deserialisation - memory allocation is not necessary
	// *
	// *
	// * @param requestExpected
	// * @param serializeBuffer
	// */
	// public void serializeRequest(Request request, ByteBuffer serializeBuffer) {
	// serializeObject(request, serializeBuffer);
	// }
	//
	// public void serializeResponse(Response response, ByteBuffer serializeBuffer) {
	// serializeObject(response, serializeBuffer);
	// }
	//
	// private void serializeObject(Object obj, ByteBuffer serializeBuffer) {
	// try (ByteArrayOutputStream byteOut = new ByteArrayOutputStream(); ObjectOutputStream objectOut = new ObjectOutputStream(byteOut);) {
	// objectOut.writeObject(obj);
	// objectOut.flush();
	//
	// serializeBuffer.put(byteOut.toByteArray());
	//
	// } catch (IOException e) {
	// throw new RuntimeException(e);
	// }
	// }

	/**
	 * writes the lengtht field and serialized request to the buffer
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
		request.serialize(buffer);

		int numBytes = buffer.position() - startPayload;
		int endPosition = buffer.position();
		buffer.position(startPosition);

		// write payload lenght to position 0-3
		buffer.putInt(numBytes);
		buffer.position(endPosition);
	}

	public void serializeResponseWithHeader(Response response, ByteBuffer buffer) {
		int startPosition = buffer.position();

		// write 4 bytes as placeholder
		buffer.putInt(0);

		int startPayload = buffer.position(); // should be startPosition + 4

		// serialize the message to the buffer
		response.serialize(buffer);

		int numBytes = buffer.position() - startPayload;
		int endPosition = buffer.position();
		buffer.position(startPosition);

		// write payload lenght to position 0-3
		buffer.putInt(numBytes);
		buffer.position(endPosition);
	}

}
