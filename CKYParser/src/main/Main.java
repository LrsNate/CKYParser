package main;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
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
			parser = new CKY(G, log_mode); break;
		case APRIORI_PROB:
			parser = new CKY(G, ap.getAprioriUnknownProb(), log_mode); break;
		case RARE:
			parser = new CKY(G, new Symbol(ap.getUnknownLabel(), true), log_mode); break;
		}
		
		BufferedReader stdin = ArgumentParser0.openStandardInput();
		//BufferedReader stdin = ArgumentParser0.openFile("D:\\Universite Paris-Diderot\\2ieme semestre\\Projet\\Results\\corpus_dev_bare\\corpus.tagging_only--test.txt");
		PrintWriter out = ap.getOutputFile();
		String line;
		int line_number = 0;
		LinkedList<Tree> k_best_parses;
		//try {
			//LinkedList<Tree> restt = parser.parse(Symbol.ListSymbols((" Affaire des caporaux de Souain").trim().split(" "), true), 5);
			//String tree = restt.getFirst().treeToString();
			//System.out.println(tree);
		//} catch (UnknownWordException e) {
			// no parses were obtained, nothing is printed 
			// just pass on to the next phrase
		//}
		//LinkedList<RewrRuleProb> rules = G.getRules(new RHS(new Symbol("Affaire", true)));
			while ((line = stdin.readLine()) != null) {
				try {
					if (!line.isEmpty()) { 
						line_number++;
						out.println("**" + line_number + "**  " + line);
						k_best_parses = parser.parse(Symbol.ListSymbols(line.trim().split(" "), ap.getInputIsLexical()), k_best);
						boolean print_probas = false;
						if(k_best > 1) {
							print_probas = true;
						}
						for (int i = 0; i < k_best_parses.size(); i++) {
							out.println(k_best_parses.get(i).treeToString(print_probas));						
						}
					}
				} catch (UnknownWordException e) {
					// no parses were obtained, nothing is printed 
					// just pass on to the next phrase
				}
			}
		} catch (MissingObligatoryArgException e) {
			Messages.error(e.getMessage());			
		}	catch (FileNotFoundException e) {
			Messages.error(e.getMessage());			
		}	catch (IOException e) {
			Messages.error(e.getMessage());			
		}	catch (MissingArgumentValueException e) {
			Messages.error(e.getMessage());			
		}
	}

}
