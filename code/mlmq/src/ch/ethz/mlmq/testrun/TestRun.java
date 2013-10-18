package ch.ethz.mlmq.testrun;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

public abstract class TestRun {

	private final Timer timer = new Timer(true);

	private final ConcurrentHashMap<String, TimerTask> activetimerTasks = new ConcurrentHashMap<>();

	public TestRun() {
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
		onTimeout(name);
	}

	abstract void run() throws Exception;

	protected void onTimeout(String name) {

	}
}
