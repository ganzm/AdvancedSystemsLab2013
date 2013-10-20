package ch.ethz.mlmq.scenario;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

import ch.ethz.mlmq.scenario.startup.Startup;
import ch.ethz.mlmq.server.Configuration;

public abstract class Scenario<T_SUT extends Startable, T_CONFIG extends Configuration> {
	private final Timer timer = new Timer(true);

	private final ConcurrentHashMap<String, TimerTask> activetimerTasks = new ConcurrentHashMap<>();

	private T_SUT sut;

	private T_CONFIG config;

	public Scenario() {
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

	protected abstract void run(T_SUT sut, T_CONFIG config) throws Exception;

	protected abstract Startup<T_SUT, T_CONFIG> initSut();

	protected void onTimeout(String name, T_SUT sut) {
	}

	public void start(String pathToConfig) throws Exception {
		Startup<T_SUT, T_CONFIG> rj = initSut();
		rj.start(pathToConfig);
		config = rj.getConfig();
		sut = rj.getSut();
		run(sut, config);
	}
}
