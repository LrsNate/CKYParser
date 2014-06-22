package main;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Set;

/**
 * CKY parser. Parses a given phrase basing on a PCFG. Gives k best parses. 
 * @author kira
 *
 */
public class CKY {
	
	/**
	 * the chart
	 */
	private Cell[][] chart = null;
	/**
	 * the size of the phrase being parsed 
	 * which is equal to the dimensions of the chart
	 */
	private int n = -1;
	
	/**
	 * the grammar
	 */
	private final ReverseGrammar G;
	
	// I made it an array instead of having just one field for alpha
	// in order to leave the possibility of assigning different probs
	// to different categories...
	/**
	 * the apriori probability of getting an unknown word in a category
	 * key: category CAT
	 * value: probability of producing an unknown word = P(UNKNOWN | CAT)
	 */	
	private HashMap<Symbol, Double>  _unknown_probs;
	
	/**
	 * the unique RHS representing all the unknown words
	 */
	private RHS _unknown_rhs;
	
	/**
	 * the method of dealing with unknown words
	 */
	private final DealWithUnknown deal_with_unknown; 
	
	/**
	 * set to true if log-probabilities are being used to compute the score of a parse tree
	 */
	private boolean _log_mode = false;

	public CKY(ReverseGrammar G) {
		this.G = G;
		// set the mode of dealing with unknown words
		// if an unknown terminal occurs in the corpus, then the probability of producing it is taken to be zero
		deal_with_unknown = DealWithUnknown.IGNORE;
	}
	
	/**
	 * 
	 * @param G: the grammar
	 * @param log_mode: set to true if log-probabilities are to be used to compute the score of a parse tree
	 */
	public CKY(ReverseGrammar G, boolean log_mode) {
		this(G);
		this._log_mode = log_mode;
	}
	
	/**
	 * The constructor 
	 * that corresponds to the mode of deailing with unknown words = APRIORI_PROB
	 * @param G: the grammar (with lexical rules for the observed words only)
	 * @param alpha = P(UNKNOWN | CAT) (the same for every morphosyntactic category CAT)
	 */
	public CKY(ReverseGrammar G, double alpha) {
		this.G = G;
		deal_with_unknown = DealWithUnknown.APRIORI_PROB;
		this._unknown_probs = new HashMap<Symbol, Double>();
		// modify the existing lexical rules 
		// and return the list of all the non-terminals that may produce terminals
		LinkedList<Symbol> lexicalNonterms = G.modifyLexicalRules(alpha);
		for (Symbol lexNonterm : lexicalNonterms) {
			_unknown_probs.put(lexNonterm, alpha);
		}
	}
	
	/**
	 * The constructor 
	 * that corresponds to the mode of deailing with unknown words = APRIORI_PROB
	 * @param G: the grammar (with lexical rules for the observed words only)
	 * @param alpha = P(UNKNOWN | CAT) (the same for every morphosyntactic category CAT)
	 * @param log_mode: set to true if log-probabilities are to be used to compute the score of a parse tree
	 */
	public CKY(ReverseGrammar G, double alpha, boolean log_mode) {
		this(G, alpha);
		this._log_mode = log_mode;
	}
	
	/**
	 * The constructor 
	 * that corresponds to the mode of deailing with unknown words = RARE
	 * In this case the probabilities of unknown words
	 * are supposed to be already present in the grammar passed in the parameter.
	 * @param G: the grammar (with lexical rules for the observed words only)
	 * @param unknown_word: the unique terminal symbol that represents all the "unknown" words
	 */
	public CKY(ReverseGrammar G, Symbol unknown_word) {
		this.G = G;
		deal_with_unknown = DealWithUnknown.RARE;
		this._unknown_rhs = new RHS(unknown_word);
		LinkedList<RewrRuleProb> rules = G.getRules(this._unknown_rhs);
		if(rules.isEmpty()) {
			throw new IllegalArgumentException("MODE = RARE. The grammar does not contain productions with UNKNOWN words in their right hand side.");
		}
	}
	
