import static org.junit.Assert.assertEquals;

import org.junit.Test;


public class RewrRuleProbTest {

	@Test
	public void testRuleParsing()
	{
		ReverseGrammar		g;
		String		ref[];
		int			i;

		ref = new String[3];
		ref[0] = "0.8 NP -> NP VP";
		ref[1] = "0.9 VP -> NP VP";
		ref[2] = "0.6 DP -> NP VP";
		
		//i = 0;
		//for (RewritingRule r : g.getRules("NP VP"))
		//{
		//	assertEquals(ref[i], r.getLHS());
		//	i++;
		//}
	}

}
