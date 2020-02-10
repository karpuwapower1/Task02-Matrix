package by.training.karpilovich.task02.exception;

public class FormatException extends Exception {

	private static final long serialVersionUID = 1L;

	public FormatException() {
		super();
	}

	public FormatException(String message, Throwable cause) {
		super(message, cause);
	}

	public FormatException(String message) {
		super(message);
	}

	public FormatException(Throwable cause) {
		super(cause);
	}

}
