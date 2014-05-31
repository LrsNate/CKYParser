
public class InvalidGrammarFormatException extends Exception {
	private String message = "";

	public InvalidGrammarFormatException() {
	}
	
	public InvalidGrammarFormatException(String message) {
		this.message = message;
	}

}
