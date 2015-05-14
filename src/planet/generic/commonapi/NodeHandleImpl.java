package planet.generic.commonapi;

import planet.commonapi.NodeHandle;
import planet.commonapi.Id;

/**
 * NodeHandle for node. Being so simple his API, surely will be 
 * able to be reused for different nodes. It only mantain the Node Id
 * and a flag that informs if the related Node is alive.
 * 
 * @author <a href="mailto: jordi.pujol@estudiants.urv.es">Jordi Pujol</a>
 * Date: 13/05/2004
 */
public class NodeHandleImpl extends NodeHandle implements Comparable {
	/**
	 * Node Id.
	 */
	private Id nodeId;
	/**
	 * true if related Node is alive.
	 */
	private boolean alive;
	
    public NodeHandleImpl()
    {
        nodeId = null;
        alive = false;
    }
    
	/**
	 * Gets the Id of the related node
	 * @see planet.commonapi.NodeHandle#getId()
	 * @return Id of the node
	 */
	public Id getId() {
		return nodeId;
	}

	/**
	 * Inform if the related node is alive.
	 * @see planet.commonapi.NodeHandle#isAlive()
	 * @return true if the related node is alive. false
	 * in other case.
	 */
	public boolean isAlive() {
		return alive;
	}
	
	/**
	 * Updates the alive flag.
	 * @see planet.commonapi.NodeHandle#setAlive(boolean)
	 * @param alive New value for the alive flag.
	 */
	public void setAlive(boolean alive) {
		this.alive = alive;
	}
	
	/**
	 * Returns the String representation of this NodeHandle
	 * @see java.lang.Object#toString()
	 * @return String representation with the nodeId and alive flag.
	 */
	public String toString() {
		//return "{NodeHandleImpl, nodeId=["+nodeId+"], alive=["+alive+"]}";
		return ""+nodeId;
	}
	
	/**
	 * Always returns one as proximity between any two nodes.
	 * @see planet.commonapi.NodeHandle#getProximity()
	 * @return Always one
	 */
	public int getProximity() {
		return 1;
	}
	
	/**
	 * Overwrites this method to reflect the comparison between
	 * the related Id's. That is:
	 * <br>
	 * <center><b>return getId().equals(((NodeHandle)obj).getId());</b></center>
	 * @see java.lang.Object#equals(java.lang.Object)
	 * @param obj NodeHandle to be compared.
	 * @return true if and only if the related Id's are equals. false in
	 * other case.
	 */
	public boolean equals(Object obj) {
		return getId().equals(((NodeHandle)obj).getId());
	}
	
    /**
     * Returns less than, equals than or greater than zero if this Id
     * is less than, equals than or greater than <b>obj</b> Id.
     * @param obj Another NodeHandle
     * @return Less than, equals than or greater than zero.
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public int compareTo(Object obj) {
        return getId().compareTo(((NodeHandle)obj).getId());
    }
    
	/**
	 * Overwrites this method to reflect the hash code of the related Id.
	 * That is:
	 * <br>
	 * <center><b>return getId().hashCode();</b></center>
	 * @see java.lang.Object#hashCode()
	 * @return The hash code of the related Id.
	 */
	public int hashCode() {
		return getId().hashCode();
	}
    
    
    /**
     * Sets the 'newValue' Id and the flag alive to true.
     * @param newValue New Id.
     * @see planet.commonapi.NodeHandle#setValues(planet.commonapi.Id)
     */
    public NodeHandle setValues(Id newValue) {
        nodeId = newValue;
        alive = true;
        return this;
    }
    
    
    /**
     * Sets the values for this NodeHandle.
     * @param newValue The new Id
     * @param alive If the NodeHandle is alive
     * @see planet.commonapi.NodeHandle#setValues(planet.commonapi.Id, boolean)
     */
    public NodeHandle setValues(Id newValue, boolean alive) {
        nodeId = newValue;
        this.alive = alive;
        return this;
    }
}
