package main;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.Map;

/**
 * A Production rule as defined in Noam Chomsky's definition of a formal
 * grammar. This particular implementation associates a left-hand side symbol
 * and the number of times it has been encountered throughout the program's
 * runtime and a set of right-hand side symbols.
 * @author Antoine LAFOUASSE
 *
 */
public class RewritingRule
{
	private final String							_lhs;
	private int										_lho;
	private final HashMap<String, AtomicInteger>	_rhs;
	
	private static final int				_defaultPrecision = 7;

	public RewritingRule(String lhs, String rhs)
	{
		this._lhs = lhs;
		this._lho = 1;
		this._rhs = new HashMap<String, AtomicInteger>();
		this._rhs.put(rhs, new AtomicInteger(1));
	}
	
	public void addRule(String rhs)
	{
		this._lho++;
		if (this._rhs.containsKey(rhs))
			this._rhs.get(rhs).incrementAndGet();
		else
			this._rhs.put(rhs, new AtomicInteger(1));
	}
	
	@Override
	@Deprecated
	public String toString()
	{
		return (this.toString(RewritingRule._defaultPrecision));
	}

	public String toString(int precision)
	{
		StringBuffer	res;
		double			prob;
		String			tk;
		
		res = new StringBuffer();
		tk = String.format("%%.%df ", precision);
		for (Map.Entry<String, AtomicInteger> e : this._rhs.entrySet())
		{
			prob = e.getValue().doubleValue() / (double) this._lho;
			res.append(String.format(tk, prob, precision));
			res.append(String.format("%s -> %s\n", this._lhs, e.getKey()));
		}
		return (res.toString());
	}
}