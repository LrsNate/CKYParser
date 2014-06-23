package main;

public class MissingObligatoryArgException extends Exception {

	private static final long	serialVersionUID	= 1L;
	
	public MissingObligatoryArgException(String message) {
		super(message);
	}

}
