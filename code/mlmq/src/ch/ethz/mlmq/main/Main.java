package ch.ethz.mlmq.main;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import ch.ethz.mlmq.logging.LoggerUtil;

public class Main {
	private static final Logger logger = Logger.getLogger(Main.class.getSimpleName());

	public static void main(String[] args) {
		if (args.length == 0) {
			showHelpAndExit();
			return;
		}

		Map<String, String> argList = parseArgs(args);

		switch (args[0]) {
		case "client_sender":
			System.out.println("Hi! I'm a client sender.");
			// TODO...
			break;
		case "client_receiver":
			System.out.println("And I'm a client receiver.");
			// TODO...
			break;
		case "broker":
			System.out.println("Behold! Here comes the boss!");
			// TODO...
			break;
		case "dbscript":
			System.exit(mainDbScript(argList));
			break;
		default:
			showHelpAndExit();
			return;
		}

	}

	private static Map<String, String> parseArgs(String[] args) {
		Map<String, String> result = new HashMap<String, String>();

		String keyPrefix = "-";

		// skip <type> argument
		for (int i = 1; i < args.length; i++) {

			if (args[i].startsWith(keyPrefix)) {

				String key = args[i].substring(1, args[i].length() - 1);
				String value = null;
				if (args.length > i + 1 && !args[i + 1].startsWith(keyPrefix)) {
					value = args[i + 1].substring(1, args[i].length() - 1);
				} else {
					value = "true";
				}

				result.put(key, value);
			}
		}
		return result;
	}

	private static void showHelpAndExit() {

		//@formatter:off
		System.out.println("usage: java -jar target.jar <type>");
		System.out.println();
		System.out.println("Types:");
		System.out.println("\tclient_sender\tStarts a client instance\n\t\t\t-config [ConfigFilePath]");
		System.out.println("\tclient_receiver\tStarts a client instance\n\t\t\t-config [ConfigFilePath]");
		System.out.println("\tbroker\tStarts a broker instance (for the middleware)\n\t\t\t-config [ConfigFilePath]");
		System.out.println("\tdbscript"
				+ "\n\t\t\t-file [ScriptFilePath] optional parameter"
				+ "\n\t\t\t-url [jdbc:postgresql://host:port]"
				+ "\n\t\t\t-db [database name]"
				+ "\n\t\t\t-user [usename]"
				+ "\n\t\t\t-pwd [password]"
				+ "\n\t\t\t-createDatabase optional flag"
				+ "\n\t\t\t-createTables optional flag"
				+ "\n\t\t\t-dropDatabase optional flag"
				);
		//@formatter:on

		System.exit(2);
	}

	private static int mainDbScript(Map<String, String> argList) {
		try {

			String file = argList.remove("file");
			String url = argList.remove("url");
			String user = argList.remove("user");
			String password = argList.remove("password");
			String db = argList.remove("db");
			boolean createDatabase = Boolean.parseBoolean(argList.remove("createDatabase"));
			boolean createTables = Boolean.parseBoolean(argList.remove("createTables"));
			boolean dropDatabase = Boolean.parseBoolean(argList.remove("dropDatabase"));

			if (!argList.isEmpty()) {
				System.out.println("Parameters not understood " + argList);
			}

			if (url == null) {
				System.out.println("Missing Parameter -url");
			}

			if (user == null) {
				System.out.println("Missing Parameter -user");
			}

			if (password == null) {
				System.out.println("Missing Parameter -password");
			}

			if (db == null) {
				System.out.println("Missing Parameter -db");
			}

			DbScriptMain.run(file, url, db, user, password, createDatabase, createTables, dropDatabase);
			return 0;
		} catch (IOException | SQLException e) {
			logger.severe("Exception " + LoggerUtil.getStackTraceString(e));
			return -1;
		}
	}
}