	/**
	 * The constructor 
	 * that corresponds to the mode of deailing with unknown words = RARE
	 * In this case the probabilities of unknown words
	 * are supposed to be already present in the grammar passed in the parameter.
	 * @param G: the grammar (with lexical rules for the observed words only)
	 * @param unknown_word: the unique terminal symbol that represents all the "unknown" words
	 * @param log_mode: set to true if log-probabilities are to be used to compute the score of a parse tree
	 */
	public CKY(ReverseGrammar G, Symbol unknown_word, boolean log_mode) {
		this(G, unknown_word);
		this._log_mode = log_mode;
	}
	
	/**
	 * 
	 * @param probRule
	 * @param probLeftTree
	 * @param probRightTree
	 * @return
	 */
	private double calculateProbability(double probRule, double probLeftTree, double probRightTree) {
		if (this._log_mode) {
			return (Math.log(probRule) + probLeftTree + probRightTree);
		}
		return (probRule * probLeftTree * probRightTree);
	}
	
	/**
	 * 
	 * @param probRule
	 * @param probUniqueDescendant
	 * @return
	 */
	private double calculateProbability(double probRule, double probUniqueDescendant) {
		double probRightTree;
		if (this._log_mode) {
			 probRightTree = 0;
		} else {
			probRightTree = 1;
		}
		return calculateProbability(probRule, probUniqueDescendant, probRightTree);
	}
	
	
	/**
	 * Initialisation of the chart (the insertion of lexical rules + potentially some single productions)
	 * @param phrase: the phrase to be parsed
	 * @throws UnknownWordException 
	 */
	private void init_chart(LinkedList<Symbol> phrase) throws UnknownWordException {
		this.n = phrase.size();
		chart = new Cell[n][n];		
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				chart[i][j] = new Cell();
			}
		}
		// add the "terminal rules", works in both cases if terminal nodes are lexical or not
		for (int i = 0; i < n; i++) {
			Symbol word = phrase.get(i);
			LinkedList<RewrRuleProb> rules = G.getRules(new RHS(word));
			for (RewrRuleProb rule : rules) {	
				RewrRuleProb rule_to_insert = rule;
				if (this._log_mode) { 
					rule_to_insert = new RewrRuleProb(rule); 
					rule_to_insert.probTakeLog();
				}
				chart[i][i].add(new Tree(rule_to_insert));
				//System.out.println(chart[i][i]);
			}
			if(rules.isEmpty()) { // unknown word!
				if (this.deal_with_unknown.equals(DealWithUnknown.IGNORE)) {
					//the probability of producing an unknown word is taken to be zero
					//if an unknown word is found, the whole phrase probability will be 0, so the phrase cannot be parsed
					throw new UnknownWordException("In the IGNORE mode, we cannot parse phrases with unknown words.");
				}
				
				if (this.deal_with_unknown.equals(DealWithUnknown.APRIORI_PROB)) {
					// for every category that is capable of producing a terminal symbol
					// add the corresponding production rule
					for (Symbol lexNonterm : this._unknown_probs.keySet()) {
						RewrRuleProb new_rule = new RewrRuleProb(lexNonterm, new RHS(word), this._unknown_probs.get(lexNonterm));
						if (this._log_mode) { new_rule.probTakeLog(); }
						chart[i][i].add(new Tree(new_rule));
					}
				}
				if (this.deal_with_unknown.equals(DealWithUnknown.RARE)) {
					LinkedList<RewrRuleProb> rules_for_unknown = G.getRules(this._unknown_rhs);
					for (RewrRuleProb rule : rules_for_unknown) {	
						RewrRuleProb new_rule = new RewrRuleProb(rule.getLHS(), new RHS(word), rule.getProbability());
						if (this._log_mode) { new_rule.probTakeLog(); }
						chart[i][i].add(new Tree(new_rule));
					}
				}
				
			}
			this.handleSingleProds(i,i);
		}
	}
	
	/**
	 * Parsing algorithm
	 * @param phrase: the phrase to be parsed
	 * @param k: the number of parse trees to return
	 * @return k best parse trees of the phrase or null if no parse trees were found 
	 * @throws UnknownWordException 
	 */
	public LinkedList<Tree> parse(LinkedList<Symbol> phrase, int k) throws UnknownWordException {
		LinkedList<Tree> res = new LinkedList<Tree>();
		this.init_chart(phrase);
		
		int begin, end, split;
		
		for (int i = 1; i < n; i++) {
			for(int j = 0; j < (n-i); j++) {
				begin = j;
				end = j+i;
				Cell cell = chart[begin][end];
				
				for(split=begin; split < end; split++) {
					Cell cell1 = chart[begin][split];
					Cell cell2 = chart[split+1][end];
					
					for (Symbol smb1 : cell1.getSymbols()) {
						for (Symbol smb2 : cell2.getSymbols()) {
							LinkedList<Symbol> rhs = new LinkedList<Symbol>();
							rhs.add(smb1);
							rhs.add(smb2);
							LinkedList<RewrRuleProb> rules = G.getRules(new RHS(rhs));
							
							for (RewrRuleProb rule : rules) {
								double prob = rule.getProbability();
								Symbol lhs = rule.getLHS();

								for (Tree tree1 : cell1.getTrees(smb1)) {
									for (Tree tree2 : cell2.getTrees(smb2)) {
										double new_tree_prob = calculateProbability(prob, tree1.getProb(), tree2.getProb());
										Tree t = new Tree(lhs, new_tree_prob, tree1, tree2);
										boolean ruleExists = false;
										if (cell.getSymbols().contains(lhs)) {

											for (Tree existingT : cell.getTrees(lhs)) {
										
												if((t.equals(existingT))) {
													ruleExists = true;
					
												}
											}
										}
					
										
										if(!ruleExists) {
											cell.add(t);
										}
									}
								}
							}
							
						}
					}
				}
				this.handleSingleProds(begin,end);
				
			}
		}
		
		res = chart[0][n-1].getTrees(this.G.getAxiom());
		
		if (res == null) { return null; }
		
		if (!(res.size() > 0)) {
			res = null;
			return res;
		}
		
		if (res.size() < k) {
			return res;
		}
		
		LinkedList<Tree> kBest = new LinkedList<Tree>();
		for (int best = 0; best < k; best++) {
			kBest.add(res.pop());
		}
		return kBest;
	}
	
	/**
	 * Auxiliary function for handling single productions
	 * @param i: row of the chart
	 * @param j: column of the chart
	 */
	
	public void handleSingleProds(int i, int j) {
		boolean added = true;
		while(added) {
			added = false;
			LinkedList<Tree> addedTrees = new LinkedList<Tree>();	
			Set<Symbol> symbols = chart[i][j].getSymbols();
			for(Symbol singleRhs : symbols) {
				LinkedList<RewrRuleProb> rules = G.getRules(new RHS(singleRhs));
				if(rules.size() > 0) {
					for (RewrRuleProb rule : rules) {
						double prob = rule.getProbability();
						Symbol lhs = rule.getLHS();
						for (Tree tree : chart[i][j].getTrees(singleRhs)) {
							double new_tree_prob = calculateProbability(prob, tree.getProb());
							Tree t = new Tree(lhs, new_tree_prob, tree);
							boolean ruleExists = false;
							if (chart[i][j].getSymbols().contains(lhs)) {

								for (Tree sP : chart[i][j].getTrees(lhs)) {
									if((t.equals(sP))) {
										
										ruleExists = true;
									}
								}
							}
							
							for (Tree singleP : addedTrees) {
								if((t.equals(singleP))) {
							
									ruleExists = true;
								}
							}
							
							if(!ruleExists) {
								addedTrees.add(t);
								added = true;
							}
					}
					
				}
			}
			
			
		}
		for (Tree addedT : addedTrees) {

			chart[i][j].add(addedT);
		}
	}
	}

}
