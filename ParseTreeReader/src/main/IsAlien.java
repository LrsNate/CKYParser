package main;

/**
 * 
 * Used to filter out the new non-terminals created during the conversion to CNF form.
 * By convention we choose a specific prefixe for all the labels of the new non-terminals 
 * created during this conversion. This class stores this prefixe 
 * and is then capable of detecting these non-terminals later on.
 * @author yuliya
 *
 */
public class IsAlien implements Filter<TreeNode> {
	
	/**
	 * the prefixe of the labels of nodes to be deleted
	 * et encore de n'importe quoi pour un commit
	 */
	private String prefixe;
	
	public IsAlien(String prefixe) {
		this.prefixe = prefixe;
	}
	
	/**
	 * determine if the node is to stay or to be deleted
	 * @return: true if the node is to be dispensed of
	 */
	public boolean filter(TreeNode t_node) {
		return t_node.getValue().startsWith(prefixe);
	}

}
