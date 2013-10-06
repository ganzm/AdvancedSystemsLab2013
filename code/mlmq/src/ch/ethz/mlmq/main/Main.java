package ch.ethz.mlmq.main;

public class Main {

	public static void main(String[] args) {
		if (args.length == 0) {
			showHelpAndExit();
			return;
		}

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
		default:
			showHelpAndExit();
			return;
		}

	}

	private static void showHelpAndExit() {
		System.out.println("usage: java -jar target.jar <type>");
		System.out.println();
		System.out.println("Types:");
		System.out.println("\tclient_sender\tStarts a client instance");
		System.out.println("\tclient_receiver\tStarts a client instance");
		System.out.println("\tbroker\tStarts a broker instance (for the middleware)");
		System.exit(2);
	}

}
