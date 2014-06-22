package test;

import static org.junit.Assert.*;

import java.util.LinkedList;

import org.junit.Test;

import main.CKY;
import main.ReverseGrammar;
import main.Symbol;
import main.Tree;
import main.UnknownWordException;
public class CKYTest {

	@Test
	public void testParser() throws UnknownWordException {
		
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
		g.addRule("0.5 N -> \\people");
		g.addRule("0.2 N -> \\fish");
		g.addRule("0.2 N -> \\tanks");
		g.addRule("0.1 N -> \\rods");
		g.addRule("0.1 V -> \\people");
		g.addRule("0.6 V -> \\fish");
		g.addRule("0.3 V -> \\tanks");
		g.addRule("1.0 P -> \\with");
		
		// create the parser and parse!
		
		System.out.println("No unknown words in the input.");
		
		CKY parser = new CKY(g);
		
		LinkedList<Tree> res = parser.parse(Symbol.ListSymbols(("\\fish \\people \\fish \\tanks").split(" ")), 5);
		String tree = res.getFirst().treeToString();
		
		assertEquals(tree, "(S (NP (NP (N \\fish)) (NP (N \\people))) (VP (V \\fish) (NP (N \\tanks))))");
		
		for (Tree t : res) {
			System.out.println(t.toString());
		}
		
	}
	
	@Test
	public void testParserOneword() throws UnknownWordException {
		
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
		g.addRule("0.5 N -> \\people");
		g.addRule("0.2 N -> \\fish");
		g.addRule("0.2 N -> \\tanks");
		g.addRule("0.1 N -> \\rods");
		g.addRule("0.1 V -> \\people");
		g.addRule("0.6 V -> \\fish");
		g.addRule("0.3 V -> \\tanks");
		g.addRule("1.0 P -> \\with");
		
		// create the parser and parse!
		
		System.out.println("No unknown words in the input.");
		
		CKY parser = new CKY(g);
		
		LinkedList<Tree> res = parser.parse(Symbol.ListSymbols(("\\fish").split(" ")), 5);
		String tree = res.getFirst().treeToString();
		
		assertEquals(tree, "(S (VP (V \\fish)))");
		
		for (Tree t : res) {
			System.out.println(t.toString());
		}
		
	}
	
	@Test
	public void testParserApriori() throws UnknownWordException {
		
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
		g.addRule("0.5 N -> \\people");
		g.addRule("0.2 N -> \\fish");
		g.addRule("0.2 N -> \\tanks");
		g.addRule("0.1 N -> \\rods");
		g.addRule("0.1 V -> \\people");
		g.addRule("0.6 V -> \\fish");
		g.addRule("0.3 V -> \\tanks");
		g.addRule("1.0 P -> \\with");
		
		// create the parser and parse!
		
		System.out.println("Set an apriori probability of getting an unknown word.");
		CKY parser_apriori = new CKY(g,0.5);
		LinkedList<Tree> res_apriori = parser_apriori.parse(Symbol.ListSymbols(("\\fish \\people \\fish \\hanks").split(" ")), 5);
		String tree_apriori = res_apriori.getFirst().treeToString();
		System.out.println(tree_apriori);
		
		assertEquals(tree_apriori, "(S (NP (NP (N \\fish)) (NP (N \\people))) (VP (V \\fish) (NP (N \\hanks))))");
		
		for (Tree t : res_apriori) {
			System.out.println(t.toString());
		}
	}
	
	@Test
	public void testParserUnk() throws UnknownWordException {
		
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
		
		// the unknown word
		Symbol unknown = new Symbol("**UNKNOWN**",true);
		
		// add lexical rules
		g.addRule("0.5 N -> \\people");
		g.addRule("0.2 N -> \\fish");
		g.addRule("0.2 N -> \\tanks");
		g.addRule("0.1 N -> \\**UNKNOWN**");
		g.addRule("0.1 V -> \\people");
		g.addRule("0.6 V -> \\fish");
		g.addRule("0.28 V -> \\tanks");
		g.addRule("0.02 V -> \\**UNKNOWN**");
		g.addRule("1.0 P -> \\with");
		
		// create the parser and parse!
		
		System.out.println("The probabilities of unknown words are already integrated into the grammar.");
		CKY parser_unk = new CKY(g,unknown);
		LinkedList<Tree> res_unk = parser_unk.parse(Symbol.ListSymbols(("\\fish \\people \\fish \\hanks").split(" ")), 5);
		String tree_unk = res_unk.getFirst().treeToString();
		System.out.println(tree_unk);
		
		assertEquals(tree_unk, "(S (NP (NP (N \\fish)) (NP (N \\people))) (VP (V \\fish) (NP (N \\hanks))))");
		
		for (Tree t : res_unk) {
			System.out.println(t.toString());
		}
	}
	
