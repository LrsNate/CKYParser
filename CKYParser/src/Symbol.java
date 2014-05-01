/**
 * A symbol as part of either the terminal or non-terminal vocabulary of a
 * grammar.
 * @author Antoine LAFOUASSE
 */
public class Symbol
{
	private final String		_value;
	private final boolean		_isTerminal;
	
	public Symbol(String value, boolean isTerminal)
	{
		this._value = value;
		this._isTerminal = isTerminal;
	}
	
	public Symbol(String value)
	{
		this(value, false);
	}
	
	@Override
	public boolean equals(Object o)
	{
		Symbol		s;

		if (o == null || !(o instanceof Symbol))
			return (false);
		s = (Symbol)o;
		return (this._value.equals(s._value)
				&& this._isTerminal == s._isTerminal);
	}
	
	public boolean isTerminal()
	{
		return (this._isTerminal);
	}
	
	@Override
	public String toString()
	{
		return (this._value);
	}
}
