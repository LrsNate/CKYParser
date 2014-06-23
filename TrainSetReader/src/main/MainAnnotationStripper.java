package main;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * For each phrase in all the given input files, print the phrase only in words, only in grammatical categories, or both.
 */
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
				switch (Environment.getAnnotationStripperOption()) {
				case LEX:
					while ((line = br.readLine()) != null) {
						Environment.getOutputStream().println(new TreeNode(line.substring(2, line.length() - 1)).getBarePhrase());
					}
					break;
				case CAT:
					while ((line = br.readLine()) != null) {
						Environment.getOutputStream().println(new TreeNode(line.substring(2, line.length() - 1)).getTaggingOnly());
					}
					break;
				case LEX_N_CAT:
					while ((line = br.readLine()) != null) {
						Environment.getOutputStream().println(new TreeNode(line.substring(2, line.length() - 1)).getTaggedPhrase());
					}
					break;
				}
			}
			catch (IOException e)
			{
				Messages.error(e.getMessage());
			}
		}
        Environment.closeOutput();
	}

}
