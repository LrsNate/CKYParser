package main;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * A class which reads a file pointed to by a BufferedReader file descriptor,
 * and progressively generates non-weighted rewriting rules from it. These 
 * rules are to be returned to the program one at a time.
 */
public class CorpusReader
{
	private Grammar	_grammar;
	
	/**
	 * Default constructor.
	 * @param g A probabilistic context-free grammar.
	 */
	public CorpusReader(Grammar g)
	{
		this._grammar = g;
	}
	
	/**
	 * Read an input file.
	 * @param fd The file to read.
	 */
	public void read(BufferedReader fd)
	{
		String		line;
		try
		{
			while ((line = fd.readLine()) != null)
				ThreadPool.getInstance().submit(
						new SentenceReader(line, this._grammar)
				);
		}
		catch (IOException e)
		{
			Messages.error(e.getMessage());
		}
	}
}
