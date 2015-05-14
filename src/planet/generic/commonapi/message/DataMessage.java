package planet.generic.commonapi.message;

import planet.commonapi.Id;

/**
 * Message of data to be send over the underlying network. This Message is
 * created by the EndPoint to save both key and message sended by the 
 * application.
 * @author <a href="mailto: jordi.pujol@estudiants.urv.es">Jordi Pujol</a>
 * Date: 01/07/2004
 */
public class DataMessage implements planet.commonapi.Message {
	private Id messageKey = null;
	private planet.commonapi.Message message = null;
	
	public DataMessage (Id key, planet.commonapi.Message msg) {
		this.messageKey = key;
		this.message = msg;
	}
	/**
	 * @return Returns the messageKey.
	 */
	public Id getMessageKey() {
		return messageKey;
	}
	/**
	 * @param messageKey The messageKey to set.
	 */
	public void setMessageKey(Id messageKey) {
		this.messageKey = messageKey;
	}
	/**
	 * @return Returns the msg.
	 */
	public planet.commonapi.Message getMessage() {
		return message;
	}
	/**
	 * @param msg The msg to set.
	 */
	public void setMessage(planet.commonapi.Message msg) {
		this.message = msg;
	}
	
	/**
	 * Shows a String representation of this DataMessage
	 * @see java.lang.Object#toString()
	 * @return String representation of this DataMessage
	 */
	public String toString() {
		return "DataMessage: Key["+messageKey+"] Message["+message+"]";
	}
}
