package ch.ethz.mlmq.common;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Properties;
import java.util.logging.Logger;

import ch.ethz.mlmq.client.ClientConfiguration;
import ch.ethz.mlmq.exception.MlmqException;
import ch.ethz.mlmq.scenario.Scenario;
import ch.ethz.mlmq.server.BrokerConfiguration;
import ch.ethz.mlmq.util.ConfigurationUtil;

/**
 * Helps you to set up Scenario, Broker, Client infrastructure
 */
public class Initializer {

	private static final Logger logger = Logger.getLogger(Initializer.class.getSimpleName());

	public Scenario initScenario(String configFilePath) throws IOException, MlmqException {
		try {
			Configuration config = loadConfig(configFilePath);

			ScenarioMapping myMapping = config.getMyMapping();

			Class<? extends Scenario> scenarioClass = findScenario(myMapping.getName());

			return createScenario(scenarioClass, config);
		} catch (Exception e) {
			throw new MlmqException(e);
		}
	}

	private Scenario createScenario(Class<? extends Scenario> scenarioClass, Configuration config) throws NoSuchMethodException, SecurityException,
			InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {

		logger.fine("Finde Configuration Constructor for " + scenarioClass);
		Constructor<? extends Scenario> constructor = scenarioClass.getConstructor(config.getClass());

		return constructor.newInstance(config);
	}

	private Configuration loadConfig(String configFilePath) throws IOException, MlmqException {

		// load configuration from file
		Properties configProps = ConfigurationUtil.loadPropertiesFromFile(configFilePath);

		String myType = configProps.getProperty(Configuration.SCENARIO_MYTYPE);
		if (Configuration.SCENARIO_MYTYPE_CLIENT_VALUE.equals(myType)) {
			return new ClientConfiguration(configProps);
		} else if (Configuration.SCENARIO_MYTYPE_BROKER_VALUE.equals(myType)) {
			return new BrokerConfiguration(configProps);
		} else {
			throw new MlmqException("Invalid Configuration, unexpected value - " + Configuration.SCENARIO_MYTYPE + "=" + myType);
		}
	}

	public static Class<? extends Scenario> findScenario(String scenarioName) throws Exception {
		String packageName = "ch.ethz.mlmq.scenario.impl.";
		String fullName = packageName + scenarioName;

		logger.info("Find scenario " + fullName);
		return Class.forName(fullName).asSubclass(Scenario.class);
	}
}
