
import java.util.LinkedList;
import java.util.Arrays;
import java.util.NoSuchElementException;

/**
 * A (context-free) production (rewriting rule)
 * @author yuliya
 *
 */
public class RewrRule {
	/**
	 * the left-hand side of the production
	 */
	protected final Symbol _lhs;
	/**
	 * the right-hand side of the production
	 */
	protected final RHS _rhs;
	
	/**
	 * construct a production rule
	 * @param lhs: left-hand side
	 * @param rhs: right-hand side
	 */
	public RewrRule(Symbol lhs, LinkedList<Symbol> rhs) {
		if (lhs.IsTerminal()) {
			throw new IllegalArgumentException();
		}
		this._lhs = lhs; 
		this._rhs = new RHS(rhs);
	}
	
	/**
	 * construct a production rule
	 * @param lhs: left-hand side
	 * @param rhs: right-hand side
	 */
	public RewrRule(Symbol lhs, RHS rhs) {
		if (lhs.IsTerminal()) {
			throw new IllegalArgumentException();
		}
		this._lhs = lhs; 
		this._rhs = rhs;
	}
	
	/**
	 * construct a production rule
	 * @param lhs: left-hand side
	 * @param rhs_smb: right-hand side
	 */
	public RewrRule(Symbol lhs, Symbol rhs_smb) {
		if (lhs.IsTerminal()) {
			throw new IllegalArgumentException();
		}
		this._lhs = lhs;
		_rhs = new RHS(rhs_smb);
	}
	
	/**
	 * construct a production rule from a string
	 * @param str_rule: a string of the form "Abc -> beta", \
	 * where Abc is a name of a single non-terminal symbol, \
	 * beta is a string with several names of symbols separated by white-spaces 
	 */
	public RewrRule(String str_rule) {
		LinkedList<String> list_words = new LinkedList<String>( Arrays.asList(str_rule.split(" ")) );		
		try {
			// the first symbol is the left-hand side of the rule
			this._lhs = new Symbol(list_words.removeFirst());
			// assume a context-free grammar => the first symbol should be a non-terminal
			if (this._lhs.IsTerminal()) {
				throw new IllegalArgumentException();
			}			
			// then comes any symbol that separates the _lhs from the rhs
			// '->'
			list_words.removeFirst();
			// then comes the right-hand side of the rule
			// (a string with several names of symbols separated by white-spaces)
			this._rhs = new RHS(list_words.toArray(new String[list_words.size()]));
		} catch (NoSuchElementException e) {
			throw new IllegalArgumentException();
		}
	}
	
	/**
	 * construct a production rule
	 * @param lhs_str: a string representing the left-hand side (a single name of a non-terminal)
	 * @param rhs_str: a string representing the right-hand side (several names of symbols separated by white-spaces)
	 */
	public RewrRule(String lhs_str, String rhs_str) {
		// the left-hand side of the rule
		this._lhs = new Symbol(lhs_str);
		if (_lhs.IsTerminal()) {
			throw new IllegalArgumentException();
		}
		// the right-hand side of the rule
		// (a string with several names of symbols separated by white-spaces)
		String[] list_symbol_names = rhs_str.split(" ");
		this._rhs = new RHS(list_symbol_names);
	}
	
	/**
	 * get the number of symbols in the right-hand side of the rule
	 * @return: the number of symbols in the right-hand side of the rule
	 */
	public int RhsSize() {
		return _rhs.size();
	}
	
	/**
	 * get the left-hand side of the rule
	 * @return: the symbol in the left-hand side of the rule
	 */
	public Symbol getLHS() {
		return _lhs;
	}
	
	/**
	 * get the right-hand side of the rule
	 * @return: the symbol in the left-hand side of the rule
	 */
	public RHS getRHS() {
		return _rhs;
	}
	
	@Override
	public String toString() {
		String str_rule = _lhs + " ->" + _rhs;
		return str_rule;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == null) {
			return false;
		}
		if (o instanceof RewrRule) {
			return ((!(((RewrRule)o).getLHS().equals(this._lhs))) && (!(((RewrRule)o).getRHS().equals(this._rhs))));
		}
		return false;
	}
	
	@Override
	public int hashCode()
	{
		String key = this.toString();
	    return key.hashCode();
	}


}
