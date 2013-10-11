package ch.ethz.mlmq.nio;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import ch.ethz.mlmq.exception.MlmqException;
import ch.ethz.mlmq.logging.LoggerUtil;
import ch.ethz.mlmq.logging.PerformanceLogger;
import ch.ethz.mlmq.logging.PerformanceLoggerManager;
import ch.ethz.mlmq.server.BrokerConfiguration;
import ch.ethz.mlmq.server.processing.WorkerTask;
import ch.ethz.mlmq.server.processing.WorkerTaskQueue;

public class BrokerNetworkInterface implements Runnable, Closeable {

	private static final Logger logger = Logger.getLogger(BrokerNetworkInterface.class.getSimpleName());

	private final PerformanceLogger perfLog = PerformanceLoggerManager.getLogger();

	private Selector selector;

	/**
	 * ServerSocket which accepts clientconnection
	 */
	private ServerSocketChannel serverSocketChannel;

	/**
	 * TCP port where we are listening
	 */
	private final int listenPort;

	/**
	 * indicates whether we are running or not
	 */
	private volatile boolean running = false;

	/**
	 * Queue where workers post their responses, which then are transfered by us
	 */
	private final NetworkIntefaceResponseQueue responseQueue;

	/**
	 * Queue where the reactor puts requests
	 */
	private final WorkerTaskQueue requestQueue;
	/**
	 * Thread which handles networking on the broker
	 */
	private Thread networkingThread;

	/**
	 * Maps unique clientNetworkHandle to ConnectedClient objects
	 */
	private final Map<Integer, ConnectedClient> connectedClients = new HashMap<Integer, ConnectedClient>();

	private int clientCounter = 0;

	private final ByteBufferPool byteBufferPool;

	public BrokerNetworkInterface(BrokerConfiguration config, WorkerTaskQueue requstQueue) {
		this.listenPort = config.getListenPort();
		this.responseQueue = new NetworkIntefaceResponseQueue();
		this.requestQueue = requstQueue;
		this.byteBufferPool = new ByteBufferPool(config);
	}

