import static org.junit.Assert.*;

import org.junit.Test;

public class RewritingRuleTest
{

	@Test
	public void testConstructor()
	{
		RewritingRule	r;
		String			tab[];

		tab = new String[2];
		tab[0] = "V";
		tab[1] = "DP";
		r = new RewritingRule("VP", tab, 0.5);
		assertEquals("VP", r.getLHS());
		assertArrayEquals(tab, r.getRHS());
		assertEquals(0.5, r.getProbability(), 0.001);
	}
	
	@Test
	public void testEquals()
	{
		String[]		tab;
		RewritingRule	r;
		
		tab = new String[1];
		tab[0] = "V";
		r = new RewritingRule("VP", tab, 0.5);
		assertFalse(r.equals(null));
		assertFalse(r.equals("Toto"));
		assertTrue(r.equals(new RewritingRule("VP", tab, 0)));
		assertFalse(r.equals(new RewritingRule("VP", new String[2], 0)));
		assertFalse(r.equals(new RewritingRule("Toto", tab, 0)));
		assertFalse(r.equals(new RewritingRule("Toto", new String[2], 0)));
	}
	
	@Test
	public void testComparison()
	{
		String[]		rhs;
		RewritingRule	r0;
		RewritingRule	r1;
		
		rhs = new String[1];
		rhs[0] = "NP";
		r0 = new RewritingRule("DP", rhs, 0.5);
		r1 = new RewritingRule("DP", rhs, 0.3);
		assertEquals(1, r0.compareTo(r1));
		assertEquals(0, r0.compareTo(r0));
		assertEquals(-1, r1.compareTo(r0));
	}
}
