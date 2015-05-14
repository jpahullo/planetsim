package planet.chord.message;

import planet.commonapi.NodeHandle;

/**
 * Chord uses this message to send a special message to broadcasting.  
 * @author <a href="mailto: jordi.pujol@estudiants.urv.es">Jordi Pujol</a>
 * Date: 01/07/2004
 */
public class BroadcastMessage implements planet.commonapi.Message {
	private planet.commonapi.Message info;
	private NodeHandle limit;
	
	/**
	 * Shows the <b>info</b> to be send on the broadcast, and the <b>limit</b>
	 * up to permits to resend this message.
	 * @param info Message to be send on the broadcast.
	 * @param limit Maximum clockwise NodeHandle that permits to 
	 * resend this message.
	 */
	public BroadcastMessage(planet.commonapi.Message info, NodeHandle limit) {
		this.info = info;
		this.limit = limit;
	}
	/**
	 * @return Returns the info.
	 */
	public planet.commonapi.Message getInfo() {
		return info;
	}
	/**
	 * @param info
	 *            The info to set.
	 */
	public void setInfo(planet.commonapi.Message info) {
		this.info = info;
	}
	/**
	 * @return Returns the limit.
	 */
	public NodeHandle getLimit() {
		return limit;
	}
	/**
	 * @param limit
	 *            The limit to set.
	 */
	public void setLimit(NodeHandle limit) {
		this.limit = limit;
	}
	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "BroadcastMessage: Message:[" + info + "]; Limit:[" + 
		limit.getId() + "]";
	}
}
