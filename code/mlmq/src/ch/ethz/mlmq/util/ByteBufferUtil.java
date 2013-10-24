package ch.ethz.mlmq.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;

import ch.ethz.mlmq.net.MlmqSerializable;

public class ByteBufferUtil {

	private static final byte trueByte = 0;
	private static final byte falseByte = -1;

	public static void putString(ByteBuffer buff, String str) {
		if (str != null) {
			byte[] data = str.getBytes();
			buff.putInt(data.length);
			buff.put(data);

		} else {
			buff.putInt(-1);
		}
	}

	public static String getString(ByteBuffer buff) {
		int size = buff.getInt();
		if (size >= 0) {
			byte[] data = new byte[size];
			buff.get(data);
			return new String(data);
		} else {
			return null;
		}
	}

	public static MlmqSerializable deserialize(MlmqSerializable obj, ByteBuffer serializeBuffer) {
		byte b = serializeBuffer.get();
		if (b == trueByte) {
			return obj.deserialize(serializeBuffer);
		} else {
			return null;
		}
	}

	public static void serialize(MlmqSerializable obj, ByteBuffer buffer) {
		if (obj != null) {
			buffer.put(trueByte);
			obj.serialize(buffer);
		} else {
			buffer.put(falseByte);
		}
	}

	public static void putBoolean(boolean b, ByteBuffer buffer) {
		if (b) {
			buffer.put(trueByte);
		} else {
			buffer.put(falseByte);
		}
	}

	public static boolean getBoolean(ByteBuffer buffer) {
		byte bByte = buffer.get();
		return bByte == trueByte;
	}

	public static void putLong(Long l, ByteBuffer buffer) {
		if (l == null) {
			buffer.put(falseByte);
		} else {
			buffer.put(trueByte);
			buffer.putLong(l);
		}
	}

	public static void putInteger(Integer l, ByteBuffer buffer) {
		if (l == null) {
			buffer.put(falseByte);
		} else {
			buffer.put(trueByte);
			buffer.putInt(l);
		}
	}

	public static Long getLong(ByteBuffer buffer) {
		byte b = buffer.get();
		if (trueByte == b) {
			return buffer.getLong();
		} else {
			return null;
		}
	}

	public static Integer getInteger(ByteBuffer buffer) {
		byte b = buffer.get();
		if (trueByte == b) {
			return buffer.getInt();
		} else {
			return null;
		}
	}

	public static void putByteArray(byte[] content, ByteBuffer buffer) {
		if (content == null) {
			buffer.putInt(-1);
		} else {
			buffer.putInt(content.length);
			buffer.put(content);
		}
	}

	public static byte[] getByteArray(ByteBuffer buffer) {
		int lenght = buffer.getInt();
		if (lenght >= 0) {
			byte[] data = new byte[lenght];
			buffer.get(data);
			return data;
		} else {

			return null;
		}
	}

	public static void serializeAny(Object obj, ByteBuffer buffer) {

		try (ByteArrayOutputStream bOut = new ByteArrayOutputStream(); ObjectOutputStream oOut = new ObjectOutputStream(bOut);) {
			oOut.writeObject(obj);
			oOut.flush();

			byte[] data = bOut.toByteArray();
			putByteArray(data, buffer);
		} catch (Exception e) {
			throw new RuntimeException("Exception while serializinging to Buffer " + buffer + " Object: " + obj, e);
		}
	}

	public static Object deserializeAny(ByteBuffer buffer) {

		byte[] data = getByteArray(buffer);
		try (ByteArrayInputStream bIn = new ByteArrayInputStream(data); ObjectInputStream oIn = new ObjectInputStream(bIn);) {
			return oIn.readObject();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
