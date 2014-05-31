
public class Tree implements Comparable<Tree>
{
	/**
	 * the symbol at the root of the tree
	 */
	private Symbol	_value;
	/**
	 * the probability of the whole tree 
	 */
	private double	_prob;
	private Tree	_left = null;
	private Tree	_right = null;
	
	public Tree(Symbol value)
	{
		this._value = value;
		this._prob = 1;
		//this._left = null;
		//this._right = null;
	}
	
	public Tree(Symbol value, double prob, Tree left)
	{
		this(value);
		this._prob = prob;
		this._left = left;
		//this._right = null; 
	}
	
	public Tree(Symbol value, double prob, Tree left, Tree right)
	{
		this(value, prob, left);
		this._right = right;
	}
	
	public Tree(RewrRuleProb r)
	{
		this(r.getLHS());
		this._prob = r.getProbability();
		RHS rhs = r.getRHS();
		Tree left = new Tree(rhs.get(0));
		this._left = left;
		if(rhs.size() > 1) {
			Tree right = new Tree(rhs.get(1));
			this._right = right;
		}
	}
	/**
	 * 
	 * @return: the Symbol at the root of the tree
	 */
	public Symbol getRoot() {
		return _value;
	}
	
	/**
	 * 
	 * @return: the probability of the tree
	 */
	public double getProb() {
		return _prob;
	}
	
	@Override
	public int compareTo(Tree t)
	{
		return (new Double(this._prob).compareTo(new Double(t._prob)));
	}
	
	@Override
	public String toString()
	{
		StringBuffer	s;
		
		if (this._left == null)
			return (this._value.toString());
		s = new StringBuffer("(");
		s.append(this._value);
		s.append(" ");
		s.append(this._left);
		if (this._right != null)
		{
			s.append(" ");
			s.append(this._right);
		}
		s.append(")");
		return (s.toString());
	}
	
	public boolean equals(Tree t) 
	{
		if (this._right == null && t._right != null) {
			return false;
		}
		if (this._right != null && t._right == null) {
			return false;
		}
		if (this._left == null && t._left != null) {
			return false;
		}
		if (this._left != null && t._left == null) {
			return false;
		}
		if (this._right == null) {
			if (this._left == null) {
				return ((this._value.equals(t._value)) && (this._prob == t._prob));
			}
			else return ((this._value.equals(t._value)) && (this._prob == t._prob) && (this._left.equals(t._left)));
		}
		return ((this._value.equals(t._value)) && (this._prob == t._prob) && (this._left.equals(t._left)) && (this._right.equals(t._right)));
	}
}
