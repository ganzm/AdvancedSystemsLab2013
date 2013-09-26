package ch.ethz.mlmq.samle;

import java.io.IOException;
import java.util.Random;

public class LeaderFollowerSample {

	private ThreadPool threadPool;

	public static void main(String[] args) throws IOException, InterruptedException {
		new LeaderFollowerSample().testLeaderFollower();
	}

	public void testLeaderFollower() throws InterruptedException {
		threadPool = new ThreadPool();

		WorkerThread t1 = new WorkerThread("T1", threadPool);
		WorkerThread t2 = new WorkerThread("T2", threadPool);
		WorkerThread t3 = new WorkerThread("T3", threadPool);
		WorkerThread t4 = new WorkerThread("T4", threadPool);
		WorkerThread t5 = new WorkerThread("T5", threadPool);
		WorkerThread t6 = new WorkerThread("T6", threadPool);
		WorkerThread t7 = new WorkerThread("T7", threadPool);
		WorkerThread t8 = new WorkerThread("T8", threadPool);

		t1.start();
		t2.start();
		t3.start();
		t4.start();
		t5.start();
		t6.start();
		t7.start();
		t8.start();

		threadPool.startPool();

		Thread.sleep(20000);

		System.out.println("Stoping pool");
		threadPool.stopPool();
	}

	private static class ThreadPool {

		private final Object monitor = new Object();
		private volatile boolean running = false;
		private boolean populated = false;
		private Thread leader;

		/**
		 * A working thread should join the pool as soon as it's work has been done
		 * 
		 * @throws InterruptedException
		 * 
		 */
		public void joinPool() throws InterruptedException {
			System.out.println(Thread.currentThread().getName() + " is joining the pool");

			synchronized (monitor) {

				if (leader == null && running) {
					System.out.println("Everyone was busy so far - There might be not enough threads in the pool");
				} else {

					populated = true;
					monitor.wait();
				}

				leader = Thread.currentThread();
			}

			if (!running) {
				// could solve this with return/status codes or something else
				throw new InterruptedException("Pool was terminated");
			}

			System.out.println(Thread.currentThread().getName() + " left the pool");
		}

		public void stopPool() {
			running = false;
			synchronized (monitor) {
				monitor.notifyAll();
			}
		}

		public void startPool() {
			synchronized (monitor) {

				while (!populated) {
					try {
						monitor.wait(10);
					} catch (InterruptedException e) {
					}
				}
				running = true;
				monitor.notify();
			}
		}

		public void promoteAnyone() {
			synchronized (monitor) {

				if (leader != Thread.currentThread()) {
					throw new RuntimeException("Only the leader can promote");
				}
				leader = null;

				monitor.notify();
			}
		}
	};

	private static class WorkerThread extends Thread {

		private final ThreadPool threadPool;
		private boolean running = false;
		private Random rnd = new Random();

		public WorkerThread(String name, ThreadPool threadPool) {
			this.threadPool = threadPool;
			setName(name);
		}

		@Override
		public void run() {
			try {

				running = true;
				while (running) {

					threadPool.joinPool();

					System.out.println(getName() + " is the new leader");

					int event = waitForEvent();
					System.out.println(getName() + " received a new event");

					threadPool.promoteAnyone();

					process(event);

				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		private void process(int event) {
			System.out.println(getName() + " processes an event " + event);

			try {
				Thread.sleep(event);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println(getName() + " processes an event finished " + event);
		}

		private int waitForEvent() {

			try {
				Thread.sleep(Math.abs(rnd.nextInt()) % 1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return Math.abs(rnd.nextInt()) % 2000;
		}
	}
}
