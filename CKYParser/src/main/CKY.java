package main;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Set;
import java.util.TreeSet;

/**
 * CKY parser. Parses a given phrase according to a PCFG. Gives k best parses. 
 * 
 */
public class CKY {
	
	/**
	 * The chart of the parser.
	 */
	private Cell[][] chart = null;
	/**
	 * The length of the phrase being parsed, 
	 * which is equal to the dimensions of the chart.
	 */
	private int n = -1;
	
	/**
	 * The probabilistic context-free grammar.
	 */
	private final ReverseGrammar G;
	
	/**
	 * The apriori probability of getting an unknown word in a category.
	 * There is the possibility of assigning different probabilities
	 * to different categories.
	 * Key: lexical category CAT
	 * Value: probability of producing an unknown word = P(UNKNOWN | CAT)
	 */	
	private HashMap<Symbol, Double>  _unknown_probs;
	
	/**
	 * The unique RHS representing all the unknown words (token "UNKNOWN").
	 */
	private RHS _unknown_rhs;
	
	/**
	 * The method of dealing with unknown words.
	 */
	private final DealWithUnknown deal_with_unknown; 
	
	/**
	 * Set to true if log-probabilities are being used to compute the score of a parse tree.
	 */
	private boolean _log_mode = false;
	/**
	 * the number of best parses to find
	 */
	private int 	_k = 1;

	/**
	 * Default constructor.
	 * @param G The PCFG used for parsing.
	 */
	public CKY(ReverseGrammar G) {
		this.G = G;
		// set the mode of dealing with unknown words to IGNORE
		// if an unknown terminal occurs in the corpus, then the probability of producing it is taken to be zero
		deal_with_unknown = DealWithUnknown.IGNORE;
	}
	
	/**
	 * The constructor that defines if log-probabilities will be used or not.
	 * @param G The grammar used for parsing.
	 * @param log_mode Set to true if log-probabilities are being used to compute the score of a parse tree.
	 */
	public CKY(ReverseGrammar G, boolean log_mode) {
		this(G);
		this._log_mode = log_mode;
	}
	
	/**
	 * The constructor 
	 * that corresponds to the mode of dealing with unknown words = APRIORI_PROB.
	 * @param G The grammar (with lexical rules for the observed words only).
	 * @param alpha = P(UNKNOWN | CAT) (the same for every morpho-syntactic category CAT)
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
	 * that corresponds to the mode of dealing with unknown words = APRIORI_PROB
	 * and defines if log-probabilities will be used or not.
	 * @param G The grammar (with lexical rules for the observed words only).
	 * @param alpha = P(UNKNOWN | CAT) (the same for every morpho-syntactic category CAT).
	 * @param log_mode Set to true if log-probabilities are being used to compute the score of a parse tree.
	 */
	public CKY(ReverseGrammar G, double alpha, boolean log_mode) {
		this(G, alpha);
		this._log_mode = log_mode;
	}
	
	/**
	 * The constructor 
	 * that corresponds to the mode of dealing with unknown words = RARE.
	 * In this case, the probabilities of unknown words
	 * are supposed to be already present in the grammar passed in the parameter.
	 * @param G The grammar (with lexical rules for the observed words only).
	 * @param unknown_word The unique terminal symbol that represents all the "unknown" words (token "UNKNOWN").
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
	 * that corresponds to the mode of dealing with unknown words = RARE.
	 * In this case, the probabilities of unknown words
	 * are supposed to be already present in the grammar passed in the parameter.
	 * @param G The grammar (with lexical rules for the observed words only).
	 * @param unknown_word The unique terminal symbol that represents all the "unknown" words (token "UNKNOWN").
	 * @param log_mode Set to true if log-probabilities are being used to compute the score of a parse tree.
	 */
	public CKY(ReverseGrammar G, Symbol unknown_word, boolean log_mode) {
		this(G, unknown_word);
		this._log_mode = log_mode;
	}
	
	/**
	 * Calculate the probability of a derivation tree from the probabilities of its right and left subtrees.
	 * @param probRule The probability of the rewriting rule corresponding to the tree.
	 * @param probLeftTree The probability of the left subtree.
	 * @param probRightTree The probability of the right subtree.
	 * @return The probability of the tree.
	 */
	private double calculateProbability(double probRule, double probLeftTree, double probRightTree) {
		if (this._log_mode) {
			return (Math.log(probRule) + probLeftTree + probRightTree);
		}
		return (probRule * probLeftTree * probRightTree);
	}
	
