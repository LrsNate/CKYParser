package main;

import java.util.LinkedList;
import java.lang.Math;

/**
 * A production (rewriting rule) with a probability.
 */
public class RewrRuleProb extends RewrRule implements Comparable<RewrRuleProb> {
	
	/**
	 * The value representing the probability of the rewriting rule.
	 * If no logarithmisation has been applied, 
	 * it stores a positive value in the interval [0,1].
	 */
	private double 	_prob;
	
	/**
	 * The counter that keeps track of the number of times that
	 * logarithmisation has been applied to the probability.
	 */
	private int 	_nlog = 0;
	
	/**
	 * Construct a probabilistic production rule.
	 * @param lhs Left-hand side.
	 * @param rhs Right-hand side.
	 * @param prob The probability of the rule.
	 */
	public RewrRuleProb(Symbol lhs, LinkedList<Symbol> rhs, double prob) {
		super(lhs,rhs);
		this._prob = prob;
		check_prob();
	}
	
	/**
	 * Construct a probabilistic production rule.
	 * @param lhs Left-hand side.
	 * @param rhs Right-hand side.
	 * @param prob The probability of the rule.
	 */
	public RewrRuleProb(Symbol lhs, RHS rhs, double prob) {
		super(lhs,rhs);
		this._prob = prob;
		check_prob();		
	}
	
	/**
	 * Deep copy constructor.
	 * @param rule The rule to copy.
	 */
	public RewrRuleProb(RewrRuleProb rule) {
		this(rule.getLHS(), rule.getRHS(), rule.getProbability());
	}
	
	/**
	 * Construct a probabilistic production rule with default probability of 1.
	 * @param lhs Left-hand side.
	 * @param rhs Right-hand side.
	 */
	public RewrRuleProb(Symbol lhs, LinkedList<Symbol> rhs) {
		this(lhs,rhs, 1.0);
	}
	
	/**
	 * Construct a probabilistic production rule from a string.
	 * @param str_prob_rule A string of the form "p Abc -> beta", 
	 * where p is the probability of the rule, 
	 * Abc is a name of a single non-terminal symbol,
	 * and beta is a string with several names of symbols separated by white-spaces.
	 */
	public RewrRuleProb(String str_prob_rule) {
		// the substring after the first occurrence of a whitespace
		// is the one that corresponds to the rewriting rule
		super(str_prob_rule.substring(str_prob_rule.indexOf(' ')+1));
		// what is before this whitespace is the probability of the rule
		String str_prob = str_prob_rule.substring(0,str_prob_rule.indexOf(' '));
		this._prob = Double.parseDouble(str_prob);
		check_prob();
	}
	
	/**
	 * Getter for _nlog.
	 * @return The number of times logarithmisation has been applied to the probability.
	 */
	public int getNLog() {
		return this._nlog;
	}
	
	/**
	 * Check if the probability is in the right format (in the interval [0,1]).
	 */
	private void check_prob() {
		if (!(this._prob > 0) || (this._prob > 1)) {
			throw new IllegalArgumentException();
		}
	}
	
	/**
	 * Modify the field containing the probability
	 * by multiplying it by the value passed in the parameter.
	 * @param alpha The multiplier.
	 * @return The new value of the probability.
	 */
	protected double mult_prob(double alpha) {
		if ((alpha < 0) || (alpha > 1)) {
			throw new IllegalArgumentException();
		}
		this._prob *= alpha;
		return this._prob;
	}
	
	/**
	 * Replace the probability of the rewriting rule by the logarithm of that probability.
	 * @return log(probability)
	 */
	protected double probTakeLog() {
		this._prob = Math.log(this._prob);
		this._nlog++;
		return this._prob;
	}
	
	
	@Override
	public String toString() {
		return this._prob + " " + super.toString();
	}
	
	
	@Override
	public int compareTo(RewrRuleProb r)
	{
		return (new Double(this._prob).compareTo(new Double(r._prob)));
	}
	
	/**
	 * Getter for _prob.
	 * @return The probability of the rule.
	 */
	public double getProbability()
	{
		return (this._prob);
	}
	
}
