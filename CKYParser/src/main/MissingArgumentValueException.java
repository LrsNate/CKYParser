package main;

public class MissingArgumentValueException extends Exception {

	private static final long	serialVersionUID	= 1L;
	
	public MissingArgumentValueException(String message) {
		super(message);
	}

}
