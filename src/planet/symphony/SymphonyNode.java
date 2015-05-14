package planet.symphony;
import java.util.Collection;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;

import planet.commonapi.Id;
import planet.commonapi.Message;
import planet.commonapi.Node;
import planet.commonapi.NodeHandle;
import planet.commonapi.RouteMessage;
import planet.commonapi.exception.InitializationException;
import planet.commonapi.results.ResultsConstraint;
import planet.generic.commonapi.behaviours.BehavioursPatternImpl;
import planet.generic.commonapi.factory.GenericFactory;
import planet.results.LinkStateResults;
import planet.simulate.Globals;
import planet.simulate.Logger;
import planet.simulate.Results;
import planet.symphony.messages.JoinMessage;
import planet.symphony.messages.NeighbourMessagePool;
import planet.util.Properties;
import planet.util.timer.TimerTaskImpl;

/**
 * 
 * This node is an implementation of the Symphony overlay. 
 * 
 * @author <a href="mailto:heliodoro.tejedor@estudiants.urv.es">Helio Tejedor</a>
 * @author <a href="mailto:marc.sanchez@estudiants.urv.es">Marc Sanchez</a>
 * @author <a href="mailto:Ruben.Mondejar@estudiants.urv.es">Ruben Mondejar</a>
 * @author <a href="mailto:cpairot@etse.urv.es">Carles Pairot</a>
 * @author <a href="mailto:jordi.pujol@estudiants.urv.es">Jordi Pujol</a>
 */

