package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Abstract class representing a context-free grammar.
 * 
 */
public abstract class Grammar0 {
	/**
	 * The axiom of the grammar.
	 */
	protected final Symbol axiom;
	
	/**
	 * Default constructor.
	 * @param axiom The axiom of the grammar.
	 */
	public Grammar0(Symbol axiom) {
		this.axiom = axiom;
	}
	
	/**
	 * Getter for the axiom field.
	 * @return The symbol that is the axiom of the grammar.
	 */
	public Symbol getAxiom() {
		return axiom;
	}
	
	/**
	 * Set to true if the given symbol is the axiom of the grammar.
	 * @param smb A symbol.
	 * @return True if the symbol is the axiom of the grammar.
	 */
	public boolean isAxiom(Symbol smb) {
		return axiom.equals(smb);
	}
	
	/**
	 * Initialize the container that actually stores the grammar.
	 */
	protected abstract void init();

	/**
	 * Add a new production rule to the grammar.
	 * @param new_rule The production rule (in String format) to be added to the grammar.
	 */
	protected abstract void addRule(String new_rule);
	
	/**
	 * Load a grammar from a file.
	 * @param grammar_file The file containing a CFG. The first line contains the name of the axiom.
	 * For all the other lines in the file, each line contains exactly one rewriting rule.
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
		// initialize the container that stores the grammar
		this.init();
		this.LoadRules(sc);
		sc.close();
	}
	
	/**
	 * Load additional rules into the grammar
	 * (this might be desirable for the purposes of 
	 * loading lexical and non-lexical rules separately (from potentially different sources)).
	 * @param sc The scanner reading the input that contains the list of rules to be added to the grammar
	 * (one rewriting rule per line).
	 */
	public void LoadRules(Scanner sc) throws InvalidGrammarFormatException {
		while (sc.hasNext()) {
			String str_rule = sc.nextLine().trim();
			addRule(str_rule);
		}		
	}


}
