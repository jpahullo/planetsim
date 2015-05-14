package planet.commonapi.behaviours.exception;

/**
 * This Exception is thrown whenever a incoming RouteMessage pattern has
 * type or mode out of range. 
 * @author Marc Sanchez
 */
public class OutOfRangeError extends java.lang.Error {
	private static String defaultMessage = "RouteMessage's Type or Mode Out Of Range";
	/**
	 * Generates new instance with default message.
	 */
	public OutOfRangeError() {
		super(defaultMessage);
	}
	
	/**
	 * Generates nes instance with the specified <b>message</b>
	 * @param message Message to be inicialized the Exception.
	 */
	public OutOfRangeError(String message) {
		super(message);
	}
	
	/**
	 * Inicialization of the new instance. 
	 * @param message The message to be inicialized.
	 * @param cause Throwable cause of the exception.
	 */
	public OutOfRangeError(String message, Throwable cause) {
		super(message, cause);
	}
	
	/**
	 * Initialization of the new instance.
	 * @param cause Throwable cause of the exception.
	 */
	public OutOfRangeError(Throwable cause) {
		super(cause);
	}
}
