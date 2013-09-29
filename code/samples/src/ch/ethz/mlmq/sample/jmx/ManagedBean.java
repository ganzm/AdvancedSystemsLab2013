package ch.ethz.mlmq.sample.jmx;

public class ManagedBean implements ManagedBeanMBean {

	@Override
	public String sayHello() {
		return "Hello";
	}

	@Override
	public String respond(String request) {

		return "Hello " + request;
	}
}
