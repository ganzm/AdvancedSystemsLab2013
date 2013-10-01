package ch.ethz.mlmq.net;

public class Protocol {
	/**
	 * Assume a messige is 2kbytes at max, add some extra memory
	 */
	public static int CLIENT_IO_BUFFER_CAPACITY = 4000;

	/**
	 * lengt of the first part of our request/response messages
	 * 
	 * where we store the int value with the message size
	 */
	public static int LENGH_FIELD_LENGHT = 4;

}