package planet.commonapi;

import java.util.Collection;
import java.util.Set;
import java.util.Vector;

import planet.commonapi.exception.InitializationException;
import planet.commonapi.results.ResultsConstraint;
import planet.util.QueueFull;
import planet.util.timer.TimerTask;

/**
 * Interface which represents a node in a peer-to-peer system, regardless of
 * the underlying protocol.  This represents a factory, in a sense, that will
 * give a application an Endpoint which it can use to send and receive
 * messages.
 *
 * @author <a href="mailto: jordi.pujol@estudiants.urv.es">Jordi Pujol</a>
 * @author <a href="mailto: marc.sanchez@urv.net">Marc Sanchez</a>
 * 07-jul-2005
 */
public interface Node extends java.io.Serializable {
    
	/**
     * This returns a VirtualizedNode specific to the given application and
     * instance name to the application, which the application can then use
     * in order to send and receive messages.
     *
     * @param app The Application 
     * @param instance An identifier for a given instance
     * @return The endpoint specific to this application, which can be used
     * for message sending/receiving. 
	 */
	public EndPoint registerApplication (Application app, String instance);
	
	/**
	 * Get the registered application by the <b>instanceName</b> name.
	 * @param instanceName Name of the registered application.
	 * @return null if there isn't an instance of <b>instanceName</b>, or 
	 * the related endpoint for the application.
	 */
	public EndPoint getRegisteredApplication(String instanceName);
	
	/**
	 * Returns all references of the applications that mantains this node.
	 * @return An array with all references to the applications that they
	 * are mantained for this node.
	 */
	public Application[] getRegisteredApplications ();
	
	/**
     * Returns the Id of this node
     *
     * @return This node's Id 
	 */
	public Id getId();
	
	/**
	 * Receive's data of the application level and sends it through de
	 * network.
	 * @param appId String that includes the application identification.
	 * @param to Node that must receive this message. If cannot represents
	 * a real node.
	 * @param nextHop Node that must receive this message as first hop.
	 * @param msg Message to be sent.
	 * If it is null, the message must be routed.
	 */
	public void routeData(String appId,
			NodeHandle to, NodeHandle nextHop, Message msg);
	
	/**
	 * This Node is joined by another node identified by <b>boot</b> to the actual network. 
	 * @param boot NodeHandle of Node to join to the actual network.
	 */
	public void join (NodeHandle boot);
	
	/**
	 * This method permits to the actual node to leave the
	 * network.
	 */
	public void leave();
	
	/**
	 * If supose TCP connections onto network layer, this method
	 * must informe with some type of error message to its 
	 * connected nodes for broken connection. In other case,
	 * nothing should be required.
	 */
	public void fail();
	
	/**
	 * It permits to the node
	 * to send messages, run stabilization, etc.
	 * @param actualStep Actual step to simulate
	 * @return true if the node needs to continue the stabilization
	 * process. false if node is just stabilized.
	 */
	public boolean process (int actualStep);
	
	/**
	 * This method is invoked to send a message <b>msg</b> to the
	 * actual Node (another ----> me).
	 * @param msg Message to be received for the actual Node.
	 * @throws QueueFull if the incoming queue of the actual Node is full.
	 */
	public void receive(RouteMessage msg) throws QueueFull;
	
	/**
	 * This method is invoked to send a message <b>msg</b> to
	 * another Node (me ---> another).
	 * @param msg Message to be received for the remote Node.
	 * @throws QueueFull if the outgoing queue of the actual Node is full.
	 */
	public void send(RouteMessage msg) throws QueueFull;
	
	/**
	 * Returns the outgoing queue with message to be send.
	 * @return The queue with the outgoing messages to be send.
	 */
	public planet.util.Queue outMessages();

	/**
	 * Shows for System.out all information of the node, including finger table.
	 */
	public void printNode();
	
	/**
	 * Shows for System.out only the owner Id, its predecessor and successor.
	 */
	public void prettyPrintNode();
	
	/**
	 * Initiating a Broadcast Message
	 * @param appId String that includes the application identification.
	 * @param to Node that must receive this message. If cannot represents
	 * a real node.
	 * @param nextHop Node that must receive this message as first hop.
	 * If it is null, the message must be routed.
	 */
	public void broadcast (String appId,
			NodeHandle to, NodeHandle nextHop, Message msg);
	
	/**
	 * Returns the NodeHandle for the actual Node.
	 * @return NodeHandle for the actual Node.
	 */
	public NodeHandle getLocalHandle();
	
	/**
     * Returns the NodeHandle of the present predecessor of the node
	 * @return predeccesor NodeHandle of this Node
	 */
	public NodeHandle getPred();
	
