package planet.trivialp2p;

import java.util.Collection;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;
import java.util.Vector;

import planet.commonapi.Id;
import planet.commonapi.Message;
import planet.commonapi.Node;
import planet.commonapi.NodeHandle;
import planet.commonapi.RouteMessage;
import planet.commonapi.behaviours.BehavioursPool;
import planet.commonapi.exception.InitializationException;
import planet.commonapi.results.ResultsConstraint;
import planet.commonapi.results.ResultsEdge;
import planet.generic.commonapi.NodeImpl;
import planet.generic.commonapi.factory.GenericFactory;
import planet.simulate.Results;
import planet.util.Properties;

/**
 * It is a trivial implementation of a P2P overlay network. Each node only
 * has two links: the predecessor and successor in a ring topology. This
 * overlay not contains stabilization protocol and is assigned under 
 * network building.
 * @author <a href="mailto: jordi.pujol@estudiants.urv.es">Jordi Pujol</a>
 * 03-jun-2005
 */
public class TrivialNode extends NodeImpl {

    /* ******************  CONSTANTS FOR MODE OF ROUTEMESSAGE *******/
    /**
     * Mode value: Defines a message's mode that requires a routing task
     * in a oneway communication.
     */
    public final static int REQUEST                 = 0;        
    /**
     * Mode value: Defines a message's mode to be delivered directly in a
     * oneway communication.
     */
    public final static int REFRESH                 = 1;        
    
    /* END ******************  CONSTANTS FOR MODE OF ROUTEMESSAGE *******/
    
    /* ******************  CONSTANTS FOR TYPE OF ROUTEMESSAGE *******/
     /**
      * Type value: It identifies that this message contains an application 
      * level Message. 
      */
     public final static int DATA                     = 0;    

    /* END ******************  CONSTANTS FOR TYPE OF ROUTEMESSAGE *******/
    
    /* ******************  CONSTANTS FOR TYPE/MODE OF ROUTEMESSAGE *******/
    /**
     * This String contains a string representation of each type
     * value of the RouteMessage.
     */
    public final static String[] TYPES = { "DATA" };
    /**
     * This String contains a string representation of each mode
     * value of the RouteMessage.
     */
    public final static String[] MODES = { "REQUEST", "REFRESH" };
    
    /* END **************  CONSTANTS FOR TYPE/MODE OF ROUTEMESSAGE *******/
    
    // Routing table:
    /** The successor of the actual node. */
    private NodeHandle successor;
    /** The predecessor of the actual node. */
    private NodeHandle predecessor;
    /** Contains ALL links of the actual node. */
    private Set links;
    /** Contains the unique node successor. */
    private Vector successors;
    /** If true, the node is already alive. */
    private boolean alive;
    /** The behaviours pool to be used. */
    private BehavioursPool behPool;
    
    
    /* ******************** STARTING IMPLEMENTATION **************************/
    
    /**
     * Initialize the internal structure.
     */
    public TrivialNode() throws InitializationException {
        super();
        alive = true;
        successor = null;
        predecessor = null;
        links = new HashSet(2); 
        successors = new Vector(1);
        if (Properties.overlayWithBehaviours)
            behPool = GenericFactory.getDefaultBehavioursPool();
    }

    /**
     * Nothing does. This implementation don't contain a stabilization protocol.
     * @param bootstrap Bootstrap node.
     * @see planet.commonapi.Node#join(planet.commonapi.NodeHandle)
     */
    public void join(NodeHandle bootstrap) {
    }

    /**
     * Nothing does. Only sets the alive flag to false.
     * @see planet.commonapi.Node#leave()
     */
    public void leave() {
        alive = false;
    }

    /**
     * Gets the internal routing information in a hashtable.
     * The key informs the concept of the related value.
     * @return A hashtable with the internal routing information.
     * @see planet.generic.commonapi.NodeImpl#getInfo()
     */
    public Hashtable getInfo() {
        Hashtable info = new Hashtable();
        info.put("successor",successor);
        info.put("predecessor",predecessor);
        return info;
    }

    /**
     * Returns the own nodehandle or its successor nodehandle, in a
     * clockwise proximity.
     * @param id The id to be find.
     * @return The nearest nodehandle in a clockwise manner.
     * @see planet.commonapi.Node#getClosestNodeHandle(planet.commonapi.Id)
     */
    public NodeHandle getClosestNodeHandle(Id id) {
        return (predecessor.getId().betweenE(predecessor.getId(),this.id)) ?
                    this.nodeHandle :
                    successor;
    }

