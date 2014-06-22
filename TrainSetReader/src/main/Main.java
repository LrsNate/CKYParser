package main;

import java.io.BufferedReader;

/**
 * The program's entry point.
 * @author Antoine LAFOUASSE
 *
 */
public final class Main
{
	public static final void main(String[] argv)
	{
		ArgumentParser	ap;
		BufferedReader	br;
		Timer			t;
		CorpusReader	r;
		Grammar			g;
		
		t = new Timer();
		ap = new ArgumentParser(argv);
		if (Environment.isAltMode())
			g = new GrammarCountUnknown(
					Environment.getUnknownLabel(),
					Environment.getUnknownThreshold()
			);
		else
			g = new Grammar();
		r = new CorpusReader(g);
		while ((br = ap.getNextFile()) != null)
			r.read(br);
		ThreadPool.terminate();
		if (g instanceof GrammarCountUnknown)
			((GrammarCountUnknown)g).recomputeLexicalCounts();
		Messages.info(t.lap());
		g.display(Environment.getPrecision());
		Messages.info(t.lap());
		
		Environment.closeOutput();
	}
}
