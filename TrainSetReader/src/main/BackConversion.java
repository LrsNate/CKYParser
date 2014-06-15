package main;

import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class BackConversion {
	
	private Scanner sc;

	public BackConversion(String input_file) throws FileNotFoundException {
		sc = new Scanner(new File(input_file));
	}
	
	/**
	 * Each line of the input file encodes a tree
	 * Delete all the nodes the labels of which start with the given prefixe,
	 * print the resulting new tree to standard output
	 * @param prefixe
	 * @param mode_probabilistic
	 */
	public void convert(String prefixe, boolean mode_probabilistic) {
		IsAlien to_be_deleted = new IsAlien(prefixe);	
		while (sc.hasNext()) {
			String res = "";
			String str_new_tree = sc.next();
			if (mode_probabilistic) {
				String arr[] = str_new_tree.split(" ", 2);
				String probability = arr[0];
				str_new_tree = arr[1];
				res = probability + " ";
			}
			TreeNode input_tree = new TreeNode(str_new_tree);
			TreeNode output_tree = TreeNode.eliminate_nodes(to_be_deleted, input_tree);
			res = res + output_tree.toBracketed();
			System.out.println(res);
		}
		sc.close();
	}

}
