package ch.ethz.mlmq.scenario;

import java.io.IOException;

import ch.ethz.mlmq.exception.MlmqException;

public interface Startable {
	void startup() throws InterruptedException, IOException, MlmqException;

}
