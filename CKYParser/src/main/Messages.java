package main;

/**
 * A simple utility class used to write messages to the error output with a
 * specific error gravity level. 
 *
 */
public class Messages
{
	private static String	_name = "CKYParser";

	/**
	 * Writes an error message to stderr and exits the program.
	 * @param s The message to be written.
	 */
	public static void error(String s)
	{
		System.err.printf("[Error] %s", Messages.message(s));
		System.exit(-1);
	}
	
	/**
	 * Writes an info message to stderr.
	 * @param s The message to be written.
	 */
	public static void info(String s)
	{
		System.err.printf("[Info] %s", Messages.message(s));
	}
	
	/**
	 * Writes a warning message to stderr.
	 * @param s The message to be written.
	 */
	public static void warning(String s)
	{
		System.err.printf("[Warning] %s", Messages.message(s));
	}
	
	private static String message(String s)
	{
		StringBuffer	res;
		
		res = new StringBuffer(Messages._name);
		res.append(": ");
		res.append(s);
		if (!s.endsWith("\n"))
			res.append("\n");
		return (res.toString());
	}
}
