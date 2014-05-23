
import java.util.LinkedList;
import java.util.HashMap;

/**
 * une case de la chart de CKY
 * @author yuliya
 *
 */
public class Cell {
	
	/**
	 * smb stores all the symbols this cell comtains,
	 * each symbol points to a list of trees it can produce
	 */
	private HashMap<Symbol, LinkedList<Tree>> smb;

	public Cell() {
		smb = new HashMap<Symbol, LinkedList<Tree>>();
	}
	
	public void add(Tree t) {
		Symbol root = t.getRoot();
		if (!smb.containsKey(root)) {
			smb.put(root, new LinkedList<Tree>());
		}
		LinkedList<Tree> trees = smb.get(root);
		trees.push(t);		
	}

}
