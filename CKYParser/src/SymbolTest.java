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

}