	/**
     * Returns the NodeHandle of the present successor of the node
	 * @return successor NodeHandle of this Node
	 */
	public NodeHandle getSucc();
	
	/**
	 * Informs if this node is alive.
	 * @return true if the node is alive. false if the node has
	 * fail or leave.
	 */
	public boolean isAlive();
	
	/**
	 * Returns the successor list of the present node with <b>max</b> number of Ids. 
	 *
	 * @param max number of the elements (NodeHandle's) to return
	 * @return successor list Vector that contains NodeHandle's of successors.
	 */
	public java.util.Vector getSuccList(int max);
	
	/* ********************************** COMMONAPI EXTENSIONS **************************** */ 
	/**
	 * The playsGoodRole's method is an extension method for commonapi specs. This method is
	 * used to allow BehavioursPool decide  wether the <b>Node</b> should run behaviours for
	 * good  peers or instead run  behaviours for bad peers. Moreover, this method  lets the
	 * programmer the responsability of decide node's role. Even it allows a node to  have a
	 * transient behaviour, sometimes behaving as a good peer and sometines behaving as a bad
	 * peer. 
	 * @return True if the Node's Role is positive.
	 */
	public boolean playsGoodRole();
	/**
	 * The setGoodRole's method is an extension method for commonapi specs. This method is 
	 * used to set the role of the node inside the overlay network. A true value means the
	 * node will behave according to the underlying overlay protocol. A false  value means
	 * the node will misbehave.
	 * @param role
	 */
	public void setGoodRole(boolean role);
	/**
	 * The isLocalMessage's method is an extension method for commonapi specs. This method 
	 * is used to allow BehavioursPool decide wether the incoming RouteMessage  is for the
	 * local node or for a  remote node. Remenber, this decision may only be addressed  by 
	 * the underlying overlay protocol. For example, for Symphony's Lookup protocol a node 
	 * is  responsible for all  RouteMessages whose keys have as an immediate succesor the 
	 * node's id or have as destination the own node.  
	 * @see planet.commonapi.behaviours.BehavioursPool
	 * @return True if the incoming RouteMessage taken as input its for the local node.
	 */
	public boolean isLocalMessage(RouteMessage msg);

    /* **************************** TIMERS ***********************************/
	/**
	 * Sets a new task to make only once time. 
	 * @param task Job to do at the unique activation.
	 * @param firstTime First moment at which the <b>task</b> will be activated, in
	 * absolute representation, not relative for the actual time.
	 */
	public void setTimer(TimerTask task, long firstTime);
	
	/**
	 * Sets a new task to make periodicly at <b>period</b> time. 
	 * @param task Job to do at each activation.
	 * @param firstTime First moment at which the <b>task</b> will be activated,
	 * in absolute representation, not relative for the actual time
	 * @param period Number of steps or millis of the period.
	 */
	public void setTimer(TimerTask task, long firstTime, long period);
	
	
	
	/* ***************************** COMMON API ROUTING STATUS QUERY ****************/
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
	 * @param rightKey Shows once the invokation has finished the left margin of
	 * the range.
	 * @return true if the range chold be determined; false otherwise, including
	 * the case of node is not present in the neighbor set returned by neighborSet().
	 */
	public boolean range(NodeHandle node, Id rank, Id leftKey,
			Id rightKey);
	
	/* END ************************ COMMON API ROUTING STATUS QUERY ****************/
	
	/* BEGIN ************************ RESULTS RELATED METHODS ****************/
	
	/**
	 * This method is a callback method used to collect all edges of a graph 
     * according to <b>resultName</b> format.
     * @param resultName Result type name to use.
	 * @param edgeCollection Edge collection where to put in all produced ResultEdges.
	 * @param constraint ResultsConstraint for edge selection
	 */
	public void buildEdges(String resultName, Collection edgeCollection, ResultsConstraint constraint);
	
	/* END ************************  RESULTS RELATED METHODS ****************/
	
	/** 
	 * Returns the NodeHandle closest to <b>id</b>.
	 * @param id Id to find.
	 * @return The NodeHandle closest to <b>id</b>.
	 */
	public abstract NodeHandle getClosestNodeHandle(Id id);
	
	/**
	 * Gets all node connections to other nodes as its NodeHandles. The order of NodeHandles
	 * is unexpected. 
	 * @return A set with all connections as NodeHandles.
	 */
	public Set getAllLinks();
    
    /**
     * Sets the new Id for this Node.
     * @param newId The new Id
     * @return The same instance, after has been updated.
     */
    public Node setValues(Id newId) throws InitializationException;
}
