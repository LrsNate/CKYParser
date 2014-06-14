package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.LinkedList;

/**
 * A class which reads a file pointed to by a BufferedReader file descriptor,
 * and progressively generates non-weighted rewriting rules from it. These 
 * rules are to be returned to the program one at a time.
 * @author Antoine LAFOUASSE
 */
public class CorpusReader
{
	private LinkedList<String>	_buffer;
	private BufferedReader		_fd;
	
	/**
	 * Instantiates a new reader with the file descriptor given in argument.
	 * @param in a BufferedReader pointing to a valid corpus.
	 * @see java.io.BufferedReader
	 */
	public CorpusReader(BufferedReader in)
	{
		this._buffer = new LinkedList<String>();
		this._fd = in;
	}
	
	/**
	 * Returns a rewriting rule from the file loaded in the reader, processing
	 * part of the file if needed.
	 * @return A non-weighted rewriting rule under the form "lvalue -> rvalue".
	 * @throws IOException If a file reading failure occurred.
	 */
	public String getNextRule() throws IOException
	{
		TreeNode	n;
		
		if (this._buffer.isEmpty())
		{
			if ((n = this.getNextTree()) == null)
				return (null);
			for (String s : n.dump().split("\n"))
				this._buffer.addLast(s);
		}
		return (this._buffer.pollFirst());
	}
	
	private TreeNode getNextTree() throws IOException
	{
		String		line;
		
		if ((line = this._fd.readLine()) == null)
			return (null);
		if (line.length() < 3)
			throw new IOException ("Wrong file format");
		line = line.substring(2, line.length() - 1);
		try
		{
			return (new TreeNode(line));
		}
		catch (IllegalArgumentException e)
		{
			Messages.error(e.getMessage());
			return (null);
		}
	}
}
