package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.LinkedList;


public final class Main
{

	public static void main(String[] argv)
	{
		CKYArgumentParser	ap;
		BufferedReader	br;
		CKY parser = null;
		try {
			
		ap = new CKYArgumentParser(argv);
		Symbol axiom = new Symbol("S");
		ReverseGrammar G = new ReverseGrammar(axiom);
		G.readGrammar(ap.getGrammarBufferedReader());
		
		boolean log_mode = ap.getLogMode();
		int k_best = ap.getKBest();
		
		switch (ap.getModeTreatUnknown()) {
		case IGNORE:
			parser = new CKY(G, log_mode);
		case APRIORI_PROB:
			parser = new CKY(G, ap.getAprioriUnknownProb(), log_mode);
		case RARE:
			parser = new CKY(G, new Symbol(ap.getUnknownLabel(), true), log_mode);		
		}
		
		BufferedReader stdin = ArgumentParser0. openStandardInput();
		String line;
		int line_number = 0;
		LinkedList<Tree> k_best_parses;
			while ((line = stdin.readLine()) != null) {
				try {
					line_number++;
					System.out.println("**" + line_number + "**  " + line);
					k_best_parses = parser.parse(Symbol.ListSymbols(line.split(" ")), k_best);
					// TODO: option: print probabilities or not 
					for (int i = 0; i < k_best_parses.size(); i++) {
						System.out.println(k_best_parses.get(i).treeToString(false));						
					}
				} catch (UnknownWordException e) {
					// no parses were obtained, nothing is printed 
					// just pass on to the next phrase
				}
			}
		} catch (Exception e) {
			Messages.error(e.getMessage());			
		}		
	}

}
