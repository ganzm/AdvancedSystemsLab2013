package ch.ethz.mlmq.util;

import java.nio.ByteBuffer;

public class ByteBufferUtil {

	public static void putString(ByteBuffer buff, String str) {
		if (str != null) {
			byte[] data = str.getBytes();
			buff.putInt(data.length);
			buff.put(data);

		} else {
			buff.putInt(0);
		}
	}

	public static String getString(ByteBuffer buff) {
		int size = buff.getInt();
		if (size > 0) {
			byte[] data = new byte[size];
			buff.get(data);
			return new String(data);
		} else {
			return null;
		}
	}
}
