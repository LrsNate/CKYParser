package main;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * A class which reads a file pointed to by a BufferedReader file descriptor,
 * and progressively generates non-weighted rewriting rules from it. These 
 * rules are to be returned to the program one at a time.
 * @author Antoine LAFOUASSE
 */
public class CorpusReader
{
	private Grammar	_grammar;
	
	public CorpusReader(Grammar g)
	{
		this._grammar = g;
	}
	
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
