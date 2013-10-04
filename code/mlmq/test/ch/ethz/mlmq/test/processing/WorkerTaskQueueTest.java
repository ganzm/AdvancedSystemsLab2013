package ch.ethz.mlmq.test.processing;

import java.io.IOException;
import java.util.logging.Logger;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import ch.ethz.mlmq.logging.LoggerUtil;
import ch.ethz.mlmq.server.processing.WorkerTask;
import ch.ethz.mlmq.server.processing.WorkerTaskQueueImpl;

public class WorkerTaskQueueTest {
	private static final Logger logger = Logger.getLogger(WorkerTaskQueueTest.class.getSimpleName());

	private volatile boolean returnedFlag = false;

	@BeforeClass
	public static void beforeClass() throws IOException {
		LoggerUtil.initConsoleDebug();
	}

	@Test
	public void testQueueFull() {
		WorkerTaskQueueImpl queue = new WorkerTaskQueueImpl(3);

		try (WorkerTask t1 = new WorkerTask(1, null);
				WorkerTask t2 = new WorkerTask(2, null);
				WorkerTask t3 = new WorkerTask(3, null);
				WorkerTask t4 = new WorkerTask(4, null);) {

			Assert.assertTrue(queue.enqueue(t1));
			Assert.assertTrue(queue.enqueue(t2));
			Assert.assertTrue(queue.enqueue(t3));

			Assert.assertFalse(queue.enqueue(t4));
		}
	}

	@Test
	public void testBlockingRead() {
		final WorkerTaskQueueImpl queue = new WorkerTaskQueueImpl(3);
		final WorkerTask expectedTask = new WorkerTask(1, null);

		Thread t = new Thread() {

			@Override
			public void run() {
				logger.info("Start BlockingDequeue");
				WorkerTask task = queue.blockingDequeue();
				logger.info("Returned from  BlockingDequeue");
				Assert.assertSame(expectedTask, task);

				returnedFlag = true;
			}
		};

		t.start();

		try {
			Thread.sleep(1000);
		} catch (InterruptedException ex) {
			logger.severe(LoggerUtil.getStackTraceString(ex));
		}

		long startT = System.currentTimeMillis();
		long timeout = 500;

		logger.info("Enqueue Task");
		queue.enqueue(expectedTask);

		while (!returnedFlag && System.currentTimeMillis() - startT < timeout) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException ex) {
				logger.severe(LoggerUtil.getStackTraceString(ex));
			}

		}

		Assert.assertTrue(returnedFlag);
	}
}