	/**
	 * Calculate the probability of a derivation tree from the probabilities of its unique subtree.
	 * @param probRule The probability of the rewriting rule corresponding to the tree.
	 * @param probUniqueDescendant The probability of the unique subtree.
	 * @return The probability of the tree.
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
	 * Initialization of the chart (the insertion of lexical rules + potentially some single productions).
	 * @param phrase The phrase to be parsed.
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
			}
			if(rules.isEmpty()) { // unknown word!
				if (this.deal_with_unknown.equals(DealWithUnknown.IGNORE)) {
					//the probability of producing an unknown word is taken to be zero
					//if an unknown word is found, the whole phrase probability will be 0, so the phrase cannot be parsed
					throw new UnknownWordException("In the IGNORE mode, we cannot parse phrases with unknown words.");
				}
				
				if (this.deal_with_unknown.equals(DealWithUnknown.APRIORI_PROB)) {
					// for every category that is capable of producing a terminal symbol
					// add the corresponding production rule CAT -> UNK
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
	 * Parsing algorithm.
	 * @param phrase The phrase to be parsed.
	 * @param k The number of parse trees to return.
	 * @return k best parse trees of the phrase or null if no parse trees were found.
	 * @throws UnknownWordException 
	 */
	public LinkedList<Tree> parse(LinkedList<Symbol> phrase, int k) throws UnknownWordException {
		this._k = k;
		LinkedList<Tree> res = new LinkedList<Tree>();
		this.init_chart(phrase);
		
		int begin, end, split;
		int i_beg = 1;
		if (phrase.size() == 1) { i_beg = 0; }
		for (int i = i_beg; i < n; i++) {
			for(int j = 0; j < (n-i); j++) {
				begin = j;
				end = j+i;
				Cell cell = chart[begin][end];
				
				for(split=begin; split < end; split++) {
					Cell cell1 = chart[begin][split];
					Cell cell2 = chart[split+1][end];
					
					//System.err.println("==== (" + begin + "," + split + "," + end + ") ====");
					//System.err.println(cell1.getSymbols().size() * cell2.getSymbols().size());
					
					for (Symbol smb1 : cell1.getSymbols()) {
						for (Symbol smb2 : cell2.getSymbols()) {
							LinkedList<Symbol> rhs = new LinkedList<Symbol>();
							rhs.add(smb1);
							rhs.add(smb2);
							LinkedList<RewrRuleProb> rules = G.getRules(new RHS(rhs));
							
							for (RewrRuleProb rule : rules) {
								double prob = rule.getProbability();
								Symbol lhs = rule.getLHS();
								
								LinkedList<Tree> cell1_trees_to_consider;
								LinkedList<Tree> cell2_trees_to_consider;
								if (this._k == 1) {
									// if only one best parse is to be returned
									// than choose only one best parse for each symbol
									cell1_trees_to_consider = new LinkedList<Tree>();
									cell1_trees_to_consider.add(cell1.getTrees(smb1).get(0));
									cell2_trees_to_consider = new LinkedList<Tree>();
									cell2_trees_to_consider.add(cell2.getTrees(smb2).get(0));
								} else {
									cell1_trees_to_consider = cell1.getTrees(smb1);
									cell2_trees_to_consider = cell2.getTrees(smb2);
								}
								

								for (Tree tree1 : cell1_trees_to_consider) {
									for (Tree tree2 : cell2_trees_to_consider) {
										double new_tree_prob = calculateProbability(prob, tree1.getProb(), tree2.getProb());
										Tree t = new Tree(lhs, new_tree_prob, tree1, tree2);
										if (!cell.getSymbols().contains(lhs)
												|| !cell.getTrees(lhs).contains(t))
											cell.add(t);
									}
								}
							}
							
						}
					}
				}
				//System.out.println("(" + begin + " " + end + ")" + " start handleSingleProds ");
				this.handleSingleProds(begin,end);
				//System.out.println("(" + begin + " " + end + ")" + " end handleSingleProds ");				
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
	 * Auxiliary function for handling single productions.
	 * @param i Row of the chart.
	 * @param j Column of the chart.
	 */
	
	public void handleSingleProds(int i, int j) {
		boolean added = true;
		TreeSet<Tree> addedTrees = new TreeSet<Tree>();
		Set<Symbol> symbols = chart[i][j].getSymbols();
		while(added)
		{
			added = false;
			for(Symbol singleRhs : symbols)
			{
				LinkedList<RewrRuleProb> rules = G.getRules(new RHS(singleRhs));
				for (RewrRuleProb rule : rules)
				{
					double prob = rule.getProbability();
					Symbol lhs = rule.getLHS();
					for (Tree tree : chart[i][j].getTrees(singleRhs))
					{
						double new_tree_prob = calculateProbability(prob, tree.getProb());
						Tree t = new Tree(lhs, new_tree_prob, tree);
						boolean ruleExists = false;
						if ((chart[i][j].getSymbols().contains(lhs)
								&& chart[i][j].getTrees(lhs).contains(t))
							|| addedTrees.contains(t))
							ruleExists = true;
						
						if(ruleExists == false) {
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
