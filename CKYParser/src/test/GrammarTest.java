package test;

import static org.junit.Assert.*;

import org.junit.Test;
import main.ReverseGrammar;
import main.Symbol;
import main.RHS;
import main.RewrRuleProb;
public class GrammarTest
{

	@Test
	public void testAddRule()
	{
		ReverseGrammar		g;
		Symbol		left_hand_sides[];
		RHS		right_hand_sides[];
		
		Symbol axiom = new Symbol("S");

		g = new ReverseGrammar(axiom);
		
		left_hand_sides = new Symbol[4];
		left_hand_sides[0] = new Symbol("Nbar");
		left_hand_sides[1] = new Symbol("NP");
		left_hand_sides[2] = new Symbol("VP");
		left_hand_sides[3] = new Symbol("VP");
		
		right_hand_sides = new RHS[4];
		right_hand_sides[0] = new RHS("N PP");
		right_hand_sides[1] = new RHS("Det Nbar");
		right_hand_sides[2] = new RHS("V PP");
		right_hand_sides[3] = new RHS("V N PP");
		for (int i = 0; i < 4; i++) {
			g.addRule(new RewrRuleProb(left_hand_sides[i], right_hand_sides[i], 0.8));
		}
		int j = 0;
		for (RewrRuleProb r : g.getRules(right_hand_sides[j]))
		{
			assertEquals(left_hand_sides[j], r.getLHS());
			j++;
		}
	}

}
