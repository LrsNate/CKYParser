
public class Tree implements Comparable<Tree>
{
	private String	_value;
	private double	_prob;
	private Tree	_left;
	private Tree	_right;
	
	public Tree(String value)
	{
		this._value = value;
		this._prob = 1;
		this._left = null;
		this._right = null;
	}
	
	public Tree(String value, double prob, Tree left)
	{
		this(value);
		this._prob = prob;
		this._left = left;
	}
	
	public Tree(String value, double prob, Tree left, Tree right)
	{
		this(value, prob, left);
		this._right = right;
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
}
