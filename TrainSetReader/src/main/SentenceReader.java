package main;
/**
 * The class that reads a parsed sentence from the training set and adds the extracted rules to the grammar.
 *
 */
public class SentenceReader implements Runnable
{
	private final String		_line;
	private final Grammar		_grammar;
	
	/**
	 * Default constructor.
	 * @param line A sentence read from the corpus.
	 * @param g The PCFG being constructed.
	 */
	public SentenceReader(String line, Grammar g)
	{
		this._line = line;
		this._grammar = g;
	}

	@Override
	public void run()
	{
		TreeNode	t;

		if (this._line.length() < 3)
			Messages.error("Wrong line format: " + this._line);
		try
		{
			t = new TreeNode(this._line.substring(2, this._line.length() - 1));
			for (String tab[] : t.dumpWordTab())
				this._grammar.addRule(tab);
		}
		catch (IllegalArgumentException e)
		{
			Messages.error(e.getMessage());
		}
	}

}
