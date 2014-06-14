package main;

import java.util.LinkedList;

/**
 * The representation of a syntactic tree.
 * @author Antoine LAFOUASSE
 *
 */
public class TreeNode
{
	private final String					_value;
	private final LinkedList<TreeNode>		_children;
	
	private static String			_errorMessage =
			"could not be resolved to a bracketed sentence.";
	
	/**
	 * Builds a new syntactic tree.
	 * @param v The representation of the tree (under bracketed form).
	 */
	public TreeNode(String v)
	{
		int		end;
		String	tab[];

		if (v.isEmpty())
			throw new IllegalArgumentException("Empty value");
		if (v.charAt(0) == '(' && v.charAt(v.length() - 1) == ')')
			v = v.substring(1, v.length() - 1);
		if (!v.matches("^\\S+$")
			&& !v.matches("^\\S+ \\S+$")
			&& !v.matches("^\\S+( *\\(.+\\))+$"))
			throw new IllegalArgumentException(
					"\"" + v + "\" " + TreeNode._errorMessage);
		else if (!v.matches("^\\S+( *\\(.+\\))+$")
			&& Environment.hasLexical())
		{
			tab = v.split(" ");
			this._value = tab[0];
			this._children = new LinkedList<TreeNode>();
			if (tab.length > 1)
				this._children.addLast(new TreeNode(tab[1]));
			return ;
		}
		end = 0;
		while (v.charAt(end) != ' ' && v.charAt(end) != '-')
			end++;
		this._value = v.substring(0, end);
		this._children = new LinkedList<TreeNode>();
		while ((v = TreeNode.skipToNextChild(v)) != null)
			this._children.addLast(new TreeNode(TreeNode.getNextChild(v)));
	}

	public LinkedList<String[]> dumpWordTab()
	{
		LinkedList<String[]>		res;
		
		res = new LinkedList<String[]>();
		if (!this._children.isEmpty())
		{
			res.add(this.toWordTab());
			for (TreeNode n : this._children)
				res.addAll(n.dumpWordTab());
		}
		return (res);
	}
	
	/**
	 * Returns the tree's syntactic category.
	 * @return The tree root's syntactic category.
	 */
	public String getValue()
	{
		return (this._value);
	}
	
	@Override
	public String toString()
	{
		StringBuffer	s;

		s = new StringBuffer(this._value);
		if (!this._children.isEmpty())
			s.append(" ->");
		for (TreeNode n : this._children)
		{
			s.append(' ');
			s.append(n.getValue());
		}
		return (s.toString());
	}
	
	public String[] toWordTab()
	{
		String				res[];
		StringBuffer		s;
		
		res = new String[2];
		res[0] = this._value;
		s = new StringBuffer();
		for (TreeNode n : this._children)
			s.append(" " + n.getValue());
		s.deleteCharAt(0);
		res[1] = s.toString();
		return (res);
	}
	
	private static String getNextChild(String s)
	{
		int		end;
		int		nest;
		
		end = 0;
		nest = 0;
		while (s.charAt(end) != ')' || nest > 1)
		{
			if (s.charAt(end) == '(')
				nest++;
			else if (s.charAt(end) == ')')
				nest--;
			end++;
		}
		return (s.substring(0, end + 1));
	}
	
	private static String skipToNextChild(String s)
	{
		int		idx;
		int		nest;

		idx = 0;
		if (!s.contains(")"))
			return (null);
		else if (!s.startsWith("("))
		{
			while (s.charAt(idx) != '(')
				idx++;
			return (s.substring(idx));
		}
		else
		{
			nest = 0;
			while (s.charAt(idx) != ')' || nest > 1)
			{
				if (s.charAt(idx) == '(')
					nest++;
				else if (s.charAt(idx) == ')')
					nest--;
				idx++;
			}
			try
			{
				while (s.charAt(idx) != '(')
					idx++;
				return (s.substring(idx));
			}
			catch (StringIndexOutOfBoundsException e)
			{
				return (null);
			}
		}
	}
}
