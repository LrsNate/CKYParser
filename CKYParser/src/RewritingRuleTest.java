import static org.junit.Assert.*;

import java.util.LinkedList;

import org.junit.Test;

public class RewritingRuleTest
{

	@Test
	public void testConstructor()
	{
		Symbol				lhs;
		LinkedList<Symbol>	rhs;
		RewritingRule		r;
		
		lhs = new Symbol("DP");
		rhs = new LinkedList<Symbol>();
		rhs.add(new Symbol("NP"));
		r = new RewritingRule(lhs, rhs, 0.5);
		assertEquals(lhs, r.getLHS());
		assertEquals(rhs, r.getRHS());
		assertEquals(0.5, r.getProbability(), 0.001);
	}
	
	@Test
	public void testEquals()
	{
		Symbol				lhs;
		LinkedList<Symbol>	rhs;
		RewritingRule		r;
		
		lhs = new Symbol("DP");
		rhs = new LinkedList<Symbol>();
		rhs.add(new Symbol("NP"));
		r = new RewritingRule(lhs, rhs, 0.5);
		assertFalse(r.equals(null));
		assertFalse(r.equals("Toto"));
		assertTrue(r.equals(new RewritingRule(lhs, rhs, 0)));
		assertFalse(r.equals(new RewritingRule(lhs,
				new LinkedList<Symbol>(),
				0)));
		assertFalse(r.equals(new RewritingRule(new Symbol("Toto"), rhs, 0)));
		assertFalse(r.equals(new RewritingRule(new Symbol("Toto"),
				new LinkedList<Symbol>(),
				0)));
	}
	
	@Test
	public void testComparison()
	{
		Symbol				lhs;
		LinkedList<Symbol>	rhs;
		RewritingRule		r0;
		RewritingRule		r1;
		
		lhs = new Symbol("DP");
		rhs = new LinkedList<Symbol>();
		rhs.add(new Symbol("NP"));
		r0 = new RewritingRule(lhs, rhs, 0.5);
		r1 = new RewritingRule(lhs, rhs, 0.3);
		assertEquals(1, r0.compareTo(r1));
		assertEquals(0, r0.compareTo(r0));
		assertEquals(-1, r1.compareTo(r0));
	}
}
