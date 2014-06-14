package main;

import java.util.LinkedList;


public class RewrRuleProb extends RewrRule implements Comparable<RewrRuleProb> {

	private final double 	_prob;
	
	private void check_prob() {
		if ((this._prob < 0) || (this._prob > 1)) {
			throw new IllegalArgumentException();
		}
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