	/**
	 * startup the network interface
	 * 
	 * @throws IOException
	 */
	public void init() throws MlmqException {

		if (networkingThread != null) {
			throw new RuntimeException("BrokerNetworkInterface already running");
		}

		logger.info("Initializing NetworkInterface on Port " + listenPort);

		networkingThread = new Thread(this);
		networkingThread.setName("NetworkInterface");
		networkingThread.start();

		// busy wait for startup
		while (!running && networkingThread.isAlive()) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
			}
		}

		if (!networkingThread.isAlive()) {
			throw new MlmqException("Error while starting NetworkInterface");
		}
	}

	@Override
	public void close() {
		running = false;

		logger.info("Wakeup selector");
		if (selector != null) {
			selector.wakeup();
		}
	}

	@Override
	public void run() {
		try {
			setup();
			running = true;
			while (running) {

				selector.select();

				// check keys
				Set<SelectionKey> selectedKeys = selector.selectedKeys();
				Iterator<SelectionKey> keyIterator = selectedKeys.iterator();
				while (keyIterator.hasNext()) {
					SelectionKey key = keyIterator.next();
					try {
						selectKey(key);
					} catch (Exception ex) {
						logger.info("Exception " + ex);
						disconnectCLient(key);
					} finally {
						keyIterator.remove();
					}
				}

				// check Queue
				selectQueue();
			}
		} catch (IOException ex) {
			logger.severe("IOException in NetworkInterface " + LoggerUtil.getStackTraceString(ex));
		} finally {
			running = false;
			logger.info("NetworkInterface closing...");
			try {
				teardown();
			} catch (IOException ex) {
				logger.warning("Exception while shutdown " + LoggerUtil.getStackTraceString(ex));
			}
			logger.info("NetworkInterface closed");
		}
	}

	private void disconnectCLient(SelectionKey key) {
		ConnectedClient clientInstance = (ConnectedClient) key.attachment();
		SocketChannel clientChannel = (SocketChannel) key.channel();

		logger.info("Disconnecting from Client " + clientChannel);
		clientInstance.close();
		try {
			clientChannel.close();
		} catch (IOException e) {
			logger.severe("Error while disconnecting from Client " + clientChannel);
		}

		connectedClients.remove(clientInstance.getClientContext().getClientNetworkHandle());
	}

	/**
	 * empties the response queue and multiplex WorkerTask to their corresponding ConnectedClients
	 */
	private void selectQueue() {
		WorkerTask task;
		while ((task = responseQueue.dequeue()) != null) {
			try {
				int clientNetworkHandle = task.getClientContext().getClientNetworkHandle();

				logger.fine("Enqueue Response for Client " + clientNetworkHandle + " Task: " + task);

				ConnectedClient connectedClient = connectedClients.get(clientNetworkHandle);

				if (connectedClient == null) {
					logger.severe("Response for unknown clientId " + clientNetworkHandle + " discarded - maybe client disconnected " + task);
					break;
				}

				CloseableByteBuffer buffer = task.getAndRemoveResponseBuffer();
				connectedClient.setResponse(buffer);
			} finally {
				// dispose task
				task.close();
			}
		}
	}

	private void selectKey(SelectionKey key) throws ClosedChannelException, IOException {

		if (key.isValid() && key.isAcceptable()) {
			selectAccept(key);
		}
		if (key.isValid() && key.isWritable()) {
			selectWrite(key);
		}
		if (key.isValid() && key.isReadable()) {
			selectRead(key);
		}
	}

	private void selectWrite(SelectionKey key) throws IOException {
		ConnectedClient clientInstance = (ConnectedClient) key.attachment();
		SocketChannel clientChannel = (SocketChannel) key.channel();

		if (logger.isLoggable(Level.FINER)) {
			logger.finer("Write " + clientInstance);
		}

		ByteBuffer txBuffer = clientInstance.getTxBuffer();
		int byteCount = clientChannel.write(txBuffer);
		logger.fine("writing to Client " + clientInstance + " num bytes " + byteCount);

		if (clientInstance.afterWrite()) {
			long time = System.currentTimeMillis() - clientInstance.getStartSendResponseTime();
			long totalTime = System.currentTimeMillis() - clientInstance.getFirstRequestByteSeenTimeStamp();

			perfLog.log(time, "SendResponse");
			perfLog.log(totalTime, "TotalRequestResponse");
		}
	}

	private void selectRead(SelectionKey key) throws IOException {
		ConnectedClient clientInstance = (ConnectedClient) key.attachment();
		SocketChannel clientChannel = (SocketChannel) key.channel();

		if (logger.isLoggable(Level.FINER)) {
			logger.finer("Write " + clientInstance);
		}

		ByteBuffer rxBuffer = clientInstance.getRxBuffer();

		if (rxBuffer.position() == 0) {
			// first byte seen from client
			clientInstance.setFirstRequestByteSeenTimeStamp();
		}

		int byteCount = clientChannel.read(rxBuffer);
		logger.fine("reading from Client " + clientInstance + " num bytes " + byteCount);

		if (clientInstance.hasReceivedMessage()) {
			CloseableByteBuffer replacementBuffer = byteBufferPool.aquire();
			CloseableByteBuffer messageBuffer = clientInstance.swapRxBuffer(replacementBuffer);

			onMessage(clientInstance, messageBuffer);
		}

		if (byteCount < 0) {
			logger.info("Socket remotely closed " + clientChannel);
			disconnectCLient(key);
		}
	}

	private void selectAccept(SelectionKey key) throws IOException, ClosedChannelException {
		SocketChannel newClientChannel = serverSocketChannel.accept();
		newClientChannel.configureBlocking(false);

		logger.info(newClientChannel + " new connection established");

		++clientCounter;

		ConnectedClient clientInstance = new ConnectedClient(clientCounter, newClientChannel.getRemoteAddress().toString());
		connectedClients.put(clientCounter, clientInstance);

		SelectionKey selectionKey = newClientChannel.register(selector, SelectionKey.OP_READ, clientInstance);

		CloseableByteBuffer rxBuffer = byteBufferPool.aquire();
		CloseableByteBuffer txBuffer = byteBufferPool.aquire();
		clientInstance.initBuffers(rxBuffer, txBuffer);

		// ögli häck - zörkälär dipändenzi
		clientInstance.setSelectionKey(selectionKey);
	}

	private void onMessage(ConnectedClient clientInstance, CloseableByteBuffer messageBuffer) {
		logger.info("onmessage " + clientInstance + " " + messageBuffer);

		// do performance logging
		long receiveTime = System.currentTimeMillis() - clientInstance.getFirstRequestByteSeenTimeStamp();
		perfLog.log(receiveTime, "ReceiveRequest");

		if (!requestQueue.enqueue(new WorkerTask(clientInstance.getClientContext(), messageBuffer))) {
			logger.severe("WorkerTaskQueue full dropping Message from Client " + clientInstance);
		}
	}

	private void teardown() throws IOException {

		responseQueue.setWakeupReactorRunnable(null);

		logger.info("Close ServerSocket");
		if (serverSocketChannel.isOpen()) {
			serverSocketChannel.close();
		}

		logger.info("Close Selector");
		if (selector.isOpen()) {
			selector.close();
		}
	}

	private void setup() throws IOException {
		logger.info("Open Selector");
		selector = Selector.open();

		logger.info("Open ServerSocket");
		serverSocketChannel = ServerSocketChannel.open();
		serverSocketChannel.bind(new InetSocketAddress(listenPort));
		serverSocketChannel.configureBlocking(false);
		serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

		logger.info("ServerSocketChannel bound to " + listenPort);

		responseQueue.setWakeupReactorRunnable(new Runnable() {
			@Override
			public void run() {
				if (logger.isLoggable(Level.FINE)) {
					logger.fine("Wake up Selector");
				}
				selector.wakeup();
			}
		});
	}

	/**
	 * returns the queue where workers put their response
	 * 
	 * @return
	 */
	public WorkerTaskQueue getResponseQueue() {
		return responseQueue;
	}
}
