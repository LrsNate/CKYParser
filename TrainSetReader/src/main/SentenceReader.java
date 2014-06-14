package main;

public class SentenceReader implements Runnable
{
	private final String		_line;
	
	public SentenceReader(String line)
	{
		this._line = line;
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
				Grammar.getInstance().addRule(tab);
		}
		catch (IllegalArgumentException e)
		{
			Messages.error(e.getMessage());
		}
	}

}
