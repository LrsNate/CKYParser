package main;

import java.util.LinkedList;

/**
 * The representation of a syntactic tree.
 * @author Antoine LAFOUASSE
 *
 */
public class TreeNode
{
	private final Symbol					_value;
	private final LinkedList<TreeNode>		_children;
	
	private static String			_errorMessage =
			"could not be resolved to a bracketed sentence.";
	
	/**
	 * creates a tree consisting of the root only
	 * with empty string as the label of the root
	 */
	public TreeNode(Symbol value) {
		this._value = value;
		this._children = new  LinkedList<TreeNode>();
	}
	
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
			this._value = new Symbol(tab[0]);
			this._children = new LinkedList<TreeNode>();
			if (tab.length > 1)
				this._children.addLast(new TreeNode(tab[1])); // ???????????????????????
			return ;
		}
		end = 0;
		while (v.charAt(end) != ' ' && v.charAt(end) != '-')
			end++;
		this._value = new Symbol(v.substring(0, end));
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
	public Symbol getValue()
	{
		return (this._value);
	}
	
	// top rewriting rule
	@Override
	public String toString()
	{
		StringBuffer	s;

		s = new StringBuffer(this._value.toString());
		if (!this._children.isEmpty())
			s.append(" ->");
		for (TreeNode n : this._children)
		{
			s.append(' ');
			s.append(n.getValue().toString());
		}
		return (s.toString());
	}
	
	// list of rewriting rules
	/**
	 * Dumps the entire tree into a String by recursively calling its toString
	 * method and stops on encountering a leaf.
	 * @return A partial collection of rewriting rules.
	 */
	public String dump()
	{
		StringBuffer	s;

		s = new StringBuffer();
		if (!this._children.isEmpty())
		{
			s.append(this.toString() + "\n");
			for (TreeNode n : this._children)
				s.append(n.dump());
		}
		return (s.toString());
	}
	
	public String toBracketed()
	{
		StringBuffer	s;
		
		if (!(this._children.size() > 0)) {
			return (this._value.toString());
		}
		s = new StringBuffer("(");
		s.append(this._value);
		for (int i = 0; i < this._children.size(); i++) {
			s.append(" "); 
			s.append(this._children.get(i).toBracketed());
		}
		s.append(")");
		
		return (s.toString());
	}

	
	public String[] toWordTab()
	{
		String				res[];
		StringBuffer		s;
		
		res = new String[2];
		res[0] = this._value.toString();
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
	
	/**
	 * copy the root node of this tree (without its children)
	 * @return: a tree that consists of only one node which is identical to the root of this tree
	 */
	protected TreeNode copy_root() {
		TreeNode new_node = new TreeNode(this._value);
		return new_node;
	}
	
	/**
	 * Copy selected nodes from one tree to another
	 * preserving the hierarchy relationships between the selected nodes
	 * @param to_be_deleted
	 * @param tree_source : the tree from which we copy the selected nodes
	 * @param new_root : the tree onto which we hook up copies of nodes of another tree
	 */
	private static void selective_subtree_copy(IsAlien to_be_deleted, TreeNode tree_source, TreeNode new_root) {
		if (!(to_be_deleted.filter(tree_source))) {
			TreeNode new_child = tree_source.copy_root();
			new_root._children.add(new_child);
			new_root = new_child;
		}
			// hook the children of this subtree directly onto the current node
			for (TreeNode subtree : tree_source._children) {
				selective_subtree_copy(to_be_deleted, subtree, new_root);
			}
	}
	/**
	 * Used for back transformation of a parse-tree from CNF form to the original form
	 * Create a new tree by deleting a certain type of nodes from the tree supplied in the parameter list
	 * @param to_be_deleted: a class that knows to distinguish between the nodes to be deleted and the nodes to be kept in the tree
	 * @param tree: the tree to be transformed
	 * @return: a new (transformed) tree 
	 */
	public static TreeNode eliminate_nodes(IsAlien to_be_deleted, TreeNode source_tree) {
		// create the top node onto which all the nodes of the source_tree are to be hooked 
		TreeNode new_tree = new TreeNode(new Symbol("Root0"));
		// we use this node and selectively hook the subtrees of the source_tree on it
		selective_subtree_copy(to_be_deleted, source_tree, new_tree);
		// we know that the root node of the source tree is never deleted
		return new_tree._children.getFirst();
	}
}
