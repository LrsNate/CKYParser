package main;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.Map;
import java.util.Set;

/**
 * A Production rule as defined in Noam Chomsky's definition of a formal
 * grammar. This particular implementation associates a left-hand side symbol
 * and the number of times it has been encountered throughout the program's
 * runtime and a set of right-hand side symbols.
 * @author Antoine LAFOUASSE
 *
 */
public class RewrRuleCounter
{
	/**
	 * the left-hand side of the production rules that are counted 
	 */
	private final Symbol						_lhs;
	/**
	 * the total number of occurrences of this left-hand side in the training corpus
	 */
	private int									_lho;
	/**
	 * the counters of rules that have this._lhs as their left-hand side
	 * key: the right-hand side of the rule
	 * value: the counter of the rule of the form (this._lhs -> value)
	 */
	private final HashMap<RHS, AtomicInteger>	_rhs;
	
	/**
	 * is true if there exists at least one right-hand side that consists of a single terminal symbol
	 */
	private boolean 							_has_lexical = false;
	
	private static final int					_defaultPrecision = 7;

	public RewrRuleCounter(Symbol lhs, RHS rhs)
	{
		this._lhs = lhs;
		this._lho = 1;
		this._rhs = new HashMap<RHS, AtomicInteger>();
		if ((rhs.size() == 1) && lhs.equals(rhs.get(0))) { return; }
		this._rhs.put(rhs, new AtomicInteger(1));
		checkHasLexical(rhs);
	}
	
	public void addRule(RHS rhs)
	{
		if ((rhs.size() == 1) && this._lhs.equals(rhs.get(0))) { return; }
		this._lho++;
		if (this._rhs.containsKey(rhs))
			this._rhs.get(rhs).incrementAndGet();
		else
			this._rhs.put(rhs, new AtomicInteger(1));
		checkHasLexical(rhs);
	}
	
	/**
	 * Check if there exists at least one right-hand side that consists of a single terminal symbol
	 * @param rhs: the right-hand side of a rewriting rule that has just been added to the grammar 
	 * @return: this._has_lexical
	 */
	private boolean checkHasLexical(RHS rhs) {
		if (!(this._has_lexical) && (rhs.size() == 1) && (rhs.get(0).IsTerminal())) {
			this._has_lexical = true;
		}
		return this._has_lexical;
	}
	
	/**
	 * 
	 * @return: the set of all right-hand sides of all the rules counted by this object
	 */
	public Set<RHS> getRHS() {
		return this._rhs.keySet();
	}
	
	protected void redistributeCounts(RHS rhs, RHS recipient_rhs) {
		if (!this._rhs.containsKey(rhs)) {
			throw new IllegalArgumentException("Trying to redistribute counts from a rewriting rule that does not exist");
		}
		// take the counts away from rhs
		int counts = this._rhs.get(rhs).get();
		this._rhs.remove(rhs);
		// give them to new_rhs
		if (this._rhs.containsKey(recipient_rhs))
			this._rhs.get(recipient_rhs).getAndAdd(counts);
		else
			this._rhs.put(recipient_rhs, new AtomicInteger(counts));
	}
	
	/**
	 * Check if in the set of rules that are counted by this object there is at least one 
	 * that has a single terminal symbol as its right-hand side
	 * @return: true if there exists at least one right-hand side that consists of a single terminal symbol
	 */
	public boolean hasLexical() {
		return this._has_lexical;
	}
	
	@Override
	@Deprecated
	public String toString()
	{
		return (this.toString(RewrRuleCounter._defaultPrecision));
	}

	public String toString(int precision)
	{
		StringBuffer	res;
		double			prob;
		String			tk;
		
		res = new StringBuffer();
		tk = String.format("%%.%df ", precision);
		for (Map.Entry<RHS, AtomicInteger> e : this._rhs.entrySet())
		{
			prob = e.getValue().doubleValue() / (double) this._lho;
			res.append(String.format(tk, prob, precision));
			res.append(String.format("%s -> %s\n", this._lhs.toString(), e.getKey().toString()));
		}
		return (res.toString());
	}
}