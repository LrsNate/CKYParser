package test;

import main.Environment;
import main.TreeNode;
import main.IsAlien;

public class EliminateNodesTest {

	public static void main(String[] args) {
		Environment.setLexical(true);
		String s1 = "(S (Z7 (Z6 (R r) (Z1 x)) (A (B (Z4 (C c))) (U u))))";
		//String s1 = "(S (Z7 (Z6 (R r) (Z1 (A a))) (Ziht8 (B (Z4 (C c))) (U u))))";
		TreeNode input = new TreeNode(s1);
		System.out.println("Phrase : " + s1);
		System.out.println("Bare phrase : " + input.getBarePhrase());
		System.out.println("Before transform:");
		System.out.println(input.dump());
		// define the set of nodes to be deleted
		// as the set of nodes for which the label starts with "Z"
		IsAlien is_new_nonterminal = new IsAlien("Z");
		TreeNode output = TreeNode.eliminate_nodes(is_new_nonterminal, input);
		System.out.println("After trasnform:");
		System.out.println(output.dump());
		//System.out.println(TreeNode.eliminate_nodes(is_new_nonterminal, output).dump());

	}

}
