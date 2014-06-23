package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

/**
 * Abstract class for parsing command-line arguments (i.e. options).
 * 
 */
public abstract class ArgumentParser0 {
	
	/**
	 * Check if there is an argument in a given position in the argument list.
	 * @param argv List of command-line arguments.
	 * @param idx Index of the argument being checked.
	 * @throws MissingArgumentValueException
	 */
	protected static void checkArgumentPresence(String[] argv, int idx) throws MissingArgumentValueException {
		if ((idx + 1) >= argv.length)
		{
			String message = String.format(
					"Missing the argument %s",
					argv[idx]);
			Messages.warning(message);
			throw new MissingArgumentValueException(message);
		}
	}

	/**
	 * Check if the argument in a given position is a positive integer (>0) and returns it, if it is.
	 * @param argv List of command-line arguments.
	 * @param idx Index of the argument being checked.
	 * @return The value of the positive integer in the given position.
	 * @throws MissingArgumentValueException
	 * @throws NumberFormatException
	 */
	protected static int parsePositiveInt(String[] argv, int idx) throws MissingArgumentValueException, NumberFormatException {
		checkArgumentPresence(argv,idx);
		int res = Integer.parseInt(argv[idx + 1]);
		if (res <= 0)
			throw new NumberFormatException(String.format(
					"The value of the argument %s must be positive (non-zero)",
					argv[idx]));
		return res;
	}
	
	/**
	 * Check if the argument in a given position is a non-negative integer (>=0) and returns it, if it is.
	 * @param argv List of command-line arguments.
	 * @param idx Index of the argument being checked.
	 * @return The value of the non-negative integer in the given position.
	 * @throws MissingArgumentValueException
	 * @throws NumberFormatException
	 */
	protected static int parseNonNegativeInt(String[] argv, int idx) throws MissingArgumentValueException, NumberFormatException {
		checkArgumentPresence(argv,idx);
		int res = Integer.parseInt(argv[idx + 1]);
		if (res < 0)
			throw new NumberFormatException(String.format(
					"The value of the argument %s must be non-negative",
					argv[idx]));
		return res;
	}
	
	/**
	 * Create a BufferedReader from standard input.
	 * @return A new BufferedReader from standard input.
	 */
	public static BufferedReader openStandardInput()
	{
		try
		{
			return (new BufferedReader(
					new InputStreamReader(System.in, "UTF-8")));
		}
		catch (UnsupportedEncodingException e)
		{
			Messages.warning(e.getMessage());
			return (new BufferedReader(new InputStreamReader(System.in)));
		}
	}	
	
	/**
	 * Create a BufferedReader from a given input file.
	 * @param filename The name of the file to open.
	 * @return A new BufferedReader from file.
	 * @throws FileNotFoundException
	 */
	public static BufferedReader openFile(String filename)
			throws FileNotFoundException
	{
		FileInputStream		f;
		
		f = new FileInputStream(filename);
		try
		{
			return (new BufferedReader(new InputStreamReader(f, "UTF-8")));
		}
		catch (UnsupportedEncodingException e)
		{
			Messages.warning(e.getMessage());
			return (new BufferedReader(new InputStreamReader(f)));
		}
	}
	
	/**
	 * Create a PrintWriter to a given output file.
	 * @param filename The name of the file to open.
	 * @return A new PrintWriter to file.
	 * @throws FileNotFoundException
	 */
	public static PrintWriter openOutputFile(String filename)
			throws FileNotFoundException
	{
		File f = new File(filename);
		try
		{
			return (new PrintWriter(f, "UTF-8"));
		}
		catch (UnsupportedEncodingException e)
		{
			Messages.warning(e.getMessage());
			return (new PrintWriter(f));
		}
	}


}
