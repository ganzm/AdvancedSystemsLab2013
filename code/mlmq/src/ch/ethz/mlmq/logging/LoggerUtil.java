package ch.ethz.mlmq.logging;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class LoggerUtil {

	/**
	 * Helper converts stacktrace of an exception to a string
	 * 
	 * @param ex
	 * @return
	 */
	public static String getStackTraceString(Exception ex) {
		ByteArrayOutputStream bOut = new ByteArrayOutputStream();
		try (PrintStream printStream = new PrintStream(bOut)) {
			ex.printStackTrace(printStream);
			return new String(bOut.toByteArray());
		}
	}
}
