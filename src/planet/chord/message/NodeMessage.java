package planet.chord.message;

import planet.commonapi.NodeHandle;

/**
 * Chord uses this message to inform to another one any NodeHandle
 * that it is required.
 * @author <a href="mailto: jordi.pujol@estudiants.urv.es">Jordi Pujol</a>
 * Date: 01/07/2004
 */
public class NodeMessage implements planet.commonapi.Message {
	private NodeHandle handle = null;
	
	/**
	 * Shows the NodeHandle to send with this message.
	 * @param handle NodeHandle to send.
	 */
	public NodeMessage(NodeHandle handle) {
		this.handle = handle;
	}
	
	/**
	 * @return Returns the NodeHandle.
	 */
	public NodeHandle getNode() {
		return handle;
	}
	
	/**
	 * @param handle
	 *            The NodeHandle to set.
	 */
	public void setNode(NodeHandle handle) {
		this.handle = handle;
	}
	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "NodeMessage: Node:[" + handle + "]";
	}
}
