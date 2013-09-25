package ch.ethz.mlmq.test.jmx;

import java.lang.management.ManagementFactory;

import javax.management.InstanceAlreadyExistsException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;

public class JmxSample {

	public static void main(String[] args) throws InterruptedException, InstanceAlreadyExistsException, MBeanRegistrationException, NotCompliantMBeanException,
			MalformedObjectNameException {

		ManagedBean bean = new ManagedBean();

		MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
		ObjectName name = new ObjectName("ch.ethz:type=MyManagedBean");
		mbs.registerMBean(bean, name);

		Thread.currentThread().join();
	}
}
