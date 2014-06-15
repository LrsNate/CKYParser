package main;

/**
 * 
 * Used to filter out the new non-terminals created during the conversion to CNF form.
 * By convention we choose a specific prefix for all the labels of the new non-terminals 
 * created during this conversion. This class stores this prefix 
 * and is then capable of detecting these non-terminals later on.
 * @author yuliya
 *
 */
public class IsAlien implements Filter<TreeNode> {
	
	/**
	 * the prefixe of the labels of nodes to be deleted
	 */
	private String prefix;
	
	public IsAlien(String prefix) {
		this.prefix = prefix;
	}
	
	/**
	 * determine if the node is to stay or to be deleted
	 * @param t_node: a node of the parse tree 
	 * @return: true if the node is to be dispensed of
	 */
	public boolean filter(TreeNode t_node) {
		return t_node.getValue().startsWith(prefix);
	}

}
