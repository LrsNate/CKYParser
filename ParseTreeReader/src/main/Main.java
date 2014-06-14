package main;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * The program's entry point.
 * @author Antoine LAFOUASSE
 *
 */
public class Main
{
	public static void main(String[] argv)
	{
		ArgumentParser	ap;
		BufferedReader	br;
		CorpusReader	cr;
		String			s;
		Grammar			g;
		Timer			t;

		t = new Timer();
		g = new Grammar();
		ap = new ArgumentParser(argv);
		while ((br = ap.getNextFile()) != null)
		{
			Messages.info(t.lap());
			cr = new CorpusReader(br);
			try
			{
				while ((s = cr.getNextRule()) != null)
					g.addRule(s);
			}
			catch (IOException e)
			{
				Messages.error(e.getMessage());
			}
		}
		Messages.info(t.lap());
		g.display(ap.getPrecision());
		Messages.info(t.lap());
	}
}
