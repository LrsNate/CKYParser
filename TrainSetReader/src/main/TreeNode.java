package main;

import java.util.LinkedList;

/**
 * The representation of a syntactic tree.
 *
 */
public class TreeNode
{
	private final Symbol					_value;
	private final LinkedList<TreeNode>		_children;
	
	private static String			_errorMessage =
			"could not be resolved to a bracketed sentence.";
	
	/**
	 * Create a tree consisting of the root only,
	 * with empty string as the label of the root.
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
		else if (!v.matches("^\\S+( *\\(.+\\))+$"))
		{
			tab = v.split(" ");
			this._value = new Symbol(tab[0]);
			this._children = new LinkedList<TreeNode>();
			if (Environment.hasLexical())
				this._children.addLast(new TreeNode(new Symbol(tab[1], true))); 
			else
				this._children.addLast(new TreeNode(new Symbol(tab[0], true))); 
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
	
	/**
	 * Convert the tree and all of its subtrees to lists of rewriting rules.
	 * @return A list of lists of strings. Every element of the list represents one tree in the form of list of a rewriting rule.
	 */
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
	 * Takes a phrase and strips the leading backslashes off the terminals.
	 * @param lst The phrase to be stripped.
	 * @return The phrase without backslashes.
	 */
	private String strippedPhrase(LinkedList<String> lst) {
		StringBuffer		res;
		res = new StringBuffer();
		for (String s : lst)
		{
			if (res.toString() != "")
				res.append(" ");
			res.append(s.trim().replace("\\", ""));
		}
		return (res.toString());
	}
	
	/**
	 * Get the phrase represented by the tree.
	 * @return The tagged phrase. The words are of the form "word/tag".
	 */
	public String getTaggedPhrase()
	{
		LinkedList<String>	lst;
		String				tmp;
		
		lst = new LinkedList<String>();
		for (TreeNode c : this._children)
		{
			if (c.getValue().IsTerminal()) {
				lst.add(c.getValue().toString() + "/" + this._value.toString());
			}
			if ((tmp = c.getTaggedPhrase()) != "")
				lst.add(tmp);
		}
		return strippedPhrase(lst);
	}
	
	/**
	 * Get the phrase represented by the tree, without the lexical terminals.
	 * @return The phrase consisting only of grammatical category tags.
	 */
	public String getTaggingOnly()
	{
		LinkedList<String>	lst;
		String				tmp;
		
		lst = new LinkedList<String>();
		for (TreeNode c : this._children)
		{
			if (c.getValue().IsTerminal()) {
				lst.add(this._value.toString());
			}
			if ((tmp = c.getTaggingOnly()) != "")
				lst.add(tmp);
		}
		String res = strippedPhrase(lst);
		return res;
	}
	
	/**
	 * Get the phrase represented by the tree, without the grammatical category tags.
	 * @return The phrase without tagging.
	 */
	public String getBarePhrase()
	{
		LinkedList<String>	lst;
		String				tmp;
		
		lst = new LinkedList<String>();		
		if (this._value.IsTerminal()) {
			lst.add(this._value.toString());
		}
		for (TreeNode c : this._children)
		{
			if ((tmp = c.getBarePhrase()) != "")
				lst.add(tmp);
		}
		return strippedPhrase(lst);
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
	
	/**
	 * Convert the tree to the bracketed form.
	 * @return A string representing the tree.
	 */
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

	/**
	 * Convert the tree to a form of a rewriting rule with the root as the LHS of the rule
	 * and the children as the RHS.
	 * @return A couple of strings. The first element of the couple is the LHS, and the second is the RHS.
	 */
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
	
	/**
	 * Get the next child of the tree from a given string.
	 * @param s The bracketed string representing a tree.
	 * @return The next branch of the tree.
	 */
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
	
	/**
	 * Get the substring of a given string starting with the next child of the tree.
	 * @param s The bracketed string representing a tree.
	 * @return The given string starting with the next branch of the tree.
	 */
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
	 * Copy the root node of this tree (without its children).
	 * @return A tree that consists of only one node which is identical to the root of this tree.
	 */
	protected TreeNode copy_root() {
		TreeNode new_node = new TreeNode(this._value);
		return new_node;
	}
	
	/**
	 * Copy selected nodes from one tree to another
	 * preserving the hierarchy relationships between the selected nodes.
	 * @param to_be_deleted Filter to delete auxiliary nodes with certain prefixes.
	 * @param tree_source The tree from which we copy the selected nodes.
	 * @param new_root The tree onto which we hook up copies of nodes of another tree.
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
	 * Used for back transformation of a parse-tree from CNF form to the original form.
	 * Create a new tree by deleting a certain type of nodes from the tree supplied in the parameter list.
	 * @param to_be_deleted A class that knows to distinguish between the nodes to be deleted and the nodes to be kept in the tree.
	 * @param source_tree The tree to be transformed.
	 * @return A new (transformed) tree. 
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
