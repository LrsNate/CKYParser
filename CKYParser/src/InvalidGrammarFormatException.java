
public class InvalidGrammarFormatException extends Exception {
	
	private static final long serialVersionUID = 1L;
	private String message;

	public InvalidGrammarFormatException() {
	}
	
	public InvalidGrammarFormatException(String message) {
		this.message = message;
	}
	
	@Override
	public String getMessage()
	{
		return (this.message);
	}
}
