package planet.commonapi;

/**
 * This interface is an abstraction of a node handle from the CommonAPI paper. A
 * node handle is a handle to a known node, which conceptually includes the
 * node's Id, as well as the node's underlying network address (such as IP/port).
 *
 * @author <a href="mailto: jordi.pujol@estudiants.urv.es">Jordi Pujol</a>
 * 07-jul-2005
 */
public abstract class NodeHandle  extends java.util.Observable implements java.io.Serializable {
	
	//constants defining types of observable events
	public static final Integer PROXIMITY_CHANGED = new Integer(1);
	public static final Integer DECLARED_DEAD = new Integer(2);
	public static final Integer DECLARED_LIVE = new Integer(3);
	
	/**
	 * Returns this node's id.
     *
     * @return The corresponding node's id.  
	 */
	public abstract Id getId();
	
	/**
	 * Returns whether or not this node is currently alive
	 *
	 * @return Whether or not this node is currently alive
	 */ 
	public abstract boolean isAlive();
	
	/**
	 * Updates the alive flag.
	 * @param alive New value for the alive flag.
	 */
	public abstract void setAlive(boolean alive);
	
	/**
	 * Evaluates the proximity between the node who invokes this method,
	 * and the node that represents this NodeHandle.
	 * @return The proximity between the two nodes, who invokes and 
	 * the node represented by this NodeHandle.
	 */
	public abstract int getProximity();
    
    /**
     * Sets the new Id for this NodeHandle, and the flag 'alive' to true.
     * @param newValue The new Id
     * @return The same instance, after being updated.
     */
    public abstract NodeHandle setValues(Id newValue);
    
    /**
     * Sets the new Id for this NodeHandle, and the flag 'alive' to the
     * specified value.
     * @param newValue The new Id
     * @param alive true if the NodeHandle is alive.
     * @return The same instance, after being updated.     
     */
    public abstract NodeHandle setValues(Id newValue, boolean alive);
	
}
