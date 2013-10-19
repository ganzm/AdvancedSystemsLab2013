package ch.ethz.mlmq.testrun;

import ch.ethz.mlmq.client.Client;

public abstract class ClientTestRun extends TestRun<Client> {

	public ClientTestRun(Client sut) {
		super(sut);
	}

}
