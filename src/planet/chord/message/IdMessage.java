package planet.chord.message;

import planet.commonapi.Id;

/**
 * Chord uses this message to inform to another one any Id
 * that it is required.
 * @author <a href="mailto: jordi.pujol@estudiants.urv.es">Jordi Pujol</a>
 * Date: 01/07/2004
 */
public class IdMessage implements planet.commonapi.Message {
	private Id id = null;
	
	/**
	 * Shows the Id to send with this message.
	 * @param id Id to send.
	 */
	public IdMessage(Id id) {
		this.id = id;
	}
	
	/**
	 * @return Returns the Id.
	 */
	public Id getNode() {
		return id;
	}
	
	/**
	 * @param id
	 *            The Id to set.
	 */
	public void setNode(Id id) {
		this.id = id;
	}
	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "IdMessage: Id:[" + id + "]";
	}
}
