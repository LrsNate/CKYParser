package main;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;

/**
 * A utility class which processes the program's command line arguments and
 * loads file descriptors and a precision argument. Precision is provided via
 * a -p or --precision flag followed by a non-negative integer. Any other
 * argument or invalid precision argument is treated as a path to a file.
 * This class then loads BufferedReader instances and delivers them one by one.
 * If no valid files are provided, a BufferedReader pointing to standard input
 * is loaded.
 * @author Antoine LAFOUASSE
 * @see java.io.BufferedReader
 */
public class ArgumentParser
{
	private Integer						_precision;
	private LinkedList<BufferedReader>	_fds;
	
	private static int					_defaultPrecision = 10;

	/**
	 * Builds a new ArgumentParser and parses the wordtab given in argument.
	 * @param argv Originally the program's command line arguments.
	 */
	public ArgumentParser(String argv[])
	{
		this._fds = new LinkedList<BufferedReader>();
		this._precision = null;
		for (int i = 0; i < argv.length; i++)
		{
			if (argv[i].equals("-p") || argv[i].equals("--precision"))
			{
				try
				{
					this._precision = this.parsePrecision(argv, i);
					i++;
				}
				catch (NumberFormatException e)
				{
					Messages.warning(String.format(
							"invalid precision argument: %s",
							argv[i + 1]));
				}
			}
			else
			{
				try
				{
					this._fds.addLast(this.openFile(argv[i]));
					Messages.info(String.format("%s: opened succesfully.",
							argv[i]));
				}
				catch (FileNotFoundException e)
				{
					Messages.warning(e.getMessage());
					continue ;
				}
			}
		}
		this.setDefaultValues();
	}
	
	/**
	 * Returns and removes a file descriptor from the list parsed from the
	 * command line arguments.
	 * @return a BufferedReader instance or null if there are no file
	 * descriptors left.
	 */
	public BufferedReader getNextFile()
	{
		return (this._fds.pollFirst());
	}
	
	/**
	 * Returns a precision setting for the program. If it was not provided in
	 * the command line arguments, then a default value is returned.
	 * @return A non-negative integer.
	 */
	public int getPrecision()
	{
		return (this._precision);
	}
	
	/**
	 * Changes the default precision value, to which this parser will revert
	 * if a precision parameter is not provided through the command line.
	 * @param precision A non-negative integer.
	 */
	public static void setDefaultPrecision(int precision)
	{
		if (precision < 0)
			throw new IllegalArgumentException(
					"Precision must not be a negative value.");
		ArgumentParser._defaultPrecision = precision;
	}

	private BufferedReader openFile(String filename)
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
	
	private BufferedReader openStandardInput()
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
	
	private Integer parsePrecision(String argv[], int idx)
		throws NumberFormatException
	{
		int		res;

		if ((idx + 1) >= argv.length)
		{
			Messages.warning("missing precision argument.");
			return (null);
		}
		res = Integer.parseInt(argv[idx + 1]);
		if (res < 0)
			throw new NumberFormatException(String.format(
					"%d (must be non-negative)",
					res));
		Messages.info(String.format("precision set to: %d digits.",
				res));
		return (res);
	}
	
	private void setDefaultValues()
	{
		if (this._precision == null)
		{
			Messages.info(String.format(
					"setting precision to %d (default value)",
					ArgumentParser._defaultPrecision));
			this._precision = ArgumentParser._defaultPrecision;
		}
		if (this._fds.isEmpty())
		{
			Messages.info("no valid input files provided.");
			Messages.info("reading from standard input.");
			this._fds.addLast(this.openStandardInput());
		}
	}
}
