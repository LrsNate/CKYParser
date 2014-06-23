package main;

import java.util.LinkedList;

/**
 * A symbol: terminal or non-terminal.
 *
 */
public class Symbol {
	/**
	 * The name of the symbol.
	 */
	private final String value;
	
	/**
	 * Set to true if the symbol is terminal, false otherwise 
	 */
	private final boolean terminal;
	
	/**
	 * Construct a Symbol with the name corresponding to the String in the parameter.
	 * @param str The string encoding the name of the symbol and its type (terminal / non-terminal) at once
	 * (if the string starts with an unescaped backslash ('\'), then the Symbol is terminal, 
	 * and its name is the passed string without that backslash).
	 */
	public Symbol(String str) {
		char first_char = str.charAt(0); 
		// check if this is supposed to be a terminal symbol
		if (first_char == '\\') {
			if (!(str.length() > 1)) {
				throw new IllegalArgumentException();
			}
			int count_leading_slashes = count_leading_occ(str, '\\');
			// this is a terminal
			if (odd(count_leading_slashes)) {
				this.value = str.substring(1).toLowerCase();
				this.terminal = true;
				return;
			}
		}
		
		this.value = str;
		this.terminal = false;
	}
	
	/**
	 * Construct a Symbol with the name corresponding to the String in the parameter. 
	 * The type of the symbol (terminal / non-terminal) is given as a second argument.
	 * @param value The name of the symbol.
	 * @param terminal True if the symbol is terminal, false if it is non-terminal.
	 */
	public Symbol(String value, boolean terminal) {
		this.value = value; 
		this.terminal = terminal;
		if (this.terminal) { this.value.toLowerCase(); }
	}
	
	/**
	 * Count the number of leading occurrences of a character in the string
	 * (i.e. the length of the prefix that contains no other characters than the given character c).
	 * @param s String to get the prefix.
	 * @param c The character the occurrences of which are to be count.
	 * @return The number of leading occurrences of the character c in the string s.
	 */
	private int count_leading_occ(String s, char c) {
		int i = 0;
		while ((i < s.length()) && (s.charAt(i) == c)) {
			i++;			
		}
		return i;
	}
	
	/**
	 * Determine if an integer is odd.
	 * @param x The integer to check.
	 * @return: True if the integer x is odd, false otherwise.
	 */
	private boolean odd(int x) {
		if ( (x & 1) == 1 ) { return true; } 
		return false;
	}
	
	/**
	 * Convert a list of strings into a list of terminal or non-terminal symbols.
	 * @param list_words A list of strings to convert.
	 * @param are_terminal Set to true if all the symbols are terminal, false if all of them are non-terminal.
	 * @return List of symbols (produced by calls to the constructor Symbol(String s)).
	 */
	public static LinkedList<Symbol> ListSymbols(String[] list_words, boolean are_terminal) {
		LinkedList<Symbol> list_symbols = new LinkedList<Symbol>();
		for (String word : list_words) {
			list_symbols.add(new Symbol(word, are_terminal));
		}
		return list_symbols;
	}
	
	/**
	 * Convert a list of strings into a list of symbols.
	 * Whether each symbol is terminal or not is determined from the string itself (based on the slash conventions).
	 * @param list_words List of strings.
	 * @return List of symbols (produced by calls to the constructor Symbol(String s)).
	 */
	public static LinkedList<Symbol> ListSymbols(String[] list_words) {
		LinkedList<Symbol> list_symbols = new LinkedList<Symbol>();
		for (String word : list_words) {
			list_symbols.add(new Symbol(word));
		}
		return list_symbols;
	}
	
	/**
	 * Detect if the symbol is terminal.
	 * @return True iff the symbol is terminal.
	 */
	public boolean IsTerminal() {
		return this.terminal;
	}
	
	/**
	 * Getter for the value field.
	 * @return The name of the symbol.
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
}
