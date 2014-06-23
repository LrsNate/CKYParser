package main;

import java.util.Collections;
import java.util.LinkedList;
import java.util.HashMap;
import java.util.Set;

/**
 * A cell of the CKY chart.
 * 
 */
public class Cell {
	
	/**
	 * A HashMap that stores all the left-hand-side symbols the cell contains,
	 * each symbol points to a list of trees it can produce (usually one).
	 */
	private HashMap<Symbol, LinkedList<Tree>> smb;

	/**
	 * Default constructor.
	 */
	public Cell() {
		this.smb = new HashMap<Symbol, LinkedList<Tree>>();
	}
	
	/**
	 * Get all the left-hand-side symbols (tree roots) that the cell contains.
	 * @return A Set of symbols the cell contains.
	 */
	public Set<Symbol> getSymbols() {
		return this.smb.keySet();
	}
	
	/**
	 * Add a derivation tree to the cell.
	 * @param t The tree to store in the cell.
	 */
	public void add(Tree t) {
		Symbol root = t.getRoot();
		if (!this.smb.containsKey(root)) {
			this.smb.put(root, new LinkedList<Tree>());
		}
		LinkedList<Tree> trees = this.smb.get(root);
		trees.push(t);		
		Collections.sort(trees, Collections.reverseOrder());
	}
	
	/**
	 * Get all the trees having a given symbol as root.
	 * @param s The left-hand-symbol being the tree root.
	 * @return All the trees the cell contains that have the given symbol as root.
	 */
	public LinkedList<Tree> getTrees(Symbol s) {
		return this.smb.get(s);
	}
	
	@Override
	public String toString() {
		String res = "";
		for (Symbol lhs : this.smb.keySet()) {
			for (Tree t : this.smb.get(lhs)) {
				res = res + " " + lhs + " : " + t.treeToString(false) + "\n";
			}
		}
		return res;
	}

}
