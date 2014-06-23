package main;

/**
 * A class for printing rewriting rules with their probabilities in the output stream.
 *
 */
public class RewrRuleDisplayer implements Runnable
{
	private final int			_precision;
	private final RewrRuleCounter	_rule;

	/**
	 * For a LHS and for a given precision,
	 * create a displayer of all the rules with that LHS.
	 * @param r The counter of rules with a given LHS.
	 * @param p Number of digits after the decimal separator.
	 */
	public RewrRuleDisplayer(RewrRuleCounter r, int p)
	{
		this._precision = p;
		this._rule = r;
	}

	@Override
	public void run()
	{
		Environment.getOutputStream().println(this._rule.toString(this._precision));
	}

}