	@Test
	public void testParserLog() throws UnknownWordException {
		
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
		g.addRule("0.5 N -> \\people");
		g.addRule("0.2 N -> \\fish");
		g.addRule("0.2 N -> \\tanks");
		g.addRule("0.1 N -> \\rods");
		g.addRule("0.1 V -> \\people");
		g.addRule("0.6 V -> \\fish");
		g.addRule("0.3 V -> \\tanks");
		g.addRule("1.0 P -> \\with");
		
		// create the parser and parse!
		
		System.out.println("No unknown words in the input. Log probabilities.");
		
		boolean mode_log = true;
		CKY parser = new CKY(g, mode_log);
		
		LinkedList<Tree> res = parser.parse(Symbol.ListSymbols(("\\fish \\people \\fish \\tanks").split(" ")), 5);
		String tree = res.getFirst().treeToString();
		
		assertEquals(tree, "(S (NP (NP (N \\fish)) (NP (N \\people))) (VP (V \\fish) (NP (N \\tanks))))");
		
		for (Tree t : res) {
			System.out.println(t.toString());
		}
		
	}
	
	@Test
	public void testParserAprioriLog() throws UnknownWordException {
		
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
		g.addRule("0.5 N -> \\people");
		g.addRule("0.2 N -> \\fish");
		g.addRule("0.2 N -> \\tanks");
		g.addRule("0.1 N -> \\rods");
		g.addRule("0.1 V -> \\people");
		g.addRule("0.6 V -> \\fish");
		g.addRule("0.3 V -> \\tanks");
		g.addRule("1.0 P -> \\with");
		
		// create the parser and parse!
		
		System.out.println("Set an apriori probability of getting an unknown word. Log probabilities.");
		boolean log_mode = true;
		CKY parser_apriori = new CKY(g,0.5,log_mode);
		LinkedList<Tree> res_apriori = parser_apriori.parse(Symbol.ListSymbols(("\\fish \\people \\fish \\hanks").split(" ")), 5);
		String tree_apriori = res_apriori.getFirst().treeToString();
		System.out.println(tree_apriori);
		
		assertEquals(tree_apriori, "(S (NP (NP (N \\fish)) (NP (N \\people))) (VP (V \\fish) (NP (N \\hanks))))");
		
		for (Tree t : res_apriori) {
			System.out.println(t.toString());
		}
	}
	
	@Test
	public void testParserUnkLog() throws UnknownWordException {
		
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
		
		// the unknown word
		Symbol unknown = new Symbol("**UNKNOWN**",true);
		
		// add lexical rules
		g.addRule("0.5 N -> \\people");
		g.addRule("0.2 N -> \\fish");
		g.addRule("0.2 N -> \\tanks");
		g.addRule("0.1 N -> \\**UNKNOWN**");
		g.addRule("0.1 V -> \\people");
		g.addRule("0.6 V -> \\fish");
		g.addRule("0.28 V -> \\tanks");
		g.addRule("0.02 V -> \\**UNKNOWN**");
		g.addRule("1.0 P -> \\with");
		
		// create the parser and parse!
		
		System.out.println("The probabilities of unknown words are already integrated into the grammar. Log probabilities.");
		boolean log_mode = true;
		CKY parser_unk = new CKY(g,unknown,log_mode);
		LinkedList<Tree> res_unk = parser_unk.parse(Symbol.ListSymbols(("\\fish \\people \\fish \\hanks").split(" ")), 5);
		String tree_unk = res_unk.getFirst().treeToString();
		System.out.println(tree_unk);
		
		assertEquals(tree_unk, "(S (NP (NP (N \\fish)) (NP (N \\people))) (VP (V \\fish) (NP (N \\hanks))))");
		
		for (Tree t : res_unk) {
			System.out.println(t.toString());
		}
	}

}
