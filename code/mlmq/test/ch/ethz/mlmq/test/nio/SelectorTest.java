package ch.ethz.mlmq.test.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
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

		sChannel.write(rxBuffer);
		logger.info("Num bytes written " + rxBuffer.position());

		if (rxBuffer.remaining() <= 0) {
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
			logger.info(new String(rxBuffer.array(), 0, rxBuffer.position()));

			// switch to write mode
			key.interestOps(SelectionKey.OP_WRITE);

			String htmlContent = "<html><body><h1>blub</h1></body></html>\n";
			// @formatter:off
			String response = 	"HTTP/1.1 200 OK\n"
								+ "Server: Apache/1.3.29 (Unix) PHP/4.3.4" 
								+ "Content-Type: text/html"
								+ "Content-Lenght: " + htmlContent.getBytes().length 
								+ "\n\n\n" 
								+ htmlContent ;
			// @formatter:on

			// dirty abuse rx buffer
			rxBuffer.clear();
			rxBuffer.put(response.getBytes());
			rxBuffer.flip();
		} else if (byteCount <= 0) {

			key.cancel();
			sChannel.close();
		}

	}
}
