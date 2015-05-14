package planet.commonapi;

import java.util.Set;
import java.util.Vector;

/**
 * Interface which represents a node in a peer-to-peer system, regardless of
 * the underlying protocol.  This represents the *local* node, upon which applications
 * can call methods.
 * <p>
 * Any implementation must include the no argument constructor.
 * </p>
 * @author <a href="mailto: jordi.pujol@estudiants.urv.es">Jordi Pujol</a>
 * 07-jul-2005
 */
public interface EndPoint extends java.io.Serializable  {
	
	/**
	 * Returns this node's id, which is its identifier in the namespace.
	 *
	 * @return The local node's id
	 */
	public Id getId();
	
	/**
	 * Returns the Application's identification.
	 * @return Application's identification.
	 */
	public String getApplicationId();
	
	/**
	 * Gets the associated Application.
	 * @return The associated application.
	 */
	public Application getApplication();
  
	/**
	 * An upcall to inform to each Application for a new step.
	 * This method is invoked at the end of each simulation step. 
	 * Before this, may arrive any number of application Messages,
	 * depending on your own main application.
	 */
	public void byStep();
	
	/**
	 * Returns a handle to the local node below this endpoint.  This node handle is serializable,
	 * and can therefore be sent to other nodes in the network and still be valid.
	 *
	 * @return A NodeHandle referring to the local node.
	 */
	public NodeHandle getLocalNodeHandle();
	
	/**
	 * Schedules a message to be delivered to this application after the provided number of
	 * milliseconds.
	 *
	 * @param message The message to be delivered
	 * @param delay The number of milliseconds to wait before delivering the message
	 */
	public void scheduleMessage(RouteMessage message, long delay);
	
	/**
	 * Sends a broadcast message to all nodes in the network.
	 * @param id Key from the message to be broadcast.
	 * @param message Message to be routed to all nodes in the network.
	 */
	public void broadcast(Id id, Message message);

	/* END **************** COMMON API METHODS *****************************************/

	/**
	 * Forwarding the message to the Application
	 * @param message RouteMessage to deliver to the application level.
	 * @return true if the NodeImpl must forward the message.
	 */
	public boolean forward(RouteMessage message);
	
	/**
	 * This method makes an attempt to route the message to the root of the given id.
	 * The hint handle will be the first hop in the route. If the id field is null, then
	 * the message is routed directly to the given node, and delivers the message
	 * there.  If the hint field is null, then this method makes an attempt to route
	 * the message to the root of the given id.  Note that one of the id and hint fields can
	 * be null, but not both.
	 *
	 * @param id The destination Id of the message.
	 * @param message The message to deliver
	 * @param hint NodeHandle to be used to route the message. If it is null,
	 * the message must be routed as the overlay network establish it.
	 */
	public void route(Id id, Message message, NodeHandle hint);

	
	/**
	 * Returns a list of nodes that can be used as next hops on a route
	 * towards key. If is safe, the expected fraction of faulty nodes in the
	 * list is guaranteed to be no higher than the fraction of
	 * faulty nodes in the overlay.
	 * @param key Target key
	 * @param max Number of next hops.
	 * @param safe If true, the expected fraction of faulty nodes are the same
	 * of the nodes in the overlay.
	 * @return Until a maximum of max nodes to use as next hop.
	 */
	public Vector localLookup(Id key, int max, boolean safe);

	/**
	 * Obtains an unordered list of up to max nodes that are neighbors 
	 * of the local node.
	 * @param max Maximum of nodes to return.
	 * @return Neighbor nodes.
	 */
	public Vector neighborSet(int max);

	/**
	 * Returns an ordered set of nodes on which replicas of the object with 
	 * this key can be stored. The maximum number of nodes is maxRank. If
	 * the implementation's maximum replica set size is lower, then its
	 * maximum replica set is returned.
	 * @param key Id for which we find the replica set.
	 * @param maxRank Maximum number of members in replica set.
	 * @return An array of nodes with the replica set.
	 */
	public Vector replicaSet(Id key, int maxRank);

	/**
	 * This operation provides information about ranges of keys for
	 * which the <b>node</b> is currently a root. Returns false if
	 * the range could not be determined, true otherwise.
	 * <br><br>
	 * It is an error to query the range of a node not present in
	 * the neighbor set.
	 * <br><br>
	 * The [leftKey,rightKey] denotes the inclusive range of key values.
	 * @param node Node that is currently a root of some range of keys.
	 * @param rank Number of keys that is root the node (rank=rightKey-leftKey).
	 * @param leftKey The value that appears in the invokation is the candidate left
	 * key of the range. It may be modified to reflect the correct left margin
	 * once invokation has finished.
	 * @param rightkey Shows once the invokation has finished the left margin of
	 * the range.
	 * @return true if the range chold be determined; false otherwise, including
	 * the case of node is not present in the neighbor set returned by neighborSet.
	 */
	public boolean range(NodeHandle node, Id rank, Id leftKey,
			Id rightkey);

	/* END **************** COMMON API METHODS *****************************************/
	
	/**
	 * Gets all node connections to other nodes as its NodeHandles. The order of NodeHandles
	 * is unexpected. 
	 * @return A set with all connections as NodeHandles.
	 */
	public Set getAllLinks();

    /**
     * Sets the initial values for this EndPoint.
     * @param app Uplying application.
     * @param node Underlaying node.
     * @return The same instance, once it has been updated.
     */
    public EndPoint setValues(Application app, Node node);
}
