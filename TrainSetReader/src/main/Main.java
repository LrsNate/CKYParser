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

		t = new Timer();
		ap = new ArgumentParser(argv);
		while ((br = ap.getNextFile()) != null)
			CorpusReader.read(br);
		ThreadPool.terminate();
		Messages.info(t.lap());
		Grammar.getInstance().display(Environment.getPrecision());
		Messages.info(t.lap());
	}
}
