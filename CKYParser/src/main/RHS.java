package main;

import java.util.Collections;
import java.util.LinkedList;

/**
 * A right-hand side of a rewriting rule.
 *
 */
public class RHS {
	
	/**
	 * The right-hand side of a production.
	 *
	 */
	private final LinkedList<Symbol> _rhs;
	
	/**
	 * Create a RHS with a single symbol in it.
	 * @param rhs_smb The symbol in the RHS.
	 */
	public RHS(Symbol rhs_smb) {
		this._rhs = new LinkedList<Symbol>();
		this._rhs.add(rhs_smb);
	}
	
	/**
	 * Create a RHS with multiple symbols in it.
	 * @param rhs The right-hand side as a list of symbols.
	 */
	public RHS(LinkedList<Symbol> rhs) {
		this._rhs = rhs;
	}
	
	/**
	 * Create a RHS out of a list of "names" of symbols.
	 * @param rhs_str_list A list of strings where each string represents a single symbol in the RHS.
	 */
	public RHS(String[] rhs_str_list) {
		this._rhs = Symbol.ListSymbols(rhs_str_list);
	}
	
	/**
	 * Create a RHS out of a string. Symbols are separated by spaces.
	 * @param s The string representing the right-hand side of a rule.
	 */
	public RHS(String s) {
		this(s.split(" "));
	}
	
	/**
	 * Get the symbol in the given position in the RHS.
	 * @param i The index of the symbol.
	 * @return A symbol of the RHS.
	 */
	public Symbol get(int i) {
		return this._rhs.get(i);
	}
	
	/**
	 * Get the length of the RHS (in symbols).
	 * @return The number of symbols in the RHS.
	 */
	public int size() {
		return this._rhs.size();
	}
	
	@Override
	public String toString() {
		String res_str = "";
		for (Symbol smb : _rhs) {
			res_str = res_str + " " + smb;
		}
		return res_str;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == null) {
			return false;
		}
		if (o instanceof RHS) {
			RHS o_rhs = (RHS)o;
			if (o_rhs.size() != this._rhs.size()) {
				return false;				
			}
			for (int i = 0; i < this._rhs.size(); i++) {
				if (!(o_rhs.get(i).equals(this._rhs.get(i)))) {
					return false;
				}
			}
			return true;
		}
		return false;
	}
	
	/**
	 * Get a string consisting of the symbols contained in the RHS without keeping their order.
	 * @return A string that contains a set of all the symbols of the RHS.
	 */
	public String getStringUnsorted() {
		// get a list of string representations of symbols
		LinkedList<String> list_smb_str = new LinkedList<String>();
		for (Symbol smb : this._rhs) {
			list_smb_str.add(smb.getStringForHashcode());
		}
		// sort the string representations of the symbols
	    // in order to get rid of the dependency on the order
		Collections.sort(list_smb_str);
		// concatenate to a string
		String key = list_smb_str.get(0);
		for (int i = 1; i < list_smb_str.size(); i++) {
			key += " " + list_smb_str.get(i);
		}
		return key;
	}
	
	@Override
	/**
	 * Get the hashcode of the RHS.
	 * NOTE: it produces the hashcode of the SET of symbols in the right-hand side
	 * (i.e. the order does not matter).
	 */
	public int hashCode()
	{
		String key = getStringUnsorted();
	    return key.hashCode();
	}

}
