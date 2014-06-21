package main;

import java.io.BufferedReader;
import java.io.IOException;

public class MainAnnotationStripper
{

	public static void main(String[] argv)
	{
		ArgumentParser		ap;
		BufferedReader		br;
		String				line;
		
		ap = new ArgumentParser(argv);
		Environment.setLexical(true);
		while ((br = ap.getNextFile()) != null)
		{
			try
			{
				while ((line = br.readLine()) != null)
				{
					Environment.getOutputStream().println(new TreeNode(line.substring(2, line.length() - 1)).getBarePhrase());
				}
			}
			catch (IOException e)
			{
				Messages.error(e.getMessage());
			}
		}
	}

}
