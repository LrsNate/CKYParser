package main;

/**
 * A derivation tree of a phrase.
 *
 */
public class Tree implements Comparable<Tree>
{
	/**
	 * The symbol at the root of the tree (corresponding to the left-hand side of the derivation chain).
	 */
	private Symbol	_value;
	
	/**
	 * The probability of the whole tree.
	 */
	private double	_prob;
	private Tree	_left = null;
	private Tree	_right = null;
	
	/**
	 * Default constructor of a tree with a default probability of 1 and without children (branches).
	 * @param value The symbol at the root of the tree.
	 */
	public Tree(Symbol value)
	{
		this._value = value;
		this._prob = 1;
	}
	
	/**
	 * Construct a tree with a given probability and one child.
	 * @param value The symbol at the root of the tree.
	 * @param prob The probability of the whole tree.
	 * @param left The unique child (subtree).
	 */
	public Tree(Symbol value, double prob, Tree left)
	{
		this(value);
		this._prob = prob;
		this._left = left;
		//this._right = null; 
	}
	
	/**
	 * Construct a tree with a given probability and two children.
	 * @param value The symbol at the root of the tree.
	 * @param prob The probability of the whole tree.
	 * @param left The left child.
	 * @param right The right child.
	 */
	public Tree(Symbol value, double prob, Tree left, Tree right)
	{
		this(value, prob, left);
		this._right = right;
	}
	
	/**
	 * Construct a tree from a rewriting rule. 
	 * The left-hand side of the rule becomes the root of the tree,
	 * and the right-hand side becomes its children
	 * (the RHS must contain one or two symbols, as the grammar used 
	 * is in Chomsky Normal Form).
	 * @param r The rewriting rule that the tree is based on.
	 */
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
	 * Getter for _value.
	 * @return The Symbol at the root of the tree.
	 */
	public Symbol getRoot() {
		return _value;
	}
	
	/**
	 * Getter for _prob.
	 * @return The probability of the tree.
	 */
	public double getProb() {
		return _prob;
	}
	
	@Override
	public int compareTo(Tree t)
	{
		return (new Double(this._prob).compareTo(new Double(t._prob)));
	}
	
	/**
	 * Convert the tree in the form of a string.
	 * @param with_prob Set to true if the probability of the tree must be included.
	 * @return A string representation of the tree.
	 */
	public String treeToString(boolean with_prob)
	{
		StringBuffer	s;
		
		if (this._left == null)
			return (this._value.toString());
		s = new StringBuffer("(");
		s.append(this._value);
		s.append(" ");
		s.append(this._left.treeToString(with_prob));
		if (this._right != null)
		{
			s.append(" ");
			s.append(this._right.treeToString(with_prob));
		}
		s.append(")");
		return (s.toString());
	}
	
	/**
	 * Convert the tree in the form of a string.
	 * @return A string representation of the tree.
	 */
	public String treeToString() {
		return treeToString(false);
	}
	
	@Override
	public String toString()
	{
		return (Double.toString(this._prob) + " " + this.treeToString());
	}
	
	/**
	 * Compare the tree to another tree.
	 * @param t The tree to compare.
	 * @return True if the trees are identical, false otherwise.
	 */
	@Override
	public boolean equals(Object o)
	{
		if (!(o instanceof Tree))
			return false;
		Tree t = (Tree)o;
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
				return ((this._value.equals(t._value)) && (!(Math.abs(this._prob - t._prob) > 0)));
			}
			else return ((this._value.equals(t._value)) && (!(Math.abs(this._prob - t._prob) > 0)) && (this._left.equals(t._left)));
		}
		return ((this._value.equals(t._value)) && (!(Math.abs(this._prob - t._prob) > 0)) && (this._left.equals(t._left)) && (this._right.equals(t._right)));
	}
}
