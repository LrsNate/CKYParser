package main;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
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
 * @see java.io.BufferedReader
 */
public class ArgumentParser
{
	/**
	 * A list of input files.
	 */
	private final LinkedList<BufferedReader>	_fds;

	/**
	 * Builds a new ArgumentParser and parses the wordtab given in argument.
	 * @param argv Originally the program's command line arguments.
	 */
	public ArgumentParser(String argv[])
	{
		this._fds = new LinkedList<BufferedReader>();
		try {
		int i = 0;
		while (i < argv.length)
		{
			if (argv[i].equals("-p") || argv[i].equals("--precision"))
			{
					ArgumentParser.parsePrecision(argv, i);
					i++;
				
			}
			else if (argv[i].equals("-t") || argv[i].equals("--nthreads"))
			{
					ArgumentParser.parseNThreads(argv, i);
					i++;
			}
			else if (argv[i].equals("-u") || argv[i].equals("--unknown-threshold"))
			{
					ArgumentParser.parseUnknownThreshold(argv, i);
					i++;
			}
			else if (argv[i].equals("-s") || argv[i].equals("--unknown-label"))
			{
				ArgumentParser.parseUnknownLabel(argv, i);
				i++;
			}
			else if (argv[i].equals("-l") || argv[i].equals("--lexical"))
			{
				Messages.info("reader set to track lexical rules.");
				Environment.setLexical(true);
			} else if (argv[i].equals("-o") || argv[i].equals("--output_file"))
			{
				ArgumentParser0.checkArgumentPresence(argv, i);
				Environment.setOutputStream(ArgumentParser0.openOutputFile(argv[i+1]));
				i++;
			} else if (argv[i].equals("--get-bare-phrase"))
			{
				Environment.setAnnotationStripperOption(AnnotationStripperOption.LEX);
			} else if (argv[i].equals("--get-categories"))
			{
				Environment.setAnnotationStripperOption(AnnotationStripperOption.CAT);
			} else if (argv[i].equals("--get-tagged-phrase"))
			{
				Environment.setAnnotationStripperOption(AnnotationStripperOption.LEX_N_CAT);
			}
			else
			{
				try
				{
					this._fds.addLast(ArgumentParser0.openFile(argv[i]));
					Messages.info(String.format("%s: opened succesfully.",
							argv[i]));
				}
				catch (FileNotFoundException e)
				{
					Messages.warning(e.getMessage());
					continue ;
				}
			}
			i++;
		}
		this.setDefaultValues();
		} catch (NumberFormatException e) {
			Messages.warning(e.getMessage());
		} catch (MissingArgumentValueException e1) {
			Messages.warning(e1.getMessage());
		} catch (FileNotFoundException e2) {
			Messages.warning(e2.getMessage());
		}
	}
	
	/**
	 * Returns and removes a file descriptor from the list parsed from the
	 * command line arguments.
	 * @return A BufferedReader instance or null if there are no file
	 * descriptors left.
	 */
	public BufferedReader getNextFile()
	{
		return (this._fds.pollFirst());
	}
	
	/**
	 * Set default configuration (if no input files are provided, read from standard input. If no output files are provided, 
	 * print to standard output).
	 */
	private void setDefaultValues()
	{
		if (this._fds.isEmpty())
		{
			Messages.info("no valid input files provided.");
			Messages.info("reading from standard input.");
			this._fds.addLast(ArgumentParser0.openStandardInput());
		}
		if (Environment.getOutputStream() == null) {
			Environment.setOutputStream(new PrintWriter(System.out, true));
		}
	}
	
	/**
	 * Read the number of threads from the command-line option.
	 * @param argv The command line arguments.
	 * @param idx The position of the --nthread argument.
	 * @throws NumberFormatException
	 * @throws MissingArgumentValueException
	 */
	private static void parseNThreads(String argv[], int idx)
			throws NumberFormatException, MissingArgumentValueException
	{
		int		res;
		res = ArgumentParser0.parsePositiveInt(argv, idx);
		Messages.info(String.format("multi-threading set to: %d threads.",
				res));
		Environment.setNThreads(res);
	}

	/**
	 * Read the precision of printing numbers like probabilities.
	 * @param argv The command line arguments.
	 * @param idx The position of the --precision argument.
	 * @throws NumberFormatException
	 * @throws MissingArgumentValueException
	 */
	private static void parsePrecision(String argv[], int idx)
		throws NumberFormatException, MissingArgumentValueException
	{
		int		res;
		res = ArgumentParser0.parsePositiveInt(argv, idx);
		Environment.setPrecision(res);
	}
	
	/**
	 * Read the threshold for rare words. If the number of times the word is found in the training set
	 * is inferior to this threshold, the word is considered rare, and its probability is counted
	 * in the probability of producing an unknown word.
	 * @param argv The command line arguments.
	 * @param idx The position of the --unknown-threshold argument.
	 * @throws NumberFormatException
	 * @throws MissingArgumentValueException
	 */
	private static void parseUnknownThreshold(String argv[], int idx)
			throws NumberFormatException, MissingArgumentValueException
	{
		int		res;
		res = ArgumentParser0.parseNonNegativeInt(argv, idx);
		Messages.info(String.format("unknown threshold set to: %d.",
				res));
		Environment.setUnknownThreshold(res);
	}

	/**
	 * Read the label for unknown words.
	 * @param argv The command line arguments.
	 * @param idx The position of the --unknown-label argument.
	 * @throws NumberFormatException
	 * @throws MissingArgumentValueException
	 */
	private static void parseUnknownLabel(String argv[], int idx)
			throws NumberFormatException, MissingArgumentValueException
	{
		ArgumentParser0.checkArgumentPresence(argv,  idx);
		Messages.info(String.format("unknown label set to: %s.",
				argv[idx + 1]));
		Environment.setUnknownLabel(argv[idx + 1]);
	}
}
