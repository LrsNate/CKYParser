/**
 * A symbol as part of either the terminal or non-terminal vocabulary of a
 * grammar.
 * @author Antoine LAFOUASSE
 */
public class Symbol
{
	private String		_value;
	private boolean		_isTerminal;
	
	public Symbol(String value, boolean isTerminal)
	{
		this._value = value;
		this._isTerminal = isTerminal;
	}
	
	public Symbol(String value)
	{
		this(value, false);
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
