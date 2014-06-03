import static org.junit.Assert.*;

import java.util.LinkedList;

import org.junit.Test;


public class CKYTest {

	@Test
	public void testParser() {
		
		// create grammar and add non-lexical rules
		ReverseGrammar g = new ReverseGrammar(new Symbol("S"));
		g.addRule("0.9 S -> NP VP");
		g.addRule("0.1 S -> VP");
		g.addRule("0.5 VP -> V NP");
		g.addRule("0.1 VP -> V");
		g.addRule("0.3 VP -> V @VP_V");
		g.addRule("0.1 VP -> V PP");
		g.addRule("1.0 @VP_V -> NP PP");
		g.addRule("0.1 NP -> NP NP");
		g.addRule("0.2 NP -> NP PP");
		g.addRule("0.7 NP -> N");
		g.addRule("1.0 PP -> P NP");
		
		// add lexical rules
		g.addRule("0.5 N -> people");
		g.addRule("0.2 N -> fish");
		g.addRule("0.2 N -> tanks");
		g.addRule("0.1 N -> rods");
		g.addRule("0.1 V -> people");
		g.addRule("0.6 V -> fish");
		g.addRule("0.3 V -> tanks");
		g.addRule("1.0 P -> with");
		
		// create the parser and parse!
		
		CKY parser = new CKY(g);
		
		LinkedList<Tree> res = parser.parse(Symbol.ListSymbols(("fish people fish tanks").split(" ")), 5);
		String tree = res.getFirst().toString();
		
		assertEquals(tree, "(S (NP (NP (N fish)) (NP (N people))) (VP (V fish) (NP (N tanks))))");
		
		for (Tree t : res) {
			System.out.println(t.toString());
		}
	}

}