    /**
     * Routes an application level message to the destination node.
     * @param appId Application name.
     * @param to Destination node (or key).
     * @param nextHop May be null. The next hop into the route.
     * @param msg Application level message to be sent.
     * @see planet.commonapi.Node#routeData(java.lang.String, planet.commonapi.NodeHandle, planet.commonapi.NodeHandle, planet.commonapi.Message)
     */
    public void routeData(String appId, NodeHandle to, NodeHandle nextHop, Message msg) {
        RouteMessage data = buildMessage(GenericFactory.generateKey(),nodeHandle,to,nextHop,DATA,REQUEST,appId,msg);
        if (data!=null)
        {
            Results.incTraffic();
            this.dispatchDataMessage(data,REQUEST,REFRESH);
        }
    }

    /**
     * Do nothing. Only sets to false the alive flag.
     * @see planet.commonapi.Node#fail()
     */
    public void fail() {
        alive = false;
    }

    /**
     * Prints out the routing information of this node.
     * @see planet.commonapi.Node#printNode()
     */
    public void printNode() {
        System.out.println("<Node id=\""+id+"\">");
        System.out.println("   <Successor   id=\""+successor.getId()+"\">");
        System.out.println("   <Predecessor id=\""+predecessor.getId()+"\">");
        System.out.println("</Node>");
    }

    /**
     * Prints out the local node information.
     * @see planet.commonapi.Node#prettyPrintNode()
     */
    public void prettyPrintNode() {
        System.out.println("<Node id=\""+id+"\"/>");
    }

    /**
     * This routing method is not implemented and always throws a 
     * NoSuchMethodError.
     * @param appId Application id that requires to send a broadcast message.
     * @param to Source node.
     * @param nextHop Next hop in the route.
     * @param msg Application level message to be delivered in the broadcast.
     * @see planet.commonapi.Node#broadcast(java.lang.String, planet.commonapi.NodeHandle, planet.commonapi.NodeHandle, planet.commonapi.Message)
     * @throws NoSuchMethodError always this method is invoked.
     */
    public void broadcast(String appId, NodeHandle to, NodeHandle nextHop,
            Message msg) {
        throw new NoSuchMethodError("Method not implemented yet.");
    }

    /**
     * Gets the predecessor nodehandle.
     * @return The predecessor nodeHandle.
     * @see planet.commonapi.Node#getPred()
     */
    public NodeHandle getPred() {
        return predecessor;
    }

    /**
     * Gets the successor nodehandle.
     * @return The successor nodehandle.
     * @see planet.commonapi.Node#getSucc()
     */
    public NodeHandle getSucc() {
        return successor;
    }

    /**
     * Shows whenever this method is alive.
     * @return true if the node is alive. false in other case.
     * @see planet.commonapi.Node#isAlive()
     */
    public boolean isAlive() {
        return alive;
    }

    /**
     * Gets the successor list.
     * @param max Maximum number of successor to be returned.
     * @return A vector with a <b>max</b> as maximum.
     * @see planet.commonapi.Node#getSuccList(int)
     */
    public Vector getSuccList(int max) {
        //NOTE: only exists one successor
        return successors;
    }

    /**
     * Always return null.
     * @param key Key to be found
     * @param max Maximum number of nodehandles to be returned.
     * @param safe Shows if the connections have to be safe.
     * @return Always null.
     * @see planet.commonapi.Node#localLookup(planet.commonapi.Id, int, boolean)
     */
    public Vector localLookup(Id key, int max, boolean safe) {
        return null;
    }

    /**
     * Always return null.
     * @param max Maximum number of neighbors to be returned.
     * @return Always null.
     * @see planet.commonapi.Node#neighborSet(int)
     */
    public Vector neighborSet(int max) {
        return null;
    }

    /**
     * Always return null.
     * @param key Key to be replicated.
     * @param maxRank Maximum number of nodes where to save the replicas.
     * @return Always null.
     * @see planet.commonapi.Node#replicaSet(planet.commonapi.Id, int)
     */
    public Vector replicaSet(Id key, int maxRank) {
        return null;
    }

