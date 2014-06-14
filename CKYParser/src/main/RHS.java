package main;

import java.util.Collections;
import java.util.LinkedList;

public class RHS {
	
	/**
	 * the right-hand side of the production
	 *
	 */
	private final LinkedList<Symbol> _rhs;
	
	/**
	 * create an RHS with a single symbol in it
	 * @param rhs_smb: the symbol in the RHS
	 */
	public RHS(Symbol rhs_smb) {
		this._rhs = new LinkedList<Symbol>();
		this._rhs.add(rhs_smb);
	}
	
	public RHS(LinkedList<Symbol> rhs) {
		this._rhs = rhs;
	}
	
	/**
	 * create an RHS out of a list of "names" of symbols
	 * @param rhs_str_list: a list of strings where each string represents a single Symbol in the RHS
	 */
	public RHS(String[] rhs_str_list) {
		this._rhs = Symbol.ListSymbols(rhs_str_list);
	}
	
	public RHS(String s) {
		this(s.split(" "));
	}
	
	public Symbol get(int i) {
		return this._rhs.get(i);
	}
	
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
	
	public String getStringUnsorted() {
		// get a list of string representations of symbols
		LinkedList<String> list_smb_str = new LinkedList<String>();
		for (Symbol smb : this._rhs) {
			list_smb_str.add(smb.toString());
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
	 * NOTE: it produces the hashcode of the SET of symbols in the right-hand side
	 * (i.e. the order does not matter)
	 */
	public int hashCode()
	{
		String key = getStringUnsorted();
	    return key.hashCode();
	}

}
