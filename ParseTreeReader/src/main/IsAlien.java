package main;

/**
 * @author yuliya
 *
 */
public class IsAlien implements Filter<TreeNode> {
	
	/**
	 * the prefixe of the labels of nodes to be deleted
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
