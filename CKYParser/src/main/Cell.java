package main;

import java.util.Collections;
import java.util.LinkedList;
import java.util.HashMap;
import java.util.Set;

/**
 * une case de la chart de CKY
 * @author yuliya
 *
 */
public class Cell {
	
	/**
	 * smb stores all the symbols this cell contains,
	 * each symbol points to a list of trees it can produce (usually one)
	 */
	private HashMap<Symbol, LinkedList<Tree>> smb;

	public Cell() {
		this.smb = new HashMap<Symbol, LinkedList<Tree>>();
	}
	
	public Set<Symbol> getSymbols() {
		return this.smb.keySet();
	}
	
	public void add(Tree t) {
		Symbol root = t.getRoot();
		if (!this.smb.containsKey(root)) {
			this.smb.put(root, new LinkedList<Tree>());
		}
		LinkedList<Tree> trees = this.smb.get(root);
		trees.push(t);		
		Collections.sort(trees, Collections.reverseOrder());
	}
	
	public LinkedList<Tree> getTrees(Symbol s) {
		return this.smb.get(s);
	}

}
