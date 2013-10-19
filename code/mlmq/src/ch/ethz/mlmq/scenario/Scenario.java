package ch.ethz.mlmq.testrun;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

public abstract class TestRun<T> {

public abstract class Scenario<T_SUT extends Startable, T_CONFIG extends Configuration> {
	private final Timer timer = new Timer(true);

	private final ConcurrentHashMap<String, TimerTask> activetimerTasks = new ConcurrentHashMap<>();

	private final T sut;

	public TestRun(T sut) {
		this.sut = sut;
	}

	protected void startTimer(final String name, long timeout) {
		TimerTask timerTask = new TimerTask() {
			@Override
			public void run() {
				timedOut(name);
			}
		};

		activetimerTasks.put(name, timerTask);
		timer.schedule(timerTask, timeout);
	}

	protected boolean clearTimer(String name) {
		TimerTask toStop = activetimerTasks.remove(name);
		if (toStop != null) {
			toStop.cancel();
			return true;
		}
		return false;
	}

	private void timedOut(String name) {
		activetimerTasks.remove(name);
		onTimeout(name, sut);
	}

	public void run() throws IOException {
		run(sut);
	}

	protected abstract void run(T sut) throws IOException;

	protected void onTimeout(String name, T sut) {

	}
}
