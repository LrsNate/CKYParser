package main;

import java.io.BufferedReader;

public final class Main
{

	public static void main(String[] argv)
	{
		CKYArgumentParser	ap;
		BufferedReader	br;
		CKY parser;
		
		ap = new CKYArgumentParser(argv);
		Symbol axiom = new Symbol("S");
		ReverseGrammar G = new ReverseGrammar(axiom);
		G.readGrammar(ap.getGrammarBufferedReader());
		
		switch {
		
		}
		
		while ((br = ap.getNextFile()) != null)
			r.read(br);
		ThreadPool.terminate();
		if (g instanceof GrammarCountUnknown)
			((GrammarCountUnknown)g).recomputeLexicalCounts();
		Messages.info(t.lap());
		g.display(Environment.getPrecision());
		
	}

}
