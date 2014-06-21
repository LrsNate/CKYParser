package main;

import java.util.LinkedList;
import java.lang.Math;


public class RewrRuleProb extends RewrRule implements Comparable<RewrRuleProb> {
	
	/**
	 * The value representing the probability of the rewriting rule.
	 * If no logarithmisation has been applied, 
	 * it stores a positive value in the interval [0,1]
	 */
	private double 	_prob;
	
	/**
	 * The counter that keeps track of the number of times
	 * logarithmisation has been applied to the probability
	 */
	private int 	_nlog = 0;
	
	/**
	 * 
	 * @return: the number of times logarithmisation has been applied to the probability
	 */
	public int getNLog() {
		return this._nlog;
	}
	
	private void check_prob() {
		if (!(this._prob > 0) || (this._prob > 1)) {
			throw new IllegalArgumentException();
		}
	}
	
	/**
	 * Modify the field containing the probability
	 * by multipliyng it by the value passed in the parameter list
	 * @param alpha: the multiplier
	 * @return: the new value of the probability
	 */
	protected double mult_prob(double alpha) {
		if ((alpha < 0) || (alpha > 1)) {
			throw new IllegalArgumentException();
		}
		this._prob *= alpha;
		return this._prob;
	}
	
	/**
	 * replace the probability of the rewriting rule by the logarithm of the probability
	 * @return: log(probability)
	 */
	protected double probTakeLog() {
		this._prob = Math.log(this._prob);
		this._nlog++;
		return this._prob;
	}
	
	public RewrRuleProb(Symbol lhs, LinkedList<Symbol> rhs, double prob) {
		super(lhs,rhs);
		this._prob = prob;
		check_prob();
	}
	
	public RewrRuleProb(Symbol lhs, RHS rhs, double prob) {
		super(lhs,rhs);
		this._prob = prob;
		check_prob();		
	}
	
	/**
	 * deep copy constructor
	 * @param rule
	 */
	public RewrRuleProb(RewrRuleProb rule) {
		this(rule.getLHS(), rule.getRHS(), rule.getProbability());
	}
	
	public RewrRuleProb(Symbol lhs, LinkedList<Symbol> rhs) {
		this(lhs,rhs, 1.0);
	}
	
	public RewrRuleProb(String str_prob_rule) {
		// the substring after the first occurrence of a whitespace
		// is the one that corresponds to the rewriting rule
		super(str_prob_rule.substring(str_prob_rule.indexOf(' ')+1));
		// what is before this whitespace is the probability of the rule
		String str_prob = str_prob_rule.substring(0,str_prob_rule.indexOf(' '));
		this._prob = Double.parseDouble(str_prob);
		check_prob();
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

	// 
	//@Override
	//public boolean equals(Object o)
	//{
	//	RewrRuleProb r;
//
	//	if (o == null || !(o instanceof RewrRuleProb))
	//		return (false);
	//	r = (RewrRuleProb) o;
	//	return (this._lhs.equals(r._lhs) && this._rhs.equals(r._rhs));
	//}
	
	public double getProbability()
	{
		return (this._prob);
	}
	

}
