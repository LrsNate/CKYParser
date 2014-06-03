
import java.util.LinkedList;
import java.util.Set;

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

	public CKY(ReverseGrammar G) {
		this.G = G;
	}
	
	private void init_chart(LinkedList<Symbol> phrase) {
		this.n = phrase.size();
		chart = new Cell[n][n];		
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				chart[i][j] = new Cell();
			}
		}
		// add the "terminal rules"
		for (int i = 0; i < n; i++) {
			Symbol word = phrase.get(i);
			LinkedList<RewrRuleProb> rules = G.getRules(new RHS(word));
			for (RewrRuleProb rule : rules) {			
				chart[i][i].add(new Tree(rule));
			}
			this.handleSingleProds(i,i);
		}
	}
	
	/**
	 * Parsing algorithm
	 * @param phrase: the phrase to be parsed
	 * @param k: the number of parse trees to return
	 * @return k best parse trees of the phrase or null if no parse trees were found 
	 */
	public LinkedList<Tree> parse(LinkedList<Symbol> phrase, int k) {
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
										Tree t = new Tree(lhs, tree1.getProb() * tree2.getProb() * prob, tree1, tree2);
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
							Tree t = new Tree(lhs, tree.getProb() * prob, tree);
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
