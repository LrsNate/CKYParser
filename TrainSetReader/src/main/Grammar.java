
package main;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


/**
 * The modelisation of a Probabilistic Context-Free Grammar (PCFG). It consists
 * in a set of weighted rewriting rules, with each rule having its probability
 * processed at display time.
 * @author Antoine LAFOUASSE
 */
public final class Grammar
{	
	private final ConcurrentHashMap<Symbol, RewrRuleCounter>	_map;
	
	private final static Grammar	_instance = new Grammar();
	private final static int		_defaultMapSize = 5000;
	
	/**
	 * _count_unknown : if (_count_unknown == true), \
	 * then for every occurrence of a lexical rule (A -> a) \
	 * add the rule (A -> **UNKNOWN**) to the counter
	 */
	private boolean 	_count_unknown;
	/**
	 * the right-hand side of a rule 
	 * conventionnaly chosen to represent all unknown terminals
	 */
	private RHS 	    _unknown_rhs;

	private Grammar()
	{
		this._map = new ConcurrentHashMap<Symbol, RewrRuleCounter>(
				Grammar._defaultMapSize);
	}
	
	private Grammar(boolean _count_unknown, String unknown_label)
	{
		this();
		this._count_unknown = _count_unknown;
		this._unknown_rhs = new RHS(new Symbol(unknown_label, true));
	}

	/**
	 * Adds a rule to the grammar and either creates a new entry or increments
	 * its occurrence count.
	 * @param rule The rule to be added.
	 */
	public void addRule(String rule)
	{
		String		tab[];
		
		tab = rule.split("->");
		if (tab.length != 2)
			throw new IllegalArgumentException("Grammar: illegal rule");
		for (int i = 0; i < tab.length; i++)
			tab[i] = tab[i].trim();
		Symbol lhs = new Symbol(tab[0]);
		RHS rhs = new RHS(tab[1]);
		if (this._map.containsKey(lhs))
			this._map.get(lhs).addRule(rhs);
		else
			this._map.put(lhs, new RewrRuleCounter(lhs, rhs));
		
		// If the mode _count_unknown is known
		// and the rule under consideration is a lexical one,
		// then its LHS is a morpho-syntactic category Cat.
		// Add the unknown terminal to the counter of this Category.
		if (this._count_unknown && ((rhs.size() == 1) && (rhs.get(0).IsTerminal()))) {
			this._map.get(lhs).addRule(_unknown_rhs);
		}
	}
	
	public synchronized void addRule(String tab[])
	{
		Symbol lhs = new Symbol(tab[0]);
		if (this._map.containsKey(lhs))
			this._map.get(lhs).addRule(new RHS(tab[1]));
		else
			this._map.put(lhs, new RewrRuleCounter(lhs, new RHS(tab[1])));
	}
	
	/**
	 * 
	 * @param precision
	 */
	public void display(int precision)
	{
		ExecutorService		e;
		Runnable			t;
		
		e = Executors.newFixedThreadPool(
				Environment.getNThreads());
		for (RewrRuleCounter r : this._map.values())
		{
			t = new RewrRuleDisplayer(r, precision);
			e.submit(t);
		}
		e.shutdown();
		try
		{
			while (!e.awaitTermination(1, TimeUnit.SECONDS));
		}
		catch (InterruptedException ex)
		{
			Messages.error(ex.getMessage());
		}
	}

	@Deprecated
	@Override
	public String toString()
	{
		StringBuffer	s;
		
		s = new StringBuffer();
		for (RewrRuleCounter r : this._map.values())
			s.append(r.toString());
		return (s.toString());
	}
	
	/**
	 * Returns a String representation of the grammar, i.e. all the rules
	 * contained in it.
	 * @param precision The precision (in digits after the decimal separator)
	 * with which the rules' frequency is to be displayed.
	 * @return A String instance with one rewriting rule per line, each line
	 * stating frequency, left-hand value, a separator and right-hand values.
	 */
	public String toString(int precision)
	{
		StringBuffer	s;
		
		s = new StringBuffer();
		for (RewrRuleCounter r : this._map.values())
			s.append(r.toString(precision));
		return (s.toString());
	}
	
	/**
	 * Fetches and return the singleton instance of the grammar.
	 * @return A unique grammar object.
	 */
	public static Grammar getInstance()
	{
		return (Grammar._instance);
	}
}
