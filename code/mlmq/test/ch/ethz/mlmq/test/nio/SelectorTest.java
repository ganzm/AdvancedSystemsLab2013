package ch.ethz.mlmq.test.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Logger;

import org.junit.Test;

import ch.ethz.mlmq.nio.ConnectedClient;
import ch.ethz.mlmq.test.util.TestLogger;

public class SelectorTest {

	private static Logger logger = TestLogger.getLogger();

	@Test
	public void testSelector() throws IOException {

		logger.info("Start Test");
		Selector selector = Selector.open();
		logger.info("Opened");

		ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
		logger.info("ServerSocketChannel created");

		serverSocketChannel.configureBlocking(false);
		serverSocketChannel.bind(new InetSocketAddress(8080));
		logger.info("ServerSocketChannel bound");

		serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

		boolean running = true;
		while (running) {

			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
			}

			logger.info("before select");
			selector.select();
			logger.info("after select");

			Set<SelectionKey> selectedKeys = selector.selectedKeys();
			Iterator<SelectionKey> keyIterator = selectedKeys.iterator();
			while (keyIterator.hasNext()) {
				try {

					SelectionKey key = keyIterator.next();

					logger.info("Interested Ops of " + key.attachment() + " is " + key.interestOps());

					if (key.isAcceptable()) {
						logger.info(key.channel() + " channel is " + "acceptable");
						ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
						SocketChannel newClientChannel = serverChannel.accept();
						newClientChannel.configureBlocking(false);

						logger.info(newClientChannel + " new connection established");

						ConnectedClient clientInstance = new ConnectedClient(newClientChannel.getRemoteAddress().toString());
						newClientChannel.register(selector, SelectionKey.OP_READ, clientInstance);

					}
					if (key.isConnectable()) {
						logger.info(key.channel() + " channel is " + "isConnectable");
					}
					if (key.isReadable()) {
						logger.info(key.channel() + " " + key.attachment() + " channel is " + "isReadable");
						read(selector, key);

					}
					if (key.isValid()) {
						logger.info(key.channel() + " " + key.attachment() + " channel is " + "isValid");
					}
					if (key.isWritable()) {
						logger.info(key.channel() + " " + key.attachment() + " channel is " + "isWritable");
						write(selector, key);
					}

				} catch (Exception ex) {
					logger.info("Exception " + ex);
				} finally {
					keyIterator.remove();
				}

			}
		}
	}

	private void write(Selector selector, SelectionKey key) throws IOException {

		ConnectedClient connectedClient = (ConnectedClient) key.attachment();
		logger.info("writing to " + connectedClient);
		SocketChannel sChannel = (SocketChannel) key.channel();

		ByteBuffer rxBuffer = connectedClient.getRxBuffer();

		int numBytes = sChannel.write(rxBuffer);
		logger.info("Num bytes written " + numBytes);

		if (rxBuffer.remaining() <= 0) {
			logger.info("switch to reading mode");
			key.interestOps(SelectionKey.OP_READ);
		}
	}

	private void read(Selector selector, SelectionKey key) throws IOException {

		ConnectedClient connectedClient = (ConnectedClient) key.attachment();
		logger.info("reading from " + connectedClient);
		SocketChannel sChannel = (SocketChannel) key.channel();

		ByteBuffer rxBuffer = connectedClient.getRxBuffer();
		int byteCount = sChannel.read(rxBuffer);
		logger.info("reading num bytes " + byteCount);
		if (byteCount > 0) {
			String request = new String(rxBuffer.array(), 0, rxBuffer.position());
			logger.info(request);

			// switch to write mode
			key.interestOps(SelectionKey.OP_WRITE);

			String htmlContent = "<html><body><h1>" + new Date() + "</h1></body></html>";
			// @formatter:off
			String response = "HTTP/1.1 200 OK\r\n" 
					+ "Content-Type: text/html; charset=UTF-8\r\n" 
					+ "Content-Lenght: "
					+ (htmlContent.getBytes(Charset.forName("UTF-8")).length) 
					+ "\r\n"
					+ "Connection: keep-alive\r\n"
					+ "\r\n" 
					+ htmlContent;
			// @formatter:on

			logger.info("Prepared Response: " + response);
			logger.info("Response total length: " + response.getBytes(Charset.forName("UTF-8")).length);

			// dirty abuse rx buffer
			rxBuffer.clear();
			rxBuffer.put(response.getBytes(Charset.forName("UTF-8")));
			rxBuffer.flip();
		} else if (byteCount <= 0) {

			logger.info("Socket remotely closed " + key.attachment());
			key.cancel();
			sChannel.close();
		}
	}
}
