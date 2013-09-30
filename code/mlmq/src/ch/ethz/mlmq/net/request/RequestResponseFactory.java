package ch.ethz.mlmq.net.request;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;

import ch.ethz.mlmq.net.response.Response;

/**
 * TODO Performance - serialisation/deserialisation needs to be refactored - this is just a quick java serialisation implementation
 */
public class RequestResponseFactory {

	/**
	 * TODO Performance - refactor serialisation/deserialisation - memory allocation is not necessary
	 * 
	 * @param serializeBuffer
	 * @return
	 */
	public Request deserializeRequest(ByteBuffer serializeBuffer) {
		return (Request) deserializeObject(serializeBuffer);
	}

	public Response deserializeResponse(ByteBuffer serializeBuffer) {
		return (Response) deserializeObject(serializeBuffer);
	}

	private Object deserializeObject(ByteBuffer serializeBuffer) {
		int numBytes = serializeBuffer.limit() - serializeBuffer.position();

		// allocate space and read data from buffer
		byte[] rawdata = new byte[numBytes];
		serializeBuffer.get(rawdata);

		try (ByteArrayInputStream bIn = new ByteArrayInputStream(rawdata); ObjectInputStream oIn = new ObjectInputStream(bIn);

		) {

			return oIn.readObject();
		} catch (IOException | ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * TODO Performance - refactor serialisation/deserialisation - memory allocation is not necessary
	 * 
	 * 
	 * @param requestExpected
	 * @param serializeBuffer
	 */
	public void serializeRequest(Request request, ByteBuffer serializeBuffer) {
		serializeObject(request, serializeBuffer);
	}

	public void serializeResponse(Response response, ByteBuffer serializeBuffer) {
		serializeObject(response, serializeBuffer);
	}

	private void serializeObject(Object obj, ByteBuffer serializeBuffer) {
		try (ByteArrayOutputStream byteOut = new ByteArrayOutputStream(); ObjectOutputStream objectOut = new ObjectOutputStream(byteOut);) {
			objectOut.writeObject(obj);
			objectOut.flush();

			serializeBuffer.put(byteOut.toByteArray());

		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
