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
 * a -p or --precision flag followed by a non-negative integer. The number of 
 * threads used by the program is provided via the -t or --nthreads flag. Any
 * other argument or invalid precision argument is treated as a path to a file.
 * This class then loads BufferedReader instances and delivers them one by one.
 * If no valid files are provided, a BufferedReader pointing to standard input
 * is loaded.
 * @author Antoine LAFOUASSE
 * @see java.io.BufferedReader
 */
public class ArgumentParser
{
	private final LinkedList<BufferedReader>	_fds;

	/**
	 * Builds a new ArgumentParser and parses the wordtab given in argument.
	 * @param argv Originally the program's command line arguments.
	 */
	public ArgumentParser(String argv[])
	{
		this._fds = new LinkedList<BufferedReader>();
		for (int i = 0; i < argv.length; i++)
		{
			if (argv[i].equals("-p") || argv[i].equals("--precision"))
			{
				try
				{
					ArgumentParser.parsePrecision(argv, i);
					i++;
				}
				catch (NumberFormatException e)
				{
					Messages.warning(String.format(
							"invalid precision argument: %s",
							argv[i + 1]));
				}
			}
			else if (argv[i].equals("-t") || argv[i].equals("--nthreads"))
			{
				try
				{
					ArgumentParser.parseNThreads(argv, i);
					i++;
				}
				catch (NumberFormatException e)
				{
					Messages.warning(String.format(
							"invalid nthreads argument: %s",
							argv[i + 1]));
				}
			}
			else if (argv[i].equals("-l") || argv[i].equals("--lexical"))
			{
				Messages.info("reader set to track lexical rules.");
				Environment.setLexical(true);
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
	
	private void setDefaultValues()
	{
		if (this._fds.isEmpty())
		{
			Messages.info("no valid input files provided.");
			Messages.info("reading from standard input.");
			this._fds.addLast(ArgumentParser.openStandardInput());
		}
	}
	
	private static BufferedReader openStandardInput()
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
	private static void parseNThreads(String argv[], int idx)
			throws NumberFormatException
	{
		int		res;

		if ((idx + 1) >= argv.length)
		{
			Messages.warning("missing threads argument.");
			return ;
		}
		res = Integer.parseInt(argv[idx + 1]);
		if (res <= 0)
			throw new NumberFormatException(String.format(
					"%d (must be non-negative)",
					res));
		Messages.info(String.format("multi-threading set to: %d threads.",
				res));
		Environment.setNThreads(res);
	}

	private static void parsePrecision(String argv[], int idx)
		throws NumberFormatException
	{
		int		res;

		if ((idx + 1) >= argv.length)
		{
			Messages.warning("missing precision argument.");
			return ;
		}
		res = Integer.parseInt(argv[idx + 1]);
		if (res < 0)
			throw new NumberFormatException(String.format(
					"%d (must be non-negative)",
					res));
		Messages.info(String.format("precision set to: %d digits.",
				res));
		Environment.setPrecision(res);
	}
}
