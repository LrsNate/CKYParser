package main;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.Map;
import java.util.Set;

/**
 * A production rule as defined in Noam Chomsky's definition of a formal
 * grammar. This particular implementation associates a left-hand side symbol
 * and the number of times it has been encountered throughout the program's
 * runtime and a set of right-hand side symbols.
 *
 */
public class RewrRuleCounter
{
	/**
	 * The left-hand side of the production rules that are counted.
	 */
	private final Symbol						_lhs;
	/**
	 * The total number of occurrences of this left-hand side in the training corpus.
	 */
	private int									_lho;
	/**
	 * The counters of rules that have this._lhs as their left-hand side.
	 * key: the right-hand side of the rule
	 * value: the counter of the rule of the form (this._lhs -> value)
	 */
	private final HashMap<RHS, AtomicInteger>	_rhs;
	
	/**
	 * Set to true if there exists at least one right-hand side that consists of a single terminal symbol.
	 */
	private boolean 							_has_lexical = false;
	
	private static final int					_defaultPrecision = 10;

	/**
	 * Default constructor.
	 * @param lhs The left-hand side of the rule.
	 * @param rhs The right-hand side of the rule.
	 */
	public RewrRuleCounter(Symbol lhs, RHS rhs)
	{
		this._lhs = lhs;
		this._lho = 1;
		this._rhs = new HashMap<RHS, AtomicInteger>();
		this._rhs.put(rhs, new AtomicInteger(1));
		checkHasLexical(rhs);
	}
	
	/**
	 * Add a new right-hand side for this left-hand side.
	 * @param rhs The RHS to add.
	 */
	public void addRule(RHS rhs)
	{
		this._lho++;
		if (this._rhs.containsKey(rhs))
			this._rhs.get(rhs).incrementAndGet();
		else
			this._rhs.put(rhs, new AtomicInteger(1));
		checkHasLexical(rhs);
	}
	
	/**
	 * Check if there exists at least one right-hand side that consists of a single terminal symbol.
	 * @param rhs The right-hand side of a rewriting rule that has just been added to the grammar.
	 * @return True if there exists at least one single terminal RHS.
	 */
	private boolean checkHasLexical(RHS rhs) {
		if (!(this._has_lexical) && (rhs.size() == 1) && (rhs.get(0).IsTerminal())) {
			this._has_lexical = true;
		}
		return this._has_lexical;
	}
	
	/**
	 * Get all the right-hand sides for this left-hand side.
	 * @return The set of all right-hand sides of all the rules counted by this object.
	 */
	public Set<RHS> getRHS() {
		return this._rhs.keySet();
	}
	
	/**
	 * Redistribute counts between an existing right-hand side to another given RHS.
	 * @param rhs The RHS to take the counts away from.
	 * @param recipient_rhs The RHS that the counts are being given to.
	 */
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
	 * that has a single terminal symbol as its right-hand side.
	 * @return True if there exists at least one right-hand side that consists of a single terminal symbol.
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

	/**
	 * Convert the list of rules in the counter to a string.
	 * @param precision The number of digits after the decimal separator to be printed in the probabilities.
	 * @return A string containing all the rules for this LHS with their probabilities.
	 */
	public String toString(int precision)
	{
		StringBuffer	res;
		double			prob;
		String			tk;
		
		res = new StringBuffer();
		tk = String.format("%%.%df ", precision);
		for (Map.Entry<RHS, AtomicInteger> e : this._rhs.entrySet())
		{
			prob = e.getValue().doubleValue() / (double) this._lho; //calculate the probability of each production
			res.append(String.format(tk, prob, precision));
			res.append(String.format("%s -> %s\n", this._lhs.toString(), e.getKey().toString()));
		}
		return (res.toString());
	}
}