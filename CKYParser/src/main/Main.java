package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.LinkedList;
import java.io.PrintWriter;

public final class Main
{

	public static void main(String[] argv)
	{
		CKYArgumentParser	ap;
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
		
		BufferedReader stdin = ArgumentParser0.openStandardInput();
		PrintWriter out = ap.getOutputFile();
		String line;
		int line_number = 0;
		LinkedList<Tree> k_best_parses;
			while ((line = stdin.readLine()) != null) {
				try {
					line_number++;
					out.println("**" + line_number + "**  " + line);
					k_best_parses = parser.parse(Symbol.ListSymbols(line.split(" ")), k_best);
					boolean print_probas = false;
					if(k_best > 1) {
						print_probas = true;
					}
					for (int i = 0; i < k_best_parses.size(); i++) {
						out.println(k_best_parses.get(i).treeToString(print_probas));						
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
