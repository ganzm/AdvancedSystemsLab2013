package ch.ethz.mlmq.util;

import java.io.File;

public class FileHelper {
	public static void deleteRecursive(String pathToFileOrDirectory) {
		deleteRecursive(new File(pathToFileOrDirectory));
	}

	public static void deleteRecursive(File f) {
		for (File ff : f.listFiles()) {
			if (ff.isFile())
				ff.delete();
			else
				deleteRecursive(ff);
		}
		f.delete();
	}
}
