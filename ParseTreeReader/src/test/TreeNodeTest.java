package test;

import main.TreeNode;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * A JUnit test case for main.TreeNode
 * @author Antoine LAFOUASSE
 *
 */
public class TreeNodeTest
{
	private TreeNode	_instance;
	private TreeNode	_instance2;

	@Before
	public void setUp() throws Exception
	{
		this._instance = new TreeNode("(NP (DET le) (NC chat))");
		this._instance2 = new TreeNode("VP (V appelle) (NP (NPP Marie))");
	}

	@Test
	public void testTreeNode()
	{
		@SuppressWarnings("unused")
		TreeNode	instance;
		
		try
		{
			instance = new TreeNode("");
			Assert.fail("Empty value Exception not thrown.");
		}
		catch (IllegalArgumentException e)
		{
		}
		catch (Exception e)
		{
			Assert.fail("Exception thrown.");
		}
		instance = new TreeNode("NPP Marie");
	}
	
	@Test
	public void testDump()
	{
		String		s1;
		String		s2;
		
		s1 = "NP -> DET NC\n";
		s2 = "VP -> V NP\nNP -> NPP\n";
		Assert.assertEquals(s1, this._instance.dump());
		Assert.assertEquals(s2, this._instance2.dump());
	}

	@Test
	public void testGetValue()
	{
		
		Assert.assertEquals("NP", this._instance.getValue());
		Assert.assertEquals("VP", this._instance2.getValue());
	}

	@Test
	public void testToString()
	{
		Assert.assertEquals("NP -> DET NC", this._instance.toString());
		Assert.assertEquals("VP -> V NP", this._instance2.toString());
	}
}
