package org.kosa.nest.exception;

public class PasswordNotCorrectException extends Exception {

	private static final long serialVersionUID = -5176523977221973772L;
	
	public PasswordNotCorrectException(String message) {
		super(message);
	}
}
