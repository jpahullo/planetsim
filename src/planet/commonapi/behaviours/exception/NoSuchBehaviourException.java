package planet.commonapi.behaviours.exception;

/**
 * This Exception is thrown whenever a incoming RouteMessage pattern does
 * not match any Behaviour pattern on BehavioursPool scheduler. 
 * @author Marc Sanchez
 */
public class NoSuchBehaviourException extends Exception {
	private static String defaultMessage = "No Such Behaviour For This RouteMessage";
	/**
	 * Generates new instance with default message.
	 */
	public NoSuchBehaviourException() {
		super(defaultMessage);
	}
	
	/**
	 * Generates nes instance with the specified <b>message</b>
	 * @param message Message to be inicialized the Exception.
	 */
	public NoSuchBehaviourException(String message) {
		super(message);
	}
	
	/**
	 * Inicialization of the new instance. 
	 * @param message The message to be inicialized.
	 * @param cause Throwable cause of the exception.
	 */
	public NoSuchBehaviourException(String message, Throwable cause) {
		super(message, cause);
	}
	
	/**
	 * Initialization of the new instance.
	 * @param cause Throwable cause of the exception.
	 */
	public NoSuchBehaviourException(Throwable cause) {
		super(cause);
	}
}
