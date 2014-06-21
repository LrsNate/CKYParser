package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public abstract class ArgumentParser0 {
	
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

	protected static int parsePositiveInt(String[] argv, int idx) throws MissingArgumentValueException, NumberFormatException {
		checkArgumentPresence(argv,idx);
		int res = Integer.parseInt(argv[idx + 1]);
		if (res <= 0)
			throw new NumberFormatException(String.format(
					"The value of the argument %s must be positive (non-zero)",
					argv[idx]));
		return res;
	}
	
	protected static int parseNonNegativeInt(String[] argv, int idx) throws MissingArgumentValueException, NumberFormatException {
		checkArgumentPresence(argv,idx);
		int res = Integer.parseInt(argv[idx + 1]);
		if (res < 0)
			throw new NumberFormatException(String.format(
					"The value of the argument %s must be non-negative",
					argv[idx]));
		return res;
	}
	
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