public class SymphonyNode
	extends planet.generic.commonapi.NodeImpl 
{
	
	/* ******************  CONSTANTS FOR MODE OF ROUTEMESSAGE *******/
	
	/**
	 * Mode value: Defines the start of communication that requires 
	 * response (REPLY). 
	 */
	public final static int REQUEST					= 0;
	/**
	 * Mode value: Defines the response of a communication.
	 */
	public final static int REPLY					= 1;
	/**
	 * Mode value: Defines a message's mode that requires only
	 * communication on one way.
	 */
	public final static int REFRESH  				= 2;		
	
	/* END ******************  CONSTANTS FOR MODE OF ROUTEMESSAGE *******/
	
	/* ******************  CONSTANTS FOR TYPE OF ROUTEMESSAGE *******/
	/**
	 * Type value: First message that is send by any node to get its
	 * inmediate successor.
	 */
	 public final static int QUERY_JOIN 			 = 0;
     public final static int SET_INFO   			 = 1;
     public final static int QUERY_CONNECT 			 = 2;
     public final static int ACCEPT_CONNECT 		 = 3;
     public final static int CANCEL_CONNECT 		 = 4;
     public final static int CLOSE_LONG_CONNECT 	 = 5;
     public final static int CLOSE_NEIGHBOUR_CONNECT = 6;
     /**
      * Type value: It identifies that this message contains an application 
      * level Message. 
      */
     public final static int DATA                     = 7;    
     
	/* END ******************  CONSTANTS FOR TYPE OF ROUTEMESSAGE *******/
	
	/* ******************  CONSTANTS FOR TYPE/MODE OF ROUTEMESSAGE *******/
	/**
	 * This String contains a string representation of each type
	 * value of the RouteMessage.
	 */
	public final static String[] TYPES = {
			"QUERY_JOIN", "SET_INFO", 
			"QUERY_CONNECT", "ACCEPT_CONNECT", 
			"CANCEL_CONNECT", "CLOSE_LONG_CONNECT", 
			"CLOSE_NEIGHBOUR_CONNECT", "DATA"};
	/**
	 * This String contains a string representation of each mode
	 * value of the RouteMessage.
	 */
	public final static String[] MODES = {"REQUEST","REPLY","REFRESH" };
	
	/* END ******************  CONSTANTS FOR TYPE/MODE OF ROUTEMESSAGE *******/
	
	
	
	/**
	 * To build new Id to find a new long distance.
	 */
	private static Random r = new Random();

	
	private List incommingSet;
	private List outcommingSet;
	public SortedKList neighbourSet; // with order !!!
    
	private double n;
	
	/* UpdateOutcommingList */
	public int retriesNewLongDistance;
    private boolean requestedNewLongDistance;
	
	public boolean alive;
	public boolean fixedNeighbours; 
    
    /* When the flag 'stabilized' is activated, makes F*24 stabilization processes
     * to guarantee that the neighbour set is the most possible correct.
     */
    public boolean statisticStabilized;
    public int statisticStabilizationSteps;
    
    private planet.commonapi.behaviours.BehavioursPool behPool; 
    
    private boolean modifiedNeighbours = true;
    
    /* only for temporal uses on the methods*/
    private Vector stabilizeVector = null;
    private NodeHandle stabilizeNH = null;
    private Vector estimationVector = null;
    private NodeHandle estimationNH = null;
    
    
    /**
     * Gets the maximum number of successors per node.
     * @return the maximum number of successors per node.
     */
    public static int getSuccessorsNumber()
    {
        return ((SymphonyProperties)Properties.overlayPropertiesInstance).maxSuccessorList;
    }
    
    /**
     * Gets the maximum number of long distance links per node.
     * @return the maximum number of long distance links per node.
     */
    public static int getLongDistanceNumber()
    {
        return ((SymphonyProperties)Properties.overlayPropertiesInstance).maxLongDistance;
    }
    
    
    /**
     * Initialize the internal data structure. 
     * @throws InitializationException
     */
    public SymphonyNode() throws InitializationException {
        super();
        alive = true;
        outcommingSet = new Vector(SymphonyNode.getLongDistanceNumber(),1);
        incommingSet = new Vector(SymphonyNode.getLongDistanceNumber(),1);
        fixedNeighbours = false;
        statisticStabilized = false;
        modifiedNeighbours = true;
        statisticStabilizationSteps = SymphonyNode.getSuccessorsNumber()*24;
        behPool = GenericFactory.getDefaultBehavioursPool();

        // Stabilize Timer
        setTimer(new StabilizeTask(),  ((SymphonyProperties)Properties.overlayPropertiesInstance).stabilizeSteps,
                ((SymphonyProperties)Properties.overlayPropertiesInstance).stabilizeSteps);
    }
	
    /**
     * Return the endpoints, based on (key,value) structure, where
     * the <b>key</b> is the Application name, and the <b>value</b> the own EndPoint instance.
     * @return The Hashtable with all the endpoint instances.
     */
	public Hashtable getEndPoints() {
		return this.endpoints;
	}
	
    /**
     * Builds a RouteMessage with the specified data.
     * @param from Communication source.
     * @param to   Communitacion destination.
     * @param type Communication type.
     * @param mode Communication mode, from the specified type.
     * @param m    Data to be sent in this communication.
     * @return The built RouteMessage or null if any error has ocurred.
     */
	public RouteMessage buildMessage(NodeHandle from, NodeHandle to, int type, int mode, Message m) {
		return buildMessage(from, to, route(to), type, mode, m, GenericFactory.generateKey());
	}
	
    /**
     * Builds a new RouteMessage with the specified data.
     * @param from Communication source.
     * @param to   Communication destination.
     * @param type Communication type.
     * @param mode Communication mode, from the specified type.
     * @param m    Data to be sent in this communication.
     * @param key  Unique communication key.
     * @return The built RouteMessage or null if any error has ocurred.
     */
	public RouteMessage buildMessage(NodeHandle from, NodeHandle to, int type, int mode, Message m, String key) {
		return buildMessage(from, to, route(to), type, mode, m, key);
	}
	
    /**
     * Builds a nw RouteMessage with the specified data.
     * @param from    Communication source.
     * @param to      Communication destination.
     * @param nextHop Communication next hop.
     * @param type    Communication type.
     * @param mode    Communication mode, from the specified type.
     * @param m       Data to be sent in this communication.
     * @return        The built RouteMessage or null if any error has ocurred.
     */
	public RouteMessage buildMessage(NodeHandle from, NodeHandle to, NodeHandle nextHop, int type, int mode, Message m) {
		return buildMessage(from, to, nextHop, type, mode, m, GenericFactory.generateKey());
	}
	
    /**
     * Builds a RouteMessage with the specified data.
     * @param from    Communication source.
     * @param to      Communication destination.
     * @param nextHop Communication next hop.
     * @param type    Communication type.
     * @param mode    Communication mode, from the specified type.
     * @param m       Data to be sent in this communication.
     * @param key     Unique communication key.
     * @return        The built RouteMessage or null if any error has ocurred.
     */
	public RouteMessage buildMessage(NodeHandle from, NodeHandle to, NodeHandle nextHop, int type, int mode, Message m, String key) {
		RouteMessage msg = null;
		try {
		    msg = GenericFactory.getMessage(key,from,to,nextHop,m,type,mode,null);
			//planet.results.LinkStateResults.newMessage(msg);
			//planet.results.LinkStateResults.updateOutcoming(this.id);
		} catch (InitializationException e) {
			key = null;
			Logger.log("ERROR: Cannot get a RouteMessage of MessagePool\n" + e.getMessage(), Logger.ERROR_LOG);
            GenericFactory.freeMessage(msg);
            msg = null;
		}
		return msg;
	}
    
    /**
     * Builds a new RouteMessage with all the values appeared in <b>toCopy</b>, and
     * the specified <b>nextHop</b>.
     * @param toCopy Message to be cloned.
     * @param nextHop Next hop in the route.
     * @return A valid RouteMessage or null if there are any error.
     */
    public RouteMessage buildMessage(RouteMessage toCopy, NodeHandle nextHop)
    {
        RouteMessage msg = null;
        try {
            msg = GenericFactory.getMessage(GenericFactory.generateKey()/*toCopy.getKey()*/,toCopy.getSource(),toCopy.getDestination(),nextHop,toCopy.getMessage(),toCopy.getType(),toCopy.getMode(),toCopy.getApplicationId());
            //planet.results.LinkStateResults.newMessage(msg);
            //planet.results.LinkStateResults.updateOutcoming(this.id);
        } catch (InitializationException e)
        {
            Logger.log("ERROR: Cannot get a RouteMessage of MessagePool\n" + e.getMessage(), Logger.ERROR_LOG);
            GenericFactory.freeMessage(msg);
            msg = null;
        }
        return msg;
    }
	
    /**
     * Build and send a new RouteMessage with the specified data.
     * @param from Communication source.
     * @param to   Communication destination.
     * @param type Communication type.
     * @param mode Communication mode, from the specified type.
     * @param m    Data to be sent in this communication.
     * @return     The generated unique key for this communication or null if cannot be sent.
     */
    private String sendMessage(NodeHandle from, NodeHandle to, int type, int mode, Message m) {
		return sendMessage(from, to, route(to), type, mode, m, GenericFactory.generateKey());
	}
	
    /**
     * Build and send a new RouteMessage with the specified data.
     * @param from Communication source.
     * @param to   Communication destination.
     * @param type Communication type.
     * @param mode Communication mode, from the specified type.
     * @param m    Data to be sent in this communication.
     * @param key  Unique communication key.
     * @return     The same key or null if cannot be sent.
     */
	private String sendMessage(NodeHandle from, NodeHandle to, int type, int mode, Message m, String key) {
		return sendMessage(from, to, route(to), type, mode, m, key);
	}
	
    /**
     * Build and send a new RouteMessage with the specified data.
     * @param from    Communication source.
     * @param to      Communication destination.
     * @param nextHop Communication next hop.
     * @param type    Communication type.
     * @param mode    Communication mode, from the specified type.
     * @param m       Data to be sent in this communication.
     * @return        The generated unique key for this communication or null if 
     *                cannot be sent.
     */
	private String sendMessage(NodeHandle from, NodeHandle to, NodeHandle nextHop, int type, int mode, Message m) {
		return sendMessage(from, to, nextHop, type, mode, m, GenericFactory.generateKey());
	}
	
    /**
     * Build and send a new RouteMessage with the specified data.
     * @param from    Communication source.
     * @param to      Communication destination.
     * @param nextHop Communication next hop.
     * @param type    Communication type.
     * @param mode    Communication mode, from the specified type.
     * @param m       Data to be sent in this communication.
     * @param key     Unique communication key.
     * @return        The same key or null if cannot be sent.
     */
	private String sendMessage(NodeHandle from, NodeHandle to, NodeHandle nextHop, int type, int mode, Message m, String key) {
	    String toReturn = key;
        RouteMessage msg = buildMessage(from,to,nextHop,type,mode,m,key);
        if (msg!=null)
        {
			//planet.results.LinkStateResults.newMessage(msg);
			//planet.results.LinkStateResults.updateOutcoming(this.id);
			sendMessage(msg);
		}
        else toReturn = null;
		return toReturn;
	}
	
    /**
     * Resends the RouteMessage to the specified <b>msg</b> next hop. 
     * @param msg RouteMessage to be resent.
     * @return The key of the RouteMessage or null if cannot be sent.
     */
	private String resendMessage(RouteMessage msg) {
		return resendMessage(msg, msg.getNextHopHandle());
	}
	
    /**
     * Resends the RouteMessage to the specified <b>nextHop</B>.
     * @param msg     RouteMessage to be resent.
     * @param nextHop Communication next hop.
     * @return        The routemessage key or null if cannot be sent.
     */
	private String resendMessage(RouteMessage msg, NodeHandle nextHop) {
		String key = null;
		key = msg.getKey();
		msg.setNextHopHandle(nextHop);
		//planet.results.LinkStateResults.newMessage(msg);
		//planet.results.LinkStateResults.updateOutcoming(this.id);
		if (!sendMessage(msg)) key = null;
		return key;
	}
	
    /**
     * Closes all stale long connections appeared in the <b>toRemoveFrom</b> list.
     * @param toRemoveFrom Long connections list.
     */
    private void closeStaleLongConnections(List toRemoveFrom) {
        int index = 0;
        NodeHandle candidate = null;
        
        while (index < toRemoveFrom.size())
        {
            candidate = (NodeHandle) toRemoveFrom.get(index);   
            if (neighbourSet.contains(candidate))
            {
                sendMessage(buildMessage(getLocalHandle(), candidate, 
                        SymphonyNode.CLOSE_LONG_CONNECT, SymphonyNode.REFRESH, null));
                toRemoveFrom.remove(index);
            } else
                index ++;
        }
    } 
    
    /**
     * Inform to stale neighbour that its connection has been closed.
     * @param closed
     */
    private void sendClosedNeighbour(Vector closed)
    {
        int size = closed.size();
        NodeHandle current = null;
        for (int i = 0; i < size; i++)
        {
            current = (NodeHandle)closed.get(i);
            sendMessage(nodeHandle, current, current, CLOSE_NEIGHBOUR_CONNECT, REFRESH, NeighbourMessagePool.getMessage(neighbourSet.getFarthestNeighbours()));
        }
    }
    
    /**
     * Add all NodeHandle in the <b>c</b> Collection to the neighbour set.
     * @param c Collection with neighbours.
     * @param adviceForClosing Send a CLOSE_NEIGHBOUR message to the removed neighbours
     * @return true if any neighbour has been added. false in other case.
     */
	public boolean addToNeighbourSet(java.util.Collection c, boolean adviceForClosing) {
		if (neighbourSet.addAll(c)) {
            modifiedNeighbours = true;
			updateOutcommingList(); 
            /*
             * We must check if incomming or outcomming connections have neighbours
             * because QUERY_CONNECT messages may have been received and accepted 
             * before SET_INFO messages have been arrived.  
             */
            closeStaleLongConnections(incommingSet);
            closeStaleLongConnections(outcommingSet); 
            if (adviceForClosing)
                sendClosedNeighbour(neighbourSet.getRemovedNeighbours());
            fixedNeighbours = false; //require more time to afirm this node is stabilized
            return true;
		}
        fixedNeighbours = (getNeighbourSet().size() - 1) == SymphonyNode.getSuccessorsNumber() * 2;
        return false;
	}
	
    /**
     * Add the <b>neighbour</B> to hte neighbour set.
     * @param neighbour Neighbour NodeHandle to be added.
     */
	public void addToNeighbourSet(NodeHandle neighbour) {
		if (neighbourSet.add(neighbour)) 
        {
            modifiedNeighbours = true;
            updateOutcommingList();
        }
	}
	
    /**
     * Gets the neightbour set.
     * @return The neighbour set.
     */
	public Collection getNeighbourSet() {
		return neighbourSet.getSortedSet();
	}
    
    /**
     * Gets the farthest neighbours.
     * @return The farthest neighbours.
     * @see planet.symphony.SortedKList#getFarthestNeighbours()
     */
    public Collection getFarthestNeighbours()
    {
        return neighbourSet.getFarthestNeighbours();
    }
	
    /**
     * Test if the NodeHandle <b>o</b> is in the neighbour set.
     * @param o The NodeHandle to be test.
     * @return true if <b>o</b> is in the set.
     */
	public boolean neighbourSetContains(NodeHandle o) {
		return neighbourSet.contains(o);
	}
	
    /**
     * Removes the <b>o</b> from the neighbourSet
     * @param o The NodeHandle to be removed.
     * @return true if it is removed or false in other case.
     */
	public boolean removeNeighbour(NodeHandle o) {
		return neighbourSet.remove(o);
	}
	
	/**
     * Returns the NodeHandle of node successor.
	 * @return The NodeHandle of node successor.
	 * @see planet.commonapi.Node#getSucc()
	 */
	public NodeHandle getSucc() {
		NodeHandle succ = (NodeHandle) neighbourSet.getFirstSucc();
		if (succ == null) return nodeHandle;
		return succ;
	}
	
    /**
     * Returns the NodeHandle of node predecessor.
     * @return The NodeHandle of node predecessor.
     * @see planet.commonapi.Node#getPred()
     */
	public NodeHandle getPred() {
		NodeHandle pred = (NodeHandle) neighbourSet.getFirstPred();
		if (pred == null) return nodeHandle;
		return pred;
	}
	
    /**
     * Get the outcomming set of long distance connections.
     * @return the outcomming set of long distance connections.
     */
	public List getOutcommingSet() {
		return this.outcommingSet;
	}
    
    /**
     * Get the incomming set of long distance connections.
     * @return the incomming set of long distance connections.
     */
	public List getIncommingSet() {
		return this.incommingSet;
	}
		
    /**
     * This node joins to the overlay network by the bootstrap node with NodeHandle <b>bootstrap</b>.
     * @param bootstrap NodeHandle of the bootstrap node.
     * @see planet.commonapi.Node#join(planet.commonapi.NodeHandle)
     */
  	public void join(NodeHandle bootstrap) {
		incommingSet.clear();
		outcommingSet.clear();
		if (id.equals(bootstrap.getId())) {
			n = 1;
			fixedNeighbours = true;
		} else {
			n = 0;
			sendMessage(nodeHandle, bootstrap, bootstrap, QUERY_JOIN, REFRESH, new JoinMessage(nodeHandle, bootstrap));
			fixedNeighbours = false;
		}
	}
	
    /**
     * Leaves the node from the overlay network.
     * 
     * @see planet.commonapi.Node#leave()
     */
	public void leave() {
		Iterator[] c = { 
				incommingSet.iterator(),
				outcommingSet.iterator(),
				neighbourSet.getNeighbourSet().iterator(),
		};
	
		int[] msgtype = {
			CLOSE_LONG_CONNECT,
			CLOSE_LONG_CONNECT,
			CLOSE_NEIGHBOUR_CONNECT,
		};
		for(int i = 0; i < c.length; i++) {
			Iterator it = c[i];
			while (it.hasNext()) {
				NodeHandle current = (NodeHandle) it.next();
				sendMessage(nodeHandle, current, current, msgtype[i], REFRESH, null);
			}
		}
		this.fixedNeighbours = true;
		this.alive = false;
	}
	
    /**
     * Returns a RouteMessage to be sent to acquire a new long distance.
     * @return a RouteMessage to be sent to acquire a new long distance.
     */
	public RouteMessage getNewLongDistance() {
        if (requestedNewLongDistance) return null;

        requestedNewLongDistance = true; //this flag ensures only one QUERY_CONNECT per step
		int retries = 10;
	
		while (true) {
			SymphonyId symId = (SymphonyId) id;
			double x = symId.getDoubleValue() + Math.exp(Math.log((double) n) * (r.nextDouble() - 1.0));
			int i = (int) x;
			x -= i;
			Id xid = new SymphonyId().setValues(x);
			NodeHandle succ = getSucc();
			NodeHandle pred = getPred();
			if (!xid.between(pred.getId(), id) && !xid.between(id, succ.getId())) 
				try {
                    NodeHandle xidNH = GenericFactory.buildNodeHandle(xid, true);
					return buildMessage(nodeHandle, xidNH, route(xidNH), QUERY_CONNECT, REFRESH, null);
				} catch(InitializationException e) {
					Logger.log("[" + id + "] cannot build a NodeHandle for a RouteMessage", Logger.ERROR_LOG);
				}
			else if (retries > 1) retries--;
			else {
					//on this case, cannot find a long distance link
					return null;
				}
		}
	}

    /**
     * Evaluates if its network context has changed. In afirmative case, a
     * new long distance query is sent.
     */
	private void updateOutcommingList() {
		double nnew = estimation();
	
		boolean recalc = true;
        if ( n!=0 )
        {
			double ratio = (double) nnew / (double) n;
			if ( (ratio >= 0.5) && (ratio <= 2) ) {
				recalc = false;
			}
		}
		if (recalc) {
			n = nnew;
			retriesNewLongDistance = 0;
			RouteMessage newLink = getNewLongDistance();
			if (newLink == null) return;
            sendMessage(newLink);
		}
	}

    /**
     * Makes the network size estimation.
     * @return The estimated network size.
     */
	private double estimation() {
		double length = 0.0;
		int cnt = 0;
        double idDouble = ((SymphonyId) id).getDoubleValue();
        double firstPred;
        double temp;
        
        estimationNH = (NodeHandle)neighbourSet.getFirstSucc();
        if (estimationNH != null)
        {
            cnt++;
            temp = ((SymphonyId)estimationNH.getId()).getDoubleValue();
            temp = temp - idDouble;
            length += (temp<0.0)?temp+1.0:temp;
        }
        
        estimationNH = (NodeHandle)neighbourSet.getFirstPred();
        if (estimationNH != null)
        {
            firstPred = ((SymphonyId)estimationNH.getId()).getDoubleValue();
            cnt++;
            temp = idDouble - firstPred;
            length += (temp<0.0)?temp+1.0:temp;

            estimationNH = (NodeHandle)neighbourSet.getSecondPred();
            if (estimationNH != null)
            {
                cnt++;
                temp = ((SymphonyId)estimationNH.getId()).getDoubleValue();
                temp = firstPred - temp;
                length += (temp<0.0)? temp + 1.0 : temp;
            }
        }
        if (cnt == 0) return 1.0;
		else return ((double) cnt) / length;
	}

    /**
     * Dispatch any incomming RouteMessage and update the local state or
     * is resent to other node.
     * @param msg Incomming RouteMessage to be treat.
     */
	public void dispatcher(RouteMessage msg) {
		/* Broadcast still unavailable */
		NodeHandle src = msg.getSource();
		NodeHandle dest = msg.getDestination();
		
		switch(msg.getMode()) {
		    // Mode = REQUEST, Mode = REQUEST
		  	case REQUEST: 
            case REFRESH: 
                Logger.log("Uncatched message Type [" + msg.getType() + "]", Logger.ERROR_LOG);
                GenericFactory.freeMessage(msg);
                break;
		    // Mode = ERROR
		  	case Globals.ERROR:
				switch(msg.getType()) {
					case QUERY_CONNECT:
						RouteMessage newLink = getNewLongDistance();
						if (newLink == null) return;
						else sendMessage(newLink);
						break;
					case ACCEPT_CONNECT:
						NodeHandle srcHandle = msg.getSource();
						incommingSet.remove(srcHandle);
                        GenericFactory.freeMessage(msg);
						break;
					default:
						Logger.log("Uncatched message Mode [" + msg.getMode() + "]", Logger.ERROR_LOG);
				}
				break;
		  	default: Logger.log("Uncatched message Mode [" + msg.getMode() + "]", Logger.ERROR_LOG);
		}
	}

    /**
     * This method is invoked to each simulation step to process the internal data,
     * and all incomming RouteMessage.
     * @param actualStep The current simulation step.
     * @return true if the node is stabilized or false in other case.
     * @see planet.commonapi.Node#process(int)
     */
	public boolean process(int actualStep) {
        requestedNewLongDistance = false; //permits only one QUERY_CONNECT by step
		super.process(actualStep);
		while (hasMoreMessages()) {
			//planet.results.LinkStateResults.updateIncoming(this.id);
			RouteMessage msg = nextMessage();
			try {
				 behPool.onMessage(msg, this);
                 GenericFactory.freeMessage(msg);
			} catch (planet.commonapi.behaviours.exception.NoSuchBehaviourException e) {
				dispatcher(msg);
			} catch (planet.commonapi.behaviours.exception.NoBehaviourDispatchedException d) {
				dispatcher(msg);
			}
		}
        if (fixedNeighbours)
        {
            if (statisticStabilizationSteps>0)
                statisticStabilizationSteps--;
            statisticStabilized = statisticStabilizationSteps==0;
        } else
        {
            statisticStabilized = false;
            statisticStabilizationSteps = SymphonyNode.getSuccessorsNumber()*24;
        }

		invokeByStepToAllApplications();
        
        return !isStabilized(); 
	}
	
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
	public boolean isLocalMessage(RouteMessage msg) {
		NodeHandle recipient = msg.getDestination();
		return  recipient.equals(this.nodeHandle) 
				|| (getPred() != null && recipient.getId().betweenE(getPred().getId(), this.id));
	}

    /**
     * This method returns the internal data in a Hashtable, where the key
     * is a String with the field name, and the value, the related object.
     * @return A Hashtable with all internal information.
     * @see planet.generic.commonapi.NodeImpl#getInfo()
     */
	public Hashtable getInfo() {
		Hashtable info = new Hashtable();
		info.put("neighbourSet", neighbourSet.getNeighbourSet());
		info.put("estimation", new Double(n));
		return info;
	}
    
	/**
	 * @param appId String that includes the application identification.
	 * @param to Node that must receive this message. If cannot represents
	 * a real node.
	 * @param nextHop Node that must receive this message as first hop.
	 * @param msg Message to be sent.
	 * If it is null, the message must be routed.
	 */
	public void routeData(String appId,
			NodeHandle to, NodeHandle nextHop, Message msg) {
		NodeHandle hop = nextHop;
		if (hop == null) {
			hop = route(to);
			if (hop == null) Logger.log("route return null!", Logger.ERROR_LOG);
		}
        RouteMessage temp = buildMessage(nodeHandle,to,hop,DATA,REQUEST,msg,GenericFactory.generateKey());
        temp.setApplicationId(appId);
		sendData(temp);
	}
	
    /**
     * Shows in the standard out all internal routing information.
     * @see planet.commonapi.Node#printNode()
     */
	public void printNode() {
		StringBuffer sb = new StringBuffer();
		sb.append("________________________________________________");
		sb.append("\nId [" + id + "] >> \n\n");
		sb.append("\nRole>    " + (role?BehavioursPatternImpl.ROLE_GOOD:BehavioursPatternImpl.ROLE_BAD));
		sb.append("\nN>       " + n);
		Iterator it;
		
		sb.append("\nOutSet>  ");
		it = outcommingSet.iterator();
		while (it.hasNext()) {
			NodeHandle current = (NodeHandle) it.next();
			sb.append(current.toString() + ",");
		}
		
		sb.append("\nInSet>   ");
		it = incommingSet.iterator();
		while (it.hasNext()) {
			NodeHandle current = (NodeHandle) it.next();
			sb.append(current.toString() + ",");
		}
		sb.append("\nNeighbourSet> ");
		it = neighbourSet.iterator();
		while (it.hasNext()) {
			NodeHandle current = (NodeHandle) it.next();
			sb.append(current.toString() + ",");
		}
        sb.append("\noutcomming> "+this.outMessages());
        sb.append("\nincomming > "+this.inMessages());
		sb.append("\n________________________________________________");
		System.out.println(sb.toString());
	}

    /**
     * Shows a brief description of the internal routing state.
     * @see planet.commonapi.Node#prettyPrintNode()
     */
	public void prettyPrintNode() {
	    StringBuffer sb = new StringBuffer();
		sb.append("________________________________________________");
		sb.append("\nId [" + id + "] >> \n\n");
		sb.append("\nRole>    " + (role?BehavioursPatternImpl.ROLE_GOOD:BehavioursPatternImpl.ROLE_BAD));
		sb.append("\nN>       " + n);
		sb.append("\n________________________________________________");
		System.out.println(sb.toString());
	}
	
	/**
     * Shows if the node is already alive.
	 * @return true if the node is alive. false in other case.
	 * @see planet.commonapi.Node#isAlive()
	 */
	public boolean isAlive() {
		return alive;
	}

    /**
     * Having a node destination <b>to</b>, calculates the shortest path to it,
     * passing by the <b>a</b> node or <b>b</b> node.
     * @param to Destination node.
     * @param a  The first candidate to arrive to the destination node.
     * @param b  The second candidate to arrive to the destination node.
     * @return The <b>a</b> candidate if has the shortest path to <b>to</b> node,
     * or <b> in other case.
     */
	private NodeHandle bestOf(NodeHandle to, NodeHandle a, NodeHandle b) {
		if (a == null)
			return b;			
		if (b == null)
			return a;
		double lena = Math.abs(((SymphonyId) to.getId()).getDoubleValue() - ((SymphonyId)a.getId()).getDoubleValue());
		double lenb = Math.abs(((SymphonyId) to.getId()).getDoubleValue() - ((SymphonyId)b.getId()).getDoubleValue());
		if (lena <= lenb)
			return a;
		else
			return b;
	}
    
    /**
     * Having a node destination <b>to</b>, calculates the shortest path to it,
     * passing by the <b>a</b> node or <b>b</b> node.
     * @param to Destination node.
     * @param a  The first candidate to arrive to the destination node.
     * @param it NodeHandle iterator with another candidates to arrive to the
     * destination node.
     * @return The NodeHandle with the shortest path to destination node.
     */
    private NodeHandle bestOf(NodeHandle to, NodeHandle a, Vector values) {
        NodeHandle candidate = a;
        for (int i = 0; i < values.size(); i++)
        {
            candidate = bestOf(to, candidate, (NodeHandle) values.get(i));
        }
        return candidate;
    }
	
    /**
     * Returns the NodeHandle with the shortest path to the destination node,
     * using all internal routing information.
     * @param to Destination node.
     * @return   The best node to route any RouteMessage to the node <b>to</b>.
     */
	public NodeHandle route(NodeHandle to) {
		if (to.getId().between(id, getSucc().getId())) {
			return getSucc();
		}

		NodeHandle candidate = null;

        candidate = bestOf(to, candidate, (Vector)neighbourSet.getSortedSet());
		candidate = bestOf(to, candidate, (Vector)outcommingSet);
		candidate = bestOf(to, candidate, (Vector)incommingSet);
		
		return candidate;
	}

    /**
     * Sends a RouteMessage with Application level data.
     * @param message RouteMessage to be sent with Application level data.
     */
	private void sendData(RouteMessage message) {
		//planet.results.LinkStateResults.newMessage(message);
		//planet.results.LinkStateResults.updateOutcoming(this.id);
        sendMessage(message);
        Results.incTraffic();
	}

	/**
     * Shows a String representation of this node, showing only its Id.
	 * @return A String representation of this node.
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "SymphonyNode(" + id + ")";
	}
	
    /**
     * Informs if this node is stabilized.
     * @return true if the node is stabilized. false in other case.
     */
	public boolean isStabilized() {
		return fixedNeighbours && statisticStabilized; 
	}
	
    /**
     * Sends to any neighbour the local neighbours. It permits update any new
     * acquisition as neighbour. This method is invoked periodically to update
     * this information.
     */
	private void stabilize() {
        if (!modifiedNeighbours) return;
		stabilizeVector = (Vector)neighbourSet.getNeighbourSet();
        int length = stabilizeVector.size();
        for (int i=0; i < length; i++)
        {
			stabilizeNH = (NodeHandle) stabilizeVector.get(i);
			RouteMessage msg = buildMessage(nodeHandle, stabilizeNH, stabilizeNH, SymphonyNode.SET_INFO,
					  SymphonyNode.REFRESH, NeighbourMessagePool.getMessage(neighbourSet.getSortedSet()));
			//LinkStateResults.newMessage(msg);
			//LinkStateResults.updateOutcoming(id);
            sendMessage(msg);
		}
        modifiedNeighbours = false;
	}
	
	/**
	 * Simple TimerTask that invoke stabilize() Node method.
	 * 
	 * @author <a href="mailto: marc.sanchez@estudiants.urv.es">Marc Sanchez</a>
	 *         Date: 07/05/2004
	 */
	public class StabilizeTask extends TimerTaskImpl {
		/**
		 * Initialize this StabilizeTask.
		 */
		public StabilizeTask() {
			super(true);
		}
		/**
		 * Invoke stabilize() method of Node.
		 * 
		 * @see java.util.TimerTask#run()
		 */
		public void run() {
			super.run();
			if (!isFinished()) {
				stabilize();
			}
		}
	}
	
	
	
    /**
     * This method throws a NoSuchMethodError, because the broadcast algorithm
     * is not implemented yet.
     * @see planet.commonapi.Node#broadcast(java.lang.String,
     *      planet.commonapi.NodeHandle, planet.commonapi.NodeHandle,
     *      planet.commonapi.Message)
     * @param appId Application identification.
     * @param to Source of the message.
     * @param nextHop NextHop of the message.
     * @param msg Message to be send as broadcast.
     */
	public void broadcast(String appId, NodeHandle to, NodeHandle nextHop,
			Message msg) {
	    throw new NoSuchMethodError("Broadcast is not implemented yet.");
	}
	
    /**
     * The leave protocol is the same as in leave case.
     * @see planet.commonapi.Node#fail()
     *  
     */
	public void fail() {
		leave();
	}
	
    /**
     * Returns up to <b>max</b> successors of the actual node.
     * @see planet.commonapi.Node#getSuccList(int)
     * @param max Maximum number of successors to return.
     * @return Up to <b>max</b> successors.
     */
	public Vector getSuccList(int max) {
        return neighbourSet.getSuccList(max);
	}
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
     * @see planet.commonapi.Node#localLookup(planet.commonapi.Id, int, boolean)
     */
    public Vector localLookup(Id key, int max, boolean safe) {
        Vector toReturn = new Vector();
        
        TreeSet contacts = new TreeSet(new IdComparer(nodeHandle));
        contacts.addAll(neighbourSet.getNeighbourSet());
        contacts.addAll(incommingSet);
        contacts.addAll(outcommingSet);

        try {
            Set selected = contacts.headSet(GenericFactory.buildNodeHandle(key,true));
            if (selected.size()<=max)
                toReturn.addAll(selected);
            else 
            {
                Iterator it = selected.iterator();
                for (int i=0; i< max; i++)
                {
                    toReturn.add(it.next());
                }
            }
        } catch (Exception e) {}
        
        return toReturn;
    }
    
    /**
	 * Obtains an unordered list of up to max nodes that are neighbors 
	 * of the local node.
	 * @param max Maximum of nodes to return.
	 * @return Neighbor nodes.
     * @see planet.commonapi.Node#neighborSet(int)
     */
	public Vector neighborSet(int max) {
	    int max2 = max/2;
        Vector toReturn = neighbourSet.getPredList(max2);
        toReturn.addAll(neighbourSet.getSuccList(max2));
            
        return toReturn;
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
	 * Returns an ordered set of nodes on which replicas of the object with 
	 * this key can be stored. The maximum number of nodes is maxRank. If
	 * the implementation's maximum replica set size is lower, then its
	 * maximum replica set is returned.
	 * @param key Id for which we find the replica set.
	 * @param maxRank Maximum number of members in replica set.
	 * @return An array of nodes with the replica set.
     * @see planet.commonapi.Node#replicaSet(planet.commonapi.Id, int)
     */
    public Vector replicaSet(Id key, int maxRank) {
        return  neighbourSet.getSuccList(maxRank);
    }
	
	/** 
	 * Returns the NodeHandle closest to <b>id</b>.
	 * @param id Id to find.
	 * @return The NodeHandle closest to <b>id</b>.
	 */
	public NodeHandle getClosestNodeHandle(Id id)
	{
	    try {
	        return route(GenericFactory.buildNodeHandle(id,true));
	    } catch (Exception e)
	    {
	        return (NodeHandle)neighbourSet.getFirstSucc();
	    }
	}
	
	/**
     * Gets all node connections to other nodes as its NodeHandles. The order of
     * NodeHandles is unexpected.
     * 
     * @return A set with all connections as NodeHandles.
     */
    public Set getAllLinks() {
        HashSet conns = new HashSet();

        conns.addAll(outcommingSet);
        conns.addAll(incommingSet);
        conns.addAll(neighbourSet.getNeighbourSet());

        //remove the null element if exists
        conns.remove(null);
        return conns;
    }
	
	/* BEGIN ************************ RESULTS ****************************/
	/**
	 * This method is a callback method used to collect all of the edges of a 
     * graph according to current <b>resultName</b> format.
     * @param resultName Result type name to use.
	 * @param edgeCollection Edge collection where to add new edges.
	 * @param constraint ResultsConstraint for edge selection
	 */
	public void buildEdges(String resultName, Collection edgeCollection, 
            ResultsConstraint constraint) {
		if (edgeCollection == null || constraint == null) return;
		
		//neighbours (successors and predecessors)
		Iterator it2 = this.neighbourSet.iterator();
		addEdges(resultName,it2, constraint, edgeCollection, "#0000FF");
		
		//long distance links
		Iterator[] it = new Iterator[2];
		it[0] = this.incommingSet.iterator();
		it[1] = this.outcommingSet.iterator();
		for (int i = 0; i < 2; i++) {
			addEdges(resultName, it[i],constraint,edgeCollection,"#FF0000");
		}
	}
	/* END ************************ RESULTS ****************************/
    
    
    /** Sets the new Id.
     * @param newId New Id.
     * @return This instance after it has been updated.
     * @throws InitializationException if any error has occurred.
     * @see planet.commonapi.Node#setValues(planet.commonapi.Id)
     */
    public Node setValues(Id newId) throws InitializationException {
        super.setValues(newId);
        //F is the number of successors ==> F predecessors and F successors
        neighbourSet = new SortedKList(new IdComparer(this.nodeHandle), this.nodeHandle, SymphonyNode.getSuccessorsNumber()*2);
        return this;
    }
}
