import static org.junit.Assert.*;

import org.junit.Test;


public class GrammarTest
{

	@Test
	public void testAddRule()
	{
		Grammar		g;
		String		tab[];
		String		ref[];
		int			i;

		g = new Grammar();
		tab = new String[2];
		tab[0] = "N";
		tab[1] = "PP";
		ref = new String[3];
		ref[0] = "VP";
		ref[1] = "NP";
		ref[2] = "DP";
		g.addRule(new RewritingRule("NP", tab, 0.8));
		g.addRule(new RewritingRule("VP", tab, 0.9));
		g.addRule(new RewritingRule("DP", tab, 0.6));
		i = 0;
		for (RewritingRule r : g.getRules(tab))
		{
			assertEquals(ref[i], r.getLHS());
			i++;
		}
	}
	
	@Test
	public void testRuleParsing()
	{
		Grammar		g;
		String		ref[];
		int			i;

		g = new Grammar();
		ref = new String[3];
		ref[0] = "VP";
		ref[1] = "NP";
		ref[2] = "DP";
		g.addRule("0.8 NP -> NP VP");
		g.addRule("0.9 VP -> NP VP");
		g.addRule("0.6 DP -> NP VP");
		i = 0;
		for (RewritingRule r : g.getRules("NP VP"))
		{
			assertEquals(ref[i], r.getLHS());
			i++;
		}
	}
}