    /**
     * This methods ALWAYS return false. It is not implemented yet.
     * <br><br>
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
     * @see planet.commonapi.Node#range(planet.commonapi.NodeHandle,
     *      planet.commonapi.Id, planet.commonapi.Id, planet.commonapi.Id)
     */
    public boolean range(NodeHandle node, Id rank, Id leftKey, Id rightKey) {
        return false;
    }

    /**
     * Build the edges for its sucessor and predecessor links.
     * @param resultName Result name to be used.
     * @param edgeCollection Edge collection where to add all the new ones.
     * @param constraint Constraint to verify the addition of the edges.
     * @see planet.commonapi.Node#buildEdges(java.lang.String, java.util.Collection, planet.commonapi.results.ResultsConstraint)
     */
    public void buildEdges(String resultName, Collection edgeCollection, ResultsConstraint constraint) {
        if (edgeCollection == null || constraint == null) return;
        
        //neighbours (successors and predecessors)
        ResultsEdge e = buildNewEdge(resultName,id,successor.getId(),"#0000FF");
        if (e!=null)
            if (constraint.isACompliantEdge(e)) edgeCollection.add(e);
        e = buildNewEdge(resultName,id,predecessor.getId(),"#0000FF");
        if (e!=null)
            if (constraint.isACompliantEdge(e)) edgeCollection.add(e);
    }

    /**
     * @return All the local links.
     * @see planet.commonapi.Node#getAllLinks()
     */
    public Set getAllLinks() {
        return this.links;
    }
    
    /**
     * Process the local incoming messages.
     * @param actualStep Actual step in the simulation process.
     * @return Always false, whenever the node always is stabilized and don't 
     * require more steps for its stabilization.
     * @see planet.commonapi.Node#process(int)
     */
    public boolean process(int actualStep) {
        //always must be invoked at the beginning
        super.process(actualStep);
        
        //here starts your node process
        if (Properties.overlayWithBehaviours)
        {
            //you may use this structure when your implemented overlay use behaviours
            dispatchMessagesWithBehaviours();
        } else {
            //you may use this structure when your implemented overlay don't use behaviours
            dispatchMessages();
        }
        
        //always must be invoked at the end
        invokeByStepToAllApplications();
        return false;
    }
    
    /** Sets the new Id.
     * @param newId New Id.
     * @return This instance after it has been updated.
     * @throws InitializationException if any error has occurred.
     * @see planet.commonapi.Node#setValues(planet.commonapi.Id)
     */
    public Node setValues(Id newId) throws InitializationException {
        super.setValues(newId);
        //this overlay doesn't require any other action. 
        return this;
    }
    
    /* ****************** SPECIFIC OVERLAY METHODS *************************/
    
    /**
     * Updates the node predecessor.
     * @param pred The new node predecessor.
     */
    public void setPredecessor(NodeHandle pred)
    {
        if (predecessor!=null)
            links.remove(predecessor);
        predecessor = pred;
        links.add(pred);
    }
    
    /**
     * Updates the node successor.
     * @param succ The new node successor.
     */
    public void setSuccessor(NodeHandle succ)
    {
        if (successor!=null)
        {
            links.remove(successor);
            successors.remove(0);
        }
        successor = succ;
        links.add(succ);
        successors.add(succ);
    }
    
    /**
     * Dispatch all incoming messages of applicaion level.
     */
    private void dispatchMessages()
    {
        while (hasMoreMessages())
        {
            RouteMessage msg = nextMessage();
            //Only application level messages are delivered
            dispatchDataMessage(msg,REQUEST,REFRESH);
        }
    }
    
    /**
     * Dispatch all incoming messages of application level using behaviours.
     *
     */
    private void dispatchMessagesWithBehaviours()
    {
        while (hasMoreMessages()) {
            RouteMessage msg = nextMessage();
            try {
                 behPool.onMessage(msg, this);
                 GenericFactory.freeMessage(msg);
            } catch (planet.commonapi.behaviours.exception.NoSuchBehaviourException e) {
                throw new Error("An applicable behaviour is not found");
            } catch (planet.commonapi.behaviours.exception.NoBehaviourDispatchedException d) {
                throw new Error("An applicable behaviour is not found");
            }
        }
    }
    
    /* END ************** SPECIFIC OVERLAY METHODS *************************/
  
    
    /**
     * @return The string representation of this node.
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
        return "<TrivialNode id=\""+id+"\">";
    }
}
