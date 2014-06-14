
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
	private final ConcurrentHashMap<String, RewritingRule>	_map;
	
	private final static Grammar	_instance = new Grammar();
	private final static int		_defaultMapSize = 5000;

	private Grammar()
	{
		this._map = new ConcurrentHashMap<String, RewritingRule>(
				Grammar._defaultMapSize);
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
		if (this._map.containsKey(tab[0]))
			this._map.get(tab[0]).addRule(tab[1]);
		else
			this._map.put(tab[0], new RewritingRule(tab[0], tab[1]));
	}
	
	public synchronized void addRule(String tab[])
	{
		if (this._map.containsKey(tab[0]))
			this._map.get(tab[0]).addRule(tab[1]);
		else
			this._map.put(tab[0], new RewritingRule(tab[0], tab[1]));
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
		for (RewritingRule r : this._map.values())
		{
			t = new RewritingRuleDisplayer(r, precision);
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
		for (RewritingRule r : this._map.values())
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
		for (RewritingRule r : this._map.values())
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
