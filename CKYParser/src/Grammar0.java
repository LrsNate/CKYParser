import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;


public abstract class Grammar0 {
	/**
	 * the axiom
	 */
	protected final Symbol axiom;
	
	public Grammar0(Symbol axiom) {
		this.axiom = axiom;
	}
	
	/**
	 * 
	 * @return: the Symbol that is the axiom of the grammar
	 */
	public Symbol getAxiom() {
		return axiom;
	}
	
	/**
	 * 
	 * @param: smb  
	 * @return: true if the Symbol smb is the axiom of the grammar
	 */
	public boolean isAxiom(Symbol smb) {
		return axiom.equals(smb);
	}
	
	/**
	 * initialise the container that actually stores the grammar
	 */
	protected abstract void init();

	/**
	 * add a new production rule to the grammar
	 * @param new_rule: the production rule (in String format) to be added to the grammar
	 */
	protected abstract void addRule(String new_rule);
	
	/**
	 * Load a grammar from a file
	 * @param grammar_file: the first line contains the name of the axiom \
	 * for all the other lines in the file: \
	 * each line contains exactly one rewriting rule
	 * @throws InvalidGrammarFormatException
	 * @throws FileNotFoundException
	 */
	public Grammar0(String grammar_file) throws InvalidGrammarFormatException, FileNotFoundException {
		Scanner sc = new Scanner(new File(grammar_file));
		String first_line = "";
		if (sc.hasNext()) {
			first_line = sc.nextLine().trim();
			if (first_line.contains(" ")) {
				sc.close();
				throw new InvalidGrammarFormatException("The first line of the file with the grammar should contain the name of the axiom (with no white-spaces inside the name)");
			}
		}
		axiom = new Symbol(first_line, false);		
		// initialise the container that stores the grammar
		this.init();
		this.LoadRules(sc);
		sc.close();
	}
	
	/**
	 * load additional rules into the grammar
	 * (this might be desirable for the puposes of 
	 * loading lexical and non-lexical rules separately (from potentially different sources)) 
	 * @param file: the text-file that contains the list of rules to be added to the grammar \
	 * (one rewriting rule per line)
	 */
	public void LoadRules(Scanner sc) throws InvalidGrammarFormatException {
		while (sc.hasNext()) {
			String str_rule = sc.nextLine().trim();
			addRule(str_rule);
		}		
	}


}
