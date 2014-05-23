import java.util.LinkedList;


public class RewrRuleProb extends RewrRule implements Comparable<RewrRuleProb> {

	private final double 	_prob;
	
	public RewrRuleProb(Symbol lhs, LinkedList<Symbol> rhs, double prob) {
		super(lhs,rhs);
		this._prob = prob;
	}
	
	public RewrRuleProb(Symbol lhs, LinkedList<Symbol> rhs) {
		this(lhs,rhs, 1.0);
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
