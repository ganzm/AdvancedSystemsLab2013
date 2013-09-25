package ch.ethz.mlmq.test.jmx;

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
