package main;

import java.util.Collections;
import java.util.LinkedList;
import java.util.HashMap;

/**
 * A reversed grammar.
 * @author Antoine LAFOUASSE
 */
public class ReverseGrammar extends Grammar0 {
	private HashMap<RHS, LinkedList<RewrRuleProb>> _map;
	
	/**
	 * initialize the container that actually stores the grammar
	 */
	protected void init() {
		this._map = new HashMap<RHS, LinkedList<RewrRuleProb>>();
	}
	
	/**
	 * create a new grammar with the given axiom
	 * @param axiom: the axiom of the grammar
	 */
	public ReverseGrammar(Symbol axiom) {
		super(axiom);
		this.init();
	}
	
	/**
	 * add a new rewriting rule to the grammar
	 * @param r: the rewriting rule to add to the grammar
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
			// fait en sorte que les productions sont toujours ordonnees 
			// par ordre decroissant de probabilites
			Collections.sort(this._map.get(rhs),
					Collections.reverseOrder());
		}
		else
			throw new RuntimeException("Duplicate rules in the grammar");
				
	}
	
	/**
	 * create a new rewriting rule from a string and add it to the grammar
	 * @param new_rul: the rewriting rule to add to the grammar
	 */
	public void addRule(String new_rule)
	{
		addRule(new RewrRuleProb(new_rule));
	}

	/** 
	 * get all rules with a given right-hand side
	 * @param rhs right-hand side of the rule
	 * @return all the rules with that RHS
	 */
	public LinkedList<RewrRuleProb> getRules(RHS rhs)
	{
		if(this._map.containsKey(rhs)) {
			return this._map.get(rhs);
		}
		else return new LinkedList<RewrRuleProb>();
		
	}
	
	/**
	 * gives all final (lexical) non-terminals.
	 * @return a list of all non-terminals that can generate a terminal
	 */
	public LinkedList<Symbol> getLexicalNonterms() {
		
		LinkedList<Symbol> lexNonterms = new LinkedList<Symbol>();
		for (RHS rhs : this._map.keySet()) {
			if(rhs.get(0).IsTerminal()) {
				LinkedList<RewrRuleProb> rules = this.getRules(rhs);
				for (RewrRuleProb rule : rules) {
					Symbol lhs = rule.getLHS();
					if(!lexNonterms.contains(lhs))
						lexNonterms.add(lhs);
				}
			}
		}
		return lexNonterms;
	}
	
	/**
	 * add equiprobable productions for unknown words.
	 * @return a list of all possible lexical productions for an unknown word.
	 */
	public LinkedList<RewrRuleProb> addUnknown() {
		LinkedList<Symbol> lexicalNonterms = this.getLexicalNonterms();
		double prob = 1.0 / (double) lexicalNonterms.size();
		RHS unk = new RHS("UNK");
		LinkedList<RewrRuleProb> unknownProds = new LinkedList<RewrRuleProb>();
		for (Symbol lexNonterm : lexicalNonterms) {
			RewrRuleProb r = new RewrRuleProb(lexNonterm, unk, prob);
			unknownProds.add(r);
		}
		
		return unknownProds;
	}

}
