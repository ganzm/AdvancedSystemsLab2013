package ch.ethz.mlmq.net;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.logging.Logger;

import ch.ethz.mlmq.logging.LoggerUtil;
import ch.ethz.mlmq.net.request.Request;
import ch.ethz.mlmq.net.request.RequestResponseFactory;
import ch.ethz.mlmq.net.response.Response;

/**
 * Network Connection of a client which is connected to a broker
 */
public class ClientConnection implements Closeable {

	private static final Logger logger = Logger.getLogger("ClientConnection");

	private final String host;
	private final int port;

	private SocketChannel clientSocket;

	private RequestResponseFactory reqRespFactory;

	/**
	 * Assume a messige is 2kbytes at max, add some extra memory
	 */
	private int CLIENT_IO_BUFFER_CAPACITY = 4000;

	private ByteBuffer ioBuffer = ByteBuffer.allocate(CLIENT_IO_BUFFER_CAPACITY);

	public ClientConnection(String host, int port) {
		this.host = host;
		this.port = port;
		this.reqRespFactory = new RequestResponseFactory();
	}

	public Response submitRequest(Request request) throws IOException {
		Response response = null;
		logger.info("Submitting Request " + request.getClass().getSimpleName());

		writeToSocket(request);

		logger.info("Wait for response");

		int numBytes = -1;
		int responseLenght = -1;
		while ((numBytes = clientSocket.read(ioBuffer)) > 0) {

			if (ioBuffer.position() >= 4 && responseLenght == -1) {
				// there is enough data to read an int
				responseLenght = ioBuffer.getInt();
				logger.fine("Read ResponseLenght " + responseLenght);
			} else if (ioBuffer.position() >= 4 + responseLenght) {
				// enough data received to deserialize the response message

				ioBuffer.flip();
				response = reqRespFactory.deserializeResponse(ioBuffer);
				ioBuffer.compact();

				if (ioBuffer.position() != 0) {
					logger.warning("Discarding " + ioBuffer.position() + " bytes from inputbuffer");
				}
				break;
			}
		}

		if (numBytes <= 0) {
			throw new IOException("Connection remotely closed by host");
		}

		return response;
	}

	/**
	 * Write 4 byte (as int) which indicate the length in bytes of the serialized Request object then writes the serialized object to the socket
	 * 
	 * 
	 * @param request
	 * @throws IOException
	 */
	private void writeToSocket(Request request) throws IOException {

		// startPosition (should be 0)
		int startPosition = ioBuffer.position();

		// write 4 bytes as placeholder
		ioBuffer.putInt(0);

		int startPayload = ioBuffer.position(); // should be startPosition + 4

		// serialize the message to the buffer
		reqRespFactory.serializeRequest(request, ioBuffer);

		int numBytes = ioBuffer.position() - startPayload;
		ioBuffer.mark();
		ioBuffer.position(startPosition);
		ioBuffer.putInt(numBytes);
		ioBuffer.reset();

		// write message to the socket
		ioBuffer.flip();
		clientSocket.write(ioBuffer);

		ioBuffer.clear();
	}

	public void connect() throws UnknownHostException, IOException {

		// Create client SocketChannel
		clientSocket = SocketChannel.open();

		// Connection to host port
		InetSocketAddress adr = new InetSocketAddress(host, port);
		if (!clientSocket.connect(adr)) {
			throw new IOException("Could not connect to " + host + ":" + port);
		}
	}

	public void close() {
		logger.info("Closing Connection to " + host + ":" + port);

		if (clientSocket != null) {
			try {
				clientSocket.close();
			} catch (IOException e) {
				logger.severe("Error while closing Socket to " + host + ":" + port + " " + LoggerUtil.getStackTraceString(e));
			}
		}
	}
}
