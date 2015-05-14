package planet.commonapi.exception;

/**
 * This Exception is thrown by cannot read completely a properties file of
 * any Factory, and some factory cannot initialize any parameter on its
 * setter methods. The message will be specified for each error. 
 * @author Jordi Pujol
 */
public class InitializationException extends Exception {
	private static String defaultMessage = "Error reading a properties file";
	/**
	 * Generates new instance with default message.
	 */
	public InitializationException() {
		super(defaultMessage);
	}
	
	/**
	 * Generates nes instance with the specified <b>message</b>
	 * @param message Message to be inicialized the Exception.
	 */
	public InitializationException(String message) {
		super(message);
	}
	
	/**
	 * Inicialization of the new instance. 
	 * @param message The message to be inicialized.
	 * @param cause Throwable cause of the exception.
	 */
	public InitializationException(String message, Throwable cause) {
		super(message, cause);
	}
	
	/**
	 * Initialization of the new instance.
	 * @param cause Throwable cause of the exception.
	 */
	public InitializationException(Throwable cause) {
		super(cause);
	}
}
