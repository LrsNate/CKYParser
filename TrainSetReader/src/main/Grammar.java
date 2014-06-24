
package main;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


/**
 * The modelisation of a Probabilistic Context-Free Grammar (PCFG). It consists
 * of a set of weighted rewriting rules, with each rule having its probability
 * processed at display time.
 */
public class Grammar
{	
	private final ConcurrentHashMap<Symbol, RewrRuleCounter>	_map;
	
	private final static int		_defaultMapSize = 5000;
	

	/**
	 * Default constructor.
	 */
	public Grammar()
	{
		this._map = new ConcurrentHashMap<Symbol, RewrRuleCounter>(
				Grammar._defaultMapSize);
	}

	/**
	 * Add a rule to the grammar and either create a new entry or increment
	 * its occurrence count.
	 * @param rule The rule to be added.
	 * @return The counter of the rule that has just been added.
	 */
	public synchronized RewrRuleCounter addRule(String rule)
	{
		String		tab[];
		tab = rule.split("->");
		if (tab.length != 2)
			throw new IllegalArgumentException("Grammar: illegal rule");
		for (int i = 0; i < tab.length; i++)
			tab[i] = tab[i].trim();
		Symbol lhs = new Symbol(tab[0]);
		RHS rhs = new RHS(tab[1]);
		RewrRuleCounter rule_counter = null;
		if (this._map.containsKey(lhs)) {
			rule_counter = this._map.get(lhs);
			rule_counter.addRule(rhs);
		} else {
			rule_counter = new RewrRuleCounter(lhs, rhs);
			this._map.put(lhs, rule_counter);
		}
		
		return rule_counter;
	}
	
	/**
	 * Add a rewriting rule to the grammar. The rules are grouped by left-hand side.
	 * @param tab A string representing a rule of the form A -> B C.
	 */
	public synchronized RewrRuleCounter addRule(String tab[])
	{
		Symbol lhs = new Symbol(tab[0]);
		RHS rhs = new RHS(tab[1]);
		RewrRuleCounter rule_counter = null;
		if (this._map.containsKey(lhs)) {
			rule_counter = this._map.get(lhs);
			rule_counter.addRule(new RHS(tab[1]));
		}
		else {
			rule_counter = new RewrRuleCounter(lhs, rhs);
			this._map.put(lhs, rule_counter);
		}
		
		return rule_counter;
	}
	
	/**
	 * Display the grammar with a given precision.
	 * @param precision The number of digits after the decimal separator.
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
	 * Return a String representation of the grammar, i.e. all the rules
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
}
