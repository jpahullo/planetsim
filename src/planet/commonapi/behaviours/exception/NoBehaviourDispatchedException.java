package planet.commonapi.behaviours.exception;

/**
 * This Exception is thrown whenever a incoming RouteMessage pattern match
 * a set of behaviours but no one has been dispatched.
 * @author Marc Sanchez
 */
public class NoBehaviourDispatchedException extends Exception {
	private static String defaultMessage = "No Behaviour Was Dispatched";
	/**
	 * Generates new instance with default message.
	 */
	public NoBehaviourDispatchedException() {
		super(defaultMessage);
	}
	
	/**
	 * Generates nes instance with the specified <b>message</b>
	 * @param message Message to be inicialized the Exception.
	 */
	public NoBehaviourDispatchedException(String message) {
		super(message);
	}
	
	/**
	 * Inicialization of the new instance. 
	 * @param message The message to be inicialized.
	 * @param cause Throwable cause of the exception.
	 */
	public NoBehaviourDispatchedException(String message, Throwable cause) {
		super(message, cause);
	}
	
	/**
	 * Initialization of the new instance.
	 * @param cause Throwable cause of the exception.
	 */
	public NoBehaviourDispatchedException(Throwable cause) {
		super(cause);
	}
}
