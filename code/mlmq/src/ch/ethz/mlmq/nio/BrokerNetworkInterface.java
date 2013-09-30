package ch.ethz.mlmq.nio;

import java.io.Closeable;
import java.nio.channels.Selector;

import ch.ethz.mlmq.server.BrokerConfiguration;
import ch.ethz.mlmq.server.processing.ResponseQueue;

public class BrokerNetworkInterface implements Runnable, Closeable {

	private Selector selector;

	private final int listenPort;

	private final NetworkIntefaceResponseQueue responseQueue;

	public BrokerNetworkInterface(BrokerConfiguration config) {
		this.listenPort = config.getListenPort();
		this.responseQueue = new NetworkIntefaceResponseQueue();
	}

	public void init() {

	}

	@Override
	public void close() {
		// TODO Auto-generated method stub

	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

	}

	/**
	 * returns the queue where workers put their response
	 * 
	 * @return
	 */
	public ResponseQueue getResponseQueue() {
		return responseQueue;
	}
}
