package main;

import java.util.HashMap;
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
	private String						_lhs;
	private int							_lho;
	private HashMap<String, Integer>	_rhs;
	
	private static int					_defaultPrecision = 7;

	public RewritingRule(String lhs, String rhs)
	{
		this._lhs = lhs;
		this._lho = 1;
		this._rhs = new HashMap<String, Integer>();
		this._rhs.put(rhs, 1);
	}
	
	public void addRule(String rhs)
	{
		int		occ;

		this._lho++;
		if (this._rhs.containsKey(rhs))
			occ = this._rhs.get(rhs);
		else
			occ = 0;
		this._rhs.put(rhs, occ + 1);
	}
	
	public void display(int precision)
	{
		double			prob;
		String			tk;
		
		tk = String.format("%%.%df ", precision);
		for (Map.Entry<String, Integer> e : this._rhs.entrySet())
		{
			prob = (double) e.getValue() / (double) this._lho;
			System.out.printf(tk, prob, precision);
			System.out.printf("%s -> %s\n", this._lhs, e.getKey());
		}
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
		for (Map.Entry<String, Integer> e : this._rhs.entrySet())
		{
			prob = (double) e.getValue() / (double) this._lho;
			res.append(String.format(tk, prob, precision));
			res.append(String.format("%s -> %s\n", this._lhs, e.getKey()));
		}
		return (res.toString());
	}
}