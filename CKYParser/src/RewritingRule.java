import java.util.LinkedList;


/**
 * A rewriting rule.
 * @author Nate
 */
public class RewritingRule implements Comparable<RewritingRule>
{
	private final Symbol				_lhs;
	private final double				_prob;
	private final LinkedList<Symbol>	_rhs;
	
	public RewritingRule(Symbol lhs, LinkedList<Symbol> rhs, double prob)
	{
		this._lhs = lhs;
		this._rhs = rhs;
		this._prob = prob;
	}
	
	@Override
	public int compareTo(RewritingRule r)
	{
		return (new Double(this._prob).compareTo(new Double(r._prob)));
	}
	
	@Override
	public boolean equals(Object o)
	{
		RewritingRule	r;
		
		if (o == null || !(o instanceof RewritingRule))
			return (false);
		r = (RewritingRule)o;
		return (this._lhs.equals(r._lhs) && this._rhs.equals(r._rhs));
	}
	
	public Symbol getLHS()
	{
		return (this._lhs);
	}
	
	public double getProbability()
	{
		return (this._prob);
	}
	
	public LinkedList<Symbol> getRHS()
	{
		return (this._rhs);
	}
}
