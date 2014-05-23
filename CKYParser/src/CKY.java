
import java.util.LinkedList;

public class CKY {
	
	/**
	 * la chart
	 */
	private Cell[][] chart = null;
	/**
	 * the size of the phrase being parsed 
	 * which is equal to the dimensions of the chart
	 */
	private int n = -1;
	
	/**
	 * la grammaire
	 */
	private final ReverseGrammar G;

	public CKY(ReverseGrammar G) {
		this.G = G;
	}
	
	private void init_chart(LinkedList<Symbol> phrase) {
		this.n = phrase.size();
		chart = new Cell[n][n];		
		for (int i = 0; i < phrase.size(); i++) {
			for (int j = 0; j < phrase.size(); j++) {
				chart[i][j] = new Cell();
			}
		}
		// add the "terminal rules"
		for (int i = 0; i < phrase.size(); i++) {
			Symbol word = phrase.get(i);
			LinkedList<RewrRuleProb> rules;
			// TODO
		}
	}
	
	/**
	 * 
	 * @param phrase: the phrase to be parsed
	 * @param k: the number of parse trees to return
	 * @return k best parse trees of the phrase or null if no parse trees were found 
	 */
	public LinkedList<Tree> parse(LinkedList<Symbol> phrase, int k) {
		LinkedList<Tree> res = new LinkedList<Tree>();
		this.init_chart(phrase);
		
		// ALGO
		
		if (!(res.size() > 0)) {
			res = null;
		}
		return res;
	}

}
