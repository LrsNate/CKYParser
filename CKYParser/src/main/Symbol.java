package main;

import java.util.LinkedList;

/**
 * A symbol: terminal or non-terminal  
 * @author yuliya
 *
 */
public class Symbol {
	/**
	 * the name of the symbol
	 */
	private final String value;
	/**
	 * is true if the symbol is terminal, false otherwise 
	 */
	private final boolean terminal;
	
	/**
	 * Construct a Symbol with the name corresponding to the String in the parameter
	 * @param str: the string encoding the name of the symbol and its type (terminal / non-terminal) at once \
	 * (if str starts with an unescaped backslash ('\'), then the Symbol is terminal, and its name is "str minus that backslash")
	 */
	public Symbol(String str) {
		char first_char = str.charAt(0); 
		if (first_char == '\\') {
			if (!(str.length() > 1)) {
				throw new IllegalArgumentException();
			}
			int count_leading_slashes = count_leading_occ(str, '\\');
			if (odd(count_leading_slashes)) {
				this.value = str.substring(1);
				this.terminal = true;
				return;
			}
		}
		
		this.value = str;
		this.terminal = false;
	}
	
	/**
	 * 
	 * @param value: the name of the Symbol
	 * @param terminal: true if the Symbol is terminal
	 */
	public Symbol(String value, boolean terminal) {
		this.value = value; this.terminal = terminal;
	}
	
	/**
	 * Count the number of leading occurrences of a character in the string
	 * (i.e. the length of the prefix that contains no other characters than c)
	 * @param s: a String
	 * @param c: the character the occurrences of which are to be count
	 * @return : the number of leading occurrences of the character c in the string s
	 */
	private int count_leading_occ(String s, char c) {
		int i = 0;
		while ((i < s.length()) && (s.charAt(i) == c)) {
			i++;			
		}
		return i;
	}
	
	/**
	 * Determine if an integer is odd
	 * @param x
	 * @return: true if the integer x is odd, false otherwise
	 */
	private boolean odd(int x) {
		if ( (x & 1) == 1 ) { return true; } 
		return false;
	}
	
	/**
	 * convert a list of strings into a list of symbols
	 * @param list_words: list of strings
	 * @return: list of symbols (produced by calls to the constructor Symbol(String))
	 */
	public static LinkedList<Symbol> ListSymbols(String[] list_words) {
		LinkedList<Symbol> list_symbols = new LinkedList<Symbol>();
		for (String word : list_words) {
			list_symbols.add(new Symbol(word));
		}
		return list_symbols;
	}
	
	/**
	 * 
	 * @return: true iff the Symbol is terminal 
	 */
	public boolean IsTerminal() {
		return this.terminal;
	}
	
	/**
	 * 
	 * @return: the name of the Symbol
	 */
	public String getValue() {
		return this.value;
	}
	
	@Override
	public String toString() {
		if (this.terminal) {
			return '\\' + this.value;
		} else {
			return this.value;
		}
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof Symbol) {
			if ((((Symbol)o).IsTerminal() == this.terminal) && (((Symbol)o).getValue().equals(this.value))) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public int hashCode()
	{
		String key = this.value;
		if (this.terminal) {
			key = '\\' + key;
		}
	    return key.hashCode();
	}

	/*
	public static void main(String[] args) {
		Symbol S1 = new Symbol("SV", false);
		Symbol S2 = new Symbol("SV", false);
		System.out.println(S1.equals(S2));
		System.out.println(S1.hashCode() == S2.hashCode());
		
		Map<Symbol, HashSet<Integer> > G = new HashMap<Symbol,HashSet<Integer> >();
		if (!(G.containsKey(S1))) {
			G.put(S1, new HashSet<Integer>());		
		}
		G.get(S1).add(1);	
		if (!(G.containsKey(S2))) {
			System.out.println("not found");
			G.put(S2, new HashSet<Integer>());		
		} else {
			System.out.println("found");
		}
		G.get(S2).add(2);
		
	}
	*/

}
