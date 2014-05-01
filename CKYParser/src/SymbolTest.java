import static org.junit.Assert.*;

import org.junit.Test;

public class SymbolTest
{
	@Test
	public void testConstructors()
	{
		Symbol	i0;
		Symbol	i1;
		
		i0 = new Symbol("VP");
		i1 = new Symbol("Marie", true);
		assertFalse(i0.isTerminal());
		assertTrue(i1.isTerminal());
		assertEquals("VP", i0.toString());
		assertEquals("Marie", i1.toString());
	}
	
	@Test
	public void testEquals()
	{
		Symbol		i0;
		Symbol		i1;
		Symbol		i2;
		Symbol		i3;
		Symbol		i4;
		
		i0 = new Symbol("Marie", false);
		i1 = new Symbol("Marie", false);
		i2 = new Symbol("Marie", true);
		i3 = new Symbol("Toto", false);
		i4 = new Symbol("Toto", true);
		assertFalse(i0.equals(null));
		assertFalse(i0.equals("Toto"));
		assertTrue(i0.equals(i1));
		assertFalse(i0.equals(i2));
		assertFalse(i0.equals(i3));
		assertFalse(i0.equals(i4));
	}
}
