import static org.junit.Assert.*;

import org.junit.Test;


public class TreeTest
{
	@Test
	public void testConstructors()
	{
		Tree	i0;
		Tree	i1;
		Tree	i2;
		Tree	i3;
		
		i0 = new Tree("N");
		i1 = new Tree("NP", 0.5, i0);
		assertEquals("(NP N)", i1.toString());
		i2 = new Tree("VP");
		i3 = new Tree("S", 0.5, i1, i2);
		assertEquals("(S (NP N) VP)", i3.toString());
	}

	@Test
	public void testComparison()
	{
		Tree	i0;
		Tree	i1;
		Tree	i2;
		
		i0 = new Tree("Leaf");
		i1 = new Tree("i1", 0.5, i0);
		i2 = new Tree("i2", 0.3, i1);
		assertEquals(0, i1.compareTo(i1));
		assertEquals(1, i1.compareTo(i2));
		assertEquals(-1, i2.compareTo(i1));
	}
}
