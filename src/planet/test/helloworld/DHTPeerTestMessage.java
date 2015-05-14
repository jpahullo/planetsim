package planet.test.helloworld;
import planet.commonapi.*;
/**
 * Simple Message to contain a String to deliver a remote application.
 * 
 * @author Carles Pairot <cpairot@etse.urv.es>
 * @author Jordi Pujol <jordi.pujol@estudiants.urv.es>
 * @version 1.0
 */
public class DHTPeerTestMessage implements Message {
	/**
	 * Contents of this message.
	 */
	private String data = null;
	
	/**
	 * Builds a new message with the specified data
	 * 
	 * @param data
	 *            Data to be set to new message.
	 */
	public DHTPeerTestMessage(String data) {
		this.data = data;
	}
	
	/**
	 * Gets the content of the message.
	 * 
	 * @return A String containing all information of this message.
	 */
	public String getData() {
		return data;
	}
}