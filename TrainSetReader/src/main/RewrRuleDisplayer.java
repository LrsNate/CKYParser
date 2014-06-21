package main;

public class RewrRuleDisplayer implements Runnable
{
	private final int			_precision;
	private final RewrRuleCounter	_rule;

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
