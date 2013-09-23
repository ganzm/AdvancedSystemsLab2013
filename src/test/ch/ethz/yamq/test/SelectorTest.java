package ch.ethz.yamq.test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Set;
import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.logging.StreamHandler;

import org.junit.Test;

public class SelectorTest {

	private static final Logger logger = Logger.getLogger("Default");

	@Test
	public void testSelector() throws IOException {

		ConsoleHandler consoleHandler = new ConsoleHandler();
		Formatter formatter = new SimpleFormatter();
		StreamHandler streamHandler = new StreamHandler(System.out, formatter);
		logger.setUseParentHandlers(false);
		logger.addHandler(streamHandler);

		logger.info("Start Test");
		Selector selector = Selector.open();
		logger.info("Opened");

		// SocketChannel serverSocketChannel = SocketChannel.open(new InetSocketAddress(8080));

		ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
		logger.info("ServerSocketChannel created");

		serverSocketChannel.configureBlocking(false);
		serverSocketChannel.bind(new InetSocketAddress(8080));
		logger.info("ServerSocketChannel bound");

		SelectionKey sk = serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
		boolean running = true;
		while (running) {

			logger.info("before select");
			selector.select();
			logger.info("after select");

			Set<SelectionKey> selectedKeys = selector.selectedKeys();

			for (SelectionKey key : selectedKeys) {

				if (key.isAcceptable()) {
					logger.info(key.channel() + " channel is " + "acceptable");
					ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
					SocketChannel newClientChannel = serverChannel.accept();
					newClientChannel.configureBlocking(false);

					logger.info(newClientChannel + " new connection established");
					String clientInstance = "Client27";
					newClientChannel.register(selector, SelectionKey.OP_CONNECT | SelectionKey.OP_READ | SelectionKey.OP_WRITE, clientInstance);
				}
				if (key.isConnectable()) {
					logger.info(key.channel() + " channel is " + "isConnectable");
				}
				if (key.isReadable()) {
					logger.info(key.channel() + " " + key.attachment() + " channel is " + "isReadable");
				}
				if (key.isValid()) {
					logger.info(key.channel() + " " + key.attachment() + " channel is " + "isValid");
				}
				if (key.isWritable()) {
					logger.info(key.channel() + " " + key.attachment() + " channel is " + "isWritable");
				}
			}

		}
	}
}
