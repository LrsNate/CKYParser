import static org.junit.Assert.*;

import org.junit.Test;


public class ChartTest
{
	@Test
	public void test()
	{
		Chart	c;
		String	tab[];
		int		i;
		
		c = new Chart();
		c.addTree(3, 3, new Tree("V"));
		c.addTree(3, 3, new Tree("VP", 0.5, new Tree("V")));
		tab = new String[2];
		tab[0] = "V";
		tab[1] = "(VP V)";
		i = 0;
		System.out.println(c.getTrees(3, 3));
		for (Tree t : c.getTrees(3, 3))
		{
			assertEquals(tab[i], t.toString());
			i++;
		}
	}
}
