package main;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.LinkedList;
import java.util.Set;
import java.util.HashSet;

/**
 * A PCFG including rules for producing unknown words.
 *
 */
public class GrammarCountUnknown extends Grammar {
	
	/**
	 * The right-hand side of a rule,
	 * conventionally chosen to represent all unknown terminals.
	 */
	private RHS 	    _unknown_rhs;
	
	/**
	 * All the terminals that occur no more than _count_threshold times in the training corpus
	 * are marked as UNKNOWN
	 * (upon the call to the method recomputeLexicalCounts).
	 */
	private final int 	_count_threshold;
	
	/**
	 * Counter of terminal symbol occurrences.
	 * key: terminal symbol
	 * value: number of occurrences in the training set
	 */
	private ConcurrentHashMap<Symbol, AtomicInteger> count_terminals = new ConcurrentHashMap<Symbol, AtomicInteger>();
	
	/**
	 * The set of (pointers to) the counters of lexical rules of the grammar.
	 * NOTE: it is actually a set of counters of all the rules the left hand side of which 
	 * is capable of producing a terminal
	 * (in our case, these rules actually always contain a terminal in the right-hand side).
	 */
	private Set<RewrRuleCounter> lexical_rules = new HashSet<RewrRuleCounter>();

	public GrammarCountUnknown(String unknown_label, int count_threshold) {
		this._unknown_rhs = new RHS(new Symbol(unknown_label, true));
		this._count_threshold = count_threshold;
	}

	
	public synchronized RewrRuleCounter addRule(String rule) {
		RewrRuleCounter rule_counter = super.addRule(rule);
		if (rule_counter.hasLexical()) {
			lexical_rules.add(rule_counter);
			for (RHS rhs : rule_counter.getRHS()) {
				if (rhs.size() == 1) {
					Symbol rhs_symbol = rhs.get(0);
					if (rhs_symbol.IsTerminal()) {
						addTerminal(rhs_symbol);
					}
				}
			}
		}
		return rule_counter;
	}
	
	public synchronized RewrRuleCounter addRule(String tab[]) {
		RewrRuleCounter rule_counter = super.addRule(tab);
		if (rule_counter.hasLexical()) {
			lexical_rules.add(rule_counter);
			for (RHS rhs : rule_counter.getRHS()) {
				if (rhs.size() == 1) {
					Symbol rhs_symbol = rhs.get(0);
					if (rhs_symbol.IsTerminal()) {
						addTerminal(rhs_symbol);
					}
				}
			}
		}
		return rule_counter;
	}
	
	/**
	 * Add a terminal to the counter of terminal occurrences.
	 * @param smb The terminal symbol to add.
	 */
	public void addTerminal(Symbol smb) {
		if (!(this.count_terminals.containsKey(smb))) {
			this.count_terminals.put(smb, new AtomicInteger(1));
		} else {
			this.count_terminals.get(smb).incrementAndGet();
		}
	}
	
	/**
	 * Replace rare words with the UNKNOWN word, modify the counts accordingly.
	 */
	public void recomputeLexicalCounts() {
		for (RewrRuleCounter lex_rule : this.lexical_rules) {
			LinkedList<RHS> to_be_redistributed = new LinkedList<RHS>();
			for (RHS rhs : lex_rule.getRHS()) {
				if (rhs.size() == 1) {
					System.out.println("We are in rare mode, threshold is " + this._count_threshold);
					Symbol smb = rhs.get(0);
					// if the right hand side happens to be a rare terminal 
					if ((smb.IsTerminal() && count_terminals.containsKey(smb)) && !(count_terminals.get(smb).get() > this._count_threshold)) {
						to_be_redistributed.add(rhs);
					}
				}
			}
			for(RHS rare_rhs : to_be_redistributed) {
				lex_rule.redistributeCounts(rare_rhs, this._unknown_rhs);
			}
		}
		
	}

}
