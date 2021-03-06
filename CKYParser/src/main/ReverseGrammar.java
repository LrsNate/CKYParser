package main;

import java.util.Collections;
import java.util.LinkedList;
import java.util.HashMap;
import java.io.BufferedReader;
import java.io.IOException;

/**
 * A reversed context-free grammar (grouping the rules by their RHS and not their LHS). 
 */
public class ReverseGrammar extends Grammar0 {
	/**
	 * The container that actually stores he grammar. 
	 * A map from a right-hand side of a rule to all the rewriting rules that have this right-hand side.
	 */
	private HashMap<RHS, LinkedList<RewrRuleProb>> _map;
	
	/**
	 * Initialize the container that actually stores the grammar.
	 */
	protected void init() {
		this._map = new HashMap<RHS, LinkedList<RewrRuleProb>>();
	}
	
	/**
	 * Create a new grammar with the given axiom.
	 * @param axiom The axiom of the grammar.
	 */
	public ReverseGrammar(Symbol axiom) {
		super(axiom);
		this.init();
	}
	
	/**
	 * Add a new rewriting rule to the grammar.
	 * @param r The rewriting rule to add to the grammar.
	 */
	public void addRule(RewrRuleProb r)
	{
		LinkedList<RewrRuleProb>	lst;
		RHS					rhs;

		rhs = r.getRHS();
		if (!this._map.containsKey(rhs))
		{
			lst = new LinkedList<RewrRuleProb>();
			lst.add(r);
			this._map.put(rhs, lst);
		}
		else if (!this._map.get(rhs).contains(r))
		{
			this._map.get(rhs).add(r);
			// the rules are always sorted by descending probability
			Collections.sort(this._map.get(rhs),
					Collections.reverseOrder());
		}
		else
			throw new RuntimeException("Duplicate rules in the grammar");
				
	}
	
	/**
	 * Create a new rewriting rule from a string and add it to the grammar.
	 * @param new_rule The rewriting rule to add to the grammar.
	 */
	public void addRule(String new_rule)
	{
		addRule(new RewrRuleProb(new_rule));
	}
	
	/**
	 * Reads the grammar from the input stream.
	 * Format of the input stream: one rewriting rule per line.
	 * @param fd The input stream that contains the grammar.
	 */
	public void readGrammar(BufferedReader fd) throws ReadingGrammarException {
			String		line;
			try {
			while ((line = fd.readLine()) != null) {
				if (!line.isEmpty()) { this.addRule(line); }
			}
			} catch (IOException e) {
				throw new ReadingGrammarException("Error while reading the grammar.");
			}
	}

	/** 
	 * Get all rules with a given right-hand side.
	 * @param rhs Right-hand side of a rule.
	 * @return All the rules with that RHS.
	 */
	public LinkedList<RewrRuleProb> getRules(RHS rhs)
	{
		if(this._map.containsKey(rhs)) {
			return this._map.get(rhs);
		}
		else return new LinkedList<RewrRuleProb>();
		
	}
	
	/**
	 * 1. Modifies the probabilities of all the productions of the form 
	 * ( Non-terminal symbol -> Terminal symbol )
	 * by multiplying their probability by (1-alpha).
	 * 2. Gives all final (lexical) non-terminals.
	 * @param alpha = P(UNKNOWN | CAT) for every CAT that is capable of producing a lexical (known) symbol.
	 * @return A list of all non-terminals that can generate a terminal.
	 */
	public LinkedList<Symbol> modifyLexicalRules(double alpha) {
		LinkedList<Symbol> lexNonterms = new LinkedList<Symbol>();		
		for (RHS rhs : this._map.keySet()) {
			// NOTE: the grammar being in CNF, there cannot be more than one terminal in the RHS
			if(rhs.get(0).IsTerminal()) {				
				// get all the rules that have this terminal as their RHS
				LinkedList<RewrRuleProb> rules = this.getRules(rhs);
				// for every rule
				for (RewrRuleProb rule : rules) {
					// modify its probability by multiplying it by (1-alpha)
					rule.mult_prob(1-alpha);
					// add the category to the list of Symbols that may produce terminals
					Symbol lhs = rule.getLHS();
					if(!lexNonterms.contains(lhs)) {
						lexNonterms.add(lhs);
					}
				}
			}
		}
		return lexNonterms;
	}

}
