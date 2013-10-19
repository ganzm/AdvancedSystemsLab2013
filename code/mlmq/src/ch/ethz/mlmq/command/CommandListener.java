package ch.ethz.mlmq.command;

/**
 * Callback for CommandFileHandler. You may register to get notification about command strings written to the command file.
 * 
 * @see CommandFileHandler
 */
public interface CommandListener {

	void onCommand(String command);
}
