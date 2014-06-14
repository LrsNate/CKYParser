package main;

public class RewritingRuleDisplayer implements Runnable
{
	private final int			_precision;
	private final RewritingRule	_rule;

	public RewritingRuleDisplayer(RewritingRule r, int p)
	{
		this._precision = p;
		this._rule = r;
	}

	@Override
	public void run()
	{
		System.out.print(this._rule.toString(this._precision));
	}

}
