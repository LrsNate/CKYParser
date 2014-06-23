package main;

/**
 * 
 * Used to filter out the new non-terminals created during the conversion to CNF form.
 * By convention we choose a specific prefix for all the labels of the new non-terminals 
 * created during this conversion. This class stores this prefix 
 * and is then capable of detecting these non-terminals later on.
 *
 */
public class IsAlien implements Filter<TreeNode> {
	
	/**
	 * The prefix of the labels of nodes to be deleted.
	 */
	private String prefix;
	
	/**
	 * Default constructor.
	 * @param prefix The prefix of the labels of nodes to be deleted.
	 */
	public IsAlien(String prefix) {
		this.prefix = prefix;
	}
	
	/**
	 * Determine if the node is to stay or to be deleted.
	 * @param t_node A node of the parse tree.
	 * @return True if the node is to be dispensed of.
	 */
	public boolean filter(TreeNode t_node) {
		return t_node.getValue().toString().startsWith(prefix);
	}

}
