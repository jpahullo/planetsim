package planet.test.trivialp2ptest;
import planet.commonapi.*;
/**
 * Simple Message to contain an Id to deliver a remote application.
 * 
 * @author Carles Pairot <cpairot@etse.urv.es>
 * @author Jordi Pujol <jordi.pujol@estudiants.urv.es>
 * @version 1.0
 */
public class TrivialMessage implements Message {
	/**
	 * Contents of this message.
	 */
	private Id data = null;
    private String dataStr = null;
	
	/**
	 * Builds a new message with the specified data
	 * @param data  Data to be set to new message.
	 */
	public TrivialMessage(Id data) {
		this.data = data;
	}
	
	/**
	 * Gets the content of the message.
	 * @return The internal Id.
	 */
	public Id getData() {
		return data;
	}
    
    /**
     * Gets a string representation of this application message.
     * @return A string representation of this application message.
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
        if (dataStr==null)
            dataStr = "TrivialMessage with key["+data+"]";
        return dataStr;
    }
}