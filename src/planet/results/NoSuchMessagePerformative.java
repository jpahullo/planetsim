package planet.results;

/**
 * This Exception is thrown whenever a incoming RouteMessage pattern does
 * not match any Message Status on LinkState results. 
 * @author Marc Sanchez
 */
public class NoSuchMessagePerformative extends Exception {
	private static String defaultMessage = "No Such Message Performative";
	/**
	 * Generates new instance with default message.
	 */
	public NoSuchMessagePerformative() {
		super(defaultMessage);
	}
	
	/**
	 * Generates nes instance with the specified <b>message</b>
	 * @param message Message to be inicialized the Exception.
	 */
	public NoSuchMessagePerformative(String message) {
		super(message);
	}
	
	/**
	 * Inicialization of the new instance. 
	 * @param message The message to be inicialized.
	 * @param cause Throwable cause of the exception.
	 */
	public NoSuchMessagePerformative(String message, Throwable cause) {
		super(message, cause);
	}
	
	/**
	 * Initialization of the new instance.
	 * @param cause Throwable cause of the exception.
	 */
	public NoSuchMessagePerformative(Throwable cause) {
		super(cause);
	}
}
