package main;

public class UnknownWordException extends Exception {

	private static final long	serialVersionUID	= 1L;
	
	public UnknownWordException (String message) {
		super(message);
	}
}
