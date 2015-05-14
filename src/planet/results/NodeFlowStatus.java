package planet.results;

import planet.commonapi.Id;
/**
 * NodeFlowStaus is a class designed to keep track of flow on 
 * each node on the overlay. Hence, a flow involves to count
 * incoming, outcoming and dropped messages by the node. The
 * dropped messages are messages dropped by the node which 
 * not belong to him. 
 * @author Marc Sanchez		<marc.sanchez@estudiants.urv.es>
 */
public class NodeFlowStatus implements java.io.Serializable {
	/**
	 * Node's Id which depends on the overlay impl. 
	 * @see planet.commonapi.Id 
	 */
	private Id node;
	/**
	 * Stores the total number of incoming messages.
	 */
	private int incoming;
	/**
	 * Stores the total number of outcoming messages.
	 */
	private int outcoming;
	/**
	 * Stores the total number of dropped messages.
	 */
	private int dropped;
	
	// Constructor
	public NodeFlowStatus(Id node) {
		this.node = node;
	}
	/**
	 * Updates the number incoming messages by this node.
	 */
	public void updateIncoming() {
		incoming++;
	}
	/**
	 * Updates the number outcoming messages by this node.
	 */
	public void updateOutcoming() {
		outcoming++;
	}
	/**
	 * Updates the number of dropped messages by this node. 
	 */
	public void updateDropped() {
		dropped++;
	}
	/**
	 * @return Returns the node's Id.
	 */
	public Id getId() {
		return this.node;
	}
	/**
	 * @return Returns the number of incoming messages.
	 */
	public int getIncoming() {
		return this.incoming;
	}
	/**
	 * @return Returns the number of outcoming messages.
	 */
	public int getOutcoming() {
		return this.outcoming;
	}
	/**
	 * @return Returns the number of dropped messages.
	 */
	public int getDropped() {
		return this.dropped;
	}
}
