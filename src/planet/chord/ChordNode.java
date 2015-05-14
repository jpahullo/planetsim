package planet.chord;
import java.math.BigInteger;
import java.util.Collection;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import planet.chord.message.BroadcastMessage;
import planet.chord.message.IdMessage;
import planet.chord.message.NodeMessage;
import planet.chord.message.SuccListMessage;
import planet.commonapi.EndPoint;
import planet.commonapi.Id;
import planet.commonapi.Message;
import planet.commonapi.Node;
import planet.commonapi.NodeHandle;
import planet.commonapi.RouteMessage;
import planet.commonapi.exception.InitializationException;
import planet.commonapi.results.ResultsConstraint;
import planet.commonapi.results.ResultsEdge;
import planet.generic.commonapi.factory.GenericFactory;
import planet.simulate.Globals;
import planet.simulate.Logger;
import planet.simulate.MessageListener;
import planet.simulate.Results;
import planet.util.Properties;
import planet.util.Queue;
import planet.util.timer.TimerTaskImpl;


/**
 * 
 * A Chord node is single entity in the chord network. It extends of the class
 * NodeImpl and specializes following the lookup Chord protocol. Moreover, the
 * stabilization implementation, producing the periodic stabilition events.
 * 
 * @author <a href="mailto:Ruben.Mondejar@estudiants.urv.es">Ruben Mondejar</a>
 * @author <a href="mailto:cpairot@etse.urv.es">Carles Pairot</a>
 * @author <a href="mailto:jordi.pujol@estudiants.urv.es">Jordi Pujol</a>
 * @author <a href="mailto:marc.sanchez@estudiants.urv.es">Marc Sanchez</a>
 *  
 */
public class ChordNode extends planet.generic.commonapi.NodeImpl {
	
	/* ******************  CONSTANTS FOR MODE OF ROUTEMESSAGE *******/
	
	/**
	 * Mode value: Defines the start of communication that requires 
	 * response (REPLY). 
	 */
	public final static int REQUEST		= 0;
	/**
	 * Mode value: Defines the response of a communication.
	 */
	public final static int REPLY		= 1;
	/**
	 * Mode value: Defines a message's mode that requires only
	 * communication on one way.
	 */
	public final static int REFRESH  	= 2;		
	
	/* END ******************  CONSTANTS FOR MODE OF ROUTEMESSAGE *******/
	
	/* ******************  CONSTANTS FOR TYPE OF ROUTEMESSAGE *******/
	/**
	 * Type value: First message that is send by any node to get its
	 * inmediate successor.
	 */
	public final static int FIND_SUCC 	= 0;
	/**
	 * Type value: Start a set of messages that permits to find the
	 * inmediate successor of any new incoming node to the network.
	 */
	public final static int FIND_PRE   	= 1;
	/**
	 * Type value: Informs to the node that receives this message its
	 * new inmediate successor.
	 */
	public final static int SET_SUCC   	= 2;
	/**
	 * Type value: Informs to the node that receive this message its
	 * new inmediate predecessor. This type forces to node to change its
	 * predecessor.
	 */
	public final static int SET_PRE    	= 3;
	/**
	 * Type value: Start a set of messages that permits to find the
	 * inmediate node in charge of any key.
	 */
	public final static int GET_PRE    	= 4;
	/**
	 * Type value: Informs to the node that receive this message its
	 * inmediate predecessor. This type permits to the node to evaluate
	 * if this notification informs really of the predecessor of the node.
	 */
	public final static int NOTIFY     	= 5;
	/**
	 * Type value: Start a set of messages that permits the harvesting of
	 * successors of the node that send originally this message.
	 */
	public final static int SUCC_LIST  	= 6;
	/**
	 * Type value: The message with this type identifies a broadcast message.
	 */
	public final static int BROADCAST  	= 7;
	/**
	 * Type value: It identifies that this message contains an application 
	 * level Message. 
	 */
	public final static int DATA 		= 8;	
	
	/* END ******************  CONSTANTS FOR TYPE OF ROUTEMESSAGE *******/
	
	/* ******************  CONSTANTS FOR TYPE/MODE OF ROUTEMESSAGE *******/
	/**
	 * This String contains a string representation of each of types
	 * and modes values of the RouteMessage.
	 */
	public final static String[] TYPES = {
			"FIND_SUCC", "FIND_PRE", "SET_SUCC", "SET_PRE", "GET_PRE", "NOTIFY",
			"SUCC_LIST", "BROADCAST", "DATA"}; 
	
	/**
	 * This String contains a string representation of each mode
	 * value of the RouteMessage.
	 */
	public final static String[] MODES = {"REQUEST", "REPLY", "REFRESH" };
	
	/* END ******************  CONSTANTS FOR TYPE/MODE OF ROUTEMESSAGE *******/
	
	/**
	 * Return a RouteMessage with the specified values. If there are RouteMessages free,
	 * just build a new one. For generate new instances of RouteMessage will be used
	 * the implementation class that appears in properties file.
	 * @param appId Application Id name.
	 * @param from Source node.
	 * @param to Destination node.
	 * @param nextHop NextHop node.
	 * @return A RouteMessage with the specified values.
	 */
	public static RouteMessage getDataMessage(String appId,
			NodeHandle from, NodeHandle to, NodeHandle nextHop, Message msg) 
				throws InitializationException {
	  	RouteMessage toReturn = GenericFactory.getMessage(GenericFactory.generateKey(), from, to, nextHop, null, DATA, REQUEST, appId);
	  	toReturn.setMessage(msg);
	  	return toReturn;
	}
	
	/**
	 * Return a RouteMessage with the specified values. If there are RouteMessages free,
	 * just build a new one. For generate new instances of RouteMessage will be used
	 * the implementation class that appears in properties file.
	 * @param appId Application Id name.
	 * @param from Source node.
	 * @param to Destination node.
	 * @param nextHop NextHop node.
	 * @return A RouteMessage with the specified values.
	 */
	public static RouteMessage getBroadcastMessage(String appId,
			NodeHandle from, NodeHandle to, NodeHandle nextHop, Message msg) 
				throws InitializationException {
	  	RouteMessage toReturn = GenericFactory.getMessage("",from,to,nextHop,null,BROADCAST,REFRESH,appId);
	  	toReturn.setMessage(msg);
	  	return toReturn;
	}
	
	
	/**
     * Maximum Id value with the current configuration.
	 */
	protected Id MAX;
    /**
     * Index to use within stabilization process onto the finger table.
     */
	protected int nullPointers = 0;
	/**
	 * Detect the number of finger changes only on one simulation step.
	 */
	protected int fingerChanges = 0;
	/**
	 * Number of simulation steps without changes on finger table.
	 */
	protected int stabRate = 0;
    /**
     * Number of bits per key to use as current configuration.
     */
    protected int bitsPerKey = ((ChordProperties)Properties.overlayPropertiesInstance).bitsPerKey;
	/**
	 * Number of simulation steps without changes on finger table, that
	 * represents a real stabilization of this node.
	 */
	protected int realStabilizationRate = 
		((ChordProperties)Properties.overlayPropertiesInstance).stabilizeSteps * 
		((ChordProperties)Properties.overlayPropertiesInstance).bitsPerKey * 2;
	/**
     * The current predecessor in the Chord ring.
	 */
	protected NodeHandle predecessor;
    /**
     * Flag that shows when this node has failed.
     */
	protected boolean hasFailed; // For Failed Case
    /**
     * Flag that shows when this node has leaved.
     */
	protected boolean hasLeaved; // For Leaved Case
	/** Shows when has been received the GET_PRE response, requested on the stabilize() method to find the successor. */
	protected boolean hasReceivedSucc;
    /**
     * Flag that optimizes the number of invokations of <b>closestPrecedingFinger</b>
     * method.
     */
	private boolean cpfFound; 
	
	
	/**
     * Current finger table.
	 */
	public NodeHandle[] finger;
	/**
     * The starting indices for the finger table (as Finger[k].start).
	 */ 
	public Id[] start;
	/**
     * Current successor list.
	 */
	public Vector succList;	

	/**
	 * Temporal use in Common API methods.
	 */
	protected Vector auxCAPI;
	public Id deux;
	protected NodeHandle[] temp;
	
	
	/**
	 * Constructor, create a new BadChordNode instance with this node Id
	 */
	public ChordNode() throws InitializationException {
        super();
        deux = GenericFactory.buildId(ChordId.TWO_CHORD);
		finger = new NodeHandle[bitsPerKey];
		start = new Id[bitsPerKey];
		temp = new NodeHandle[3];
		succList = new Vector(((ChordProperties)Properties.overlayPropertiesInstance).succListMax+2);
		hasLeaved = false;
		hasFailed = false;
		hasReceivedSucc = true;   //permits the send
        
        //add stabilize timer
        setTimer(new StabilizeTask(),  ((ChordProperties)Properties.overlayPropertiesInstance).stabilizeSteps,
                ((ChordProperties)Properties.overlayPropertiesInstance).stabilizeSteps);
        // add fix finger timer
        setTimer(new FixFingerTask(),  ((ChordProperties)Properties.overlayPropertiesInstance).fixFingerSteps,
                ((ChordProperties)Properties.overlayPropertiesInstance).fixFingerSteps);

	}
	
	/**
	 * Sets the predecessor node
	 * 
	 * @param handle
	 *            NodeHandle of the predecessor
	 */
	public void setPred(NodeHandle handle) {
		predecessor = handle;
	}
	/**
	 * Sets the successor node
	 * 
	 * @param handle
	 *            NodeHandle of the successor
	 */
	public void setSucc(NodeHandle handle) {
		finger[0] = handle;
        
        //always the successor at the first position
        if (succList.size()>0)
            succList.add(0,handle);
        else
            succList.add(handle);
	}
    
    /**
     * Deletes all NodeHandles that exceeds the required size of successor list.
     */
    protected void cleanSuccList()
    {
        int succListMax = ((ChordProperties)Properties.overlayPropertiesInstance).succListMax;
        for (int i=succList.size(); i > succListMax; i--)
        {
            succList.remove(i-1);
        }
    }
    
	/**
	 * Returns the NodeHandle of the present identification of the precursor of the
	 * node
	 * 
	 * @return predeccesor NodeHandle
	 */
	public NodeHandle getPred() {
		return predecessor;
	}
	/**
	 * Returns the NodeHandle of the present identification of the successor of the
	 * node
	 * 
	 * @return predeccesor NodeHandle
	 */
	public NodeHandle getSucc() {
		return finger[0];
	}
	/**
	 * Returns the successor list of the present node
	 * 
	 * @param max
	 *            number of the elements to return
	 * @return successor list Vector
	 */
	public Vector getSuccList(int max) {
		Iterator it = succList.iterator();
		Vector sl = new Vector();
		int i = 0;
		while (it.hasNext() && i < max)
			sl.add(it.next());
		return sl;
	}
	
	/**
	 * Clears the counter of the finger table changes
	 */
	protected void clearFingerChanges() {
		fingerChanges = 0;
	}
	/**
	 * Returns the count of the finger table changes
	 * 
	 * @return finger_changes int counter
	 */
	protected int getFingerChanges() {
		return fingerChanges;
	}
	/**
	 * Sets a especific finger of the finger table
	 * @param pos position of the finger
	 * @param handle new NodeHandle to change
	 */
	protected void setFinger(int pos, NodeHandle handle) {
		if (finger[pos] == null || !finger[pos].equals(handle)) {
			fingerChanges++;
			finger[pos] = handle;
		}
	}
	
	public Hashtable getInfo() {
		Hashtable info = new Hashtable();
		info.put("predecessor", predecessor);
		info.put("finger", finger);
		info.put("start", start);
		info.put("succ_list", succList);
		return info;
	}
	/**
	 * Shows for System.out all information of the node, including finger
	 * table.
	 */
	public void printNode() {
		System.out.println("######\nNode:        " + getId());
		System.out.println("Finger table:");
		for (int i = 0; i < finger.length; i++) {
			if (finger[i] != null)
				System.out.println(start[i] + " : " + finger[i]);
			else
				System.out.println(" finger [" + i + "] == null ");
			System.out.println("-----------");
		}
		System.out.println("Predecessor: " + predecessor);
		System.out.println("Succ-List: " + succList);
		System.out.println("###### END OF NODE\n");
	}
	/**
	 * Shows for System.out only the owner Id, its predecessor and successor.
	 */
	public void prettyPrintNode() {
		System.out.println("######\nNode:        " + getId());
		System.out.println("Successor:   " + finger[0]);
		System.out.println("Predecessor: " + predecessor);
		System.out.println("###### END OF NODE\n");
	}

	/**
	 * Finds the successor of a node. If the result if null because the
	 * information its not contain in the finger table, it generate a request
	 * message and the response message content the real result
	 * 
	 * @param handle
	 *             NodeHandle of the one node
	 * @return successor Id of the node successor
	 */
	protected NodeHandle findSuccessor(NodeHandle handle) {
		if (predecessor != null && handle.getId().betweenE(predecessor.getId(), this.id)) {
			return this.nodeHandle;
		} else {
			return findPredecessor(handle);
		}
	}
	/**
	 * Finds the predecessor of a node. If the result if null because the
	 * information its not contain in the finger table, it generate a request
	 * message and the response message content the real result
	 * 
	 * @param handle
	 *            NodeHandle of the one node
	 * @return successor NodeHandle of the node predecessor
	 */
	protected NodeHandle findPredecessor(NodeHandle handle) {
		if (handle.getId().equals(this.id)) {
			return this.nodeHandle; //return predecessor.successor();
		} else if (finger[0] != null && handle.getId().betweenE(this.id, finger[0].getId())) {
			return finger[0];
		} else {
			return null;
		}
	}
	
	/**
	 * Finds the closest preceding finger of a node
	 * 
	 * @param id
	 *            Id of the one node
	 * @return successor NodeHandle of the node successor
	 */
	protected NodeHandle closestPrecedingFinger(Id id) {
		cpfFound = false;
		temp[2] = this.nodeHandle; //n_id
		for (int i = bitsPerKey - 1; (i > -1 && !cpfFound); i--) {
			if (finger[i] != null && finger[i].getId().betweenE(this.id, id)) {
				temp[2] = finger[i];
				cpfFound = true;
			}
		}
		return temp[2];
	}
	/**
	 * Finds the successor of a node. If the result if null because the
	 * information its not contain in the finger table, it generate a request
	 * message and the the content response message has been store in the
	 * finger table
	 * 
	 * @param pos
	 *            position of NodeHandle in the start table
	 * @return successor NodeHandle of the node successor
	 */
	protected NodeHandle findSuccessor(int pos) {
		if (predecessor != null && start[pos].betweenE(predecessor.getId(), this.id)) {
			return this.nodeHandle;
		} else {
			return findPredecessor(pos);
		}
	}
	/**
	 * Finds the predecessor of a node. If the result if null because the
	 * information its not contain in the finger table, it generate a request
	 * message and the the content response message has been store in the
	 * finger table
	 * 
	 * @param pos
	 *            position of NodeHandle in the start table
	 * @return successor NodeHandle of the node predecessor
	 */
	protected NodeHandle findPredecessor(int pos) {
		if (start[pos].equals(this.id)) {
			return this.nodeHandle; 
		} else if (finger[0] != null && start[pos].betweenE(this.id, finger[0].getId())) {
			return finger[0];
		} else {
            temp[1] = closestPrecedingFinger(start[pos]); 
            String key = GenericFactory.generateKey();
            sendMessage(key,nodeHandle,temp[1],FIND_PRE,REQUEST,new IdMessage(start[pos]));
            addMessageListener(key, new FindPredListener(this, pos));
		}
		return finger[pos];
	}
	/**
	 * The node joins in the network
	 * 
	 * @param bootstrap
	 *            Id of arbitrary node in the network
	 */
	public void join(NodeHandle bootstrap) {
		if (bootstrap.equals(nodeHandle)) {
			for (int i = 0; i < bitsPerKey; i++) {
				finger[i] = this.nodeHandle;
			}
			predecessor = this.nodeHandle;
		} else {
            String key = GenericFactory.generateKey(); 
            predecessor = null;
            NodeHandle boots = null;
            sendMessage(key,nodeHandle,bootstrap, FIND_SUCC,REQUEST,null);
            addMessageListener(key, new FindSuccListener(this));
		}
	}
	
	/**
	 * The node leaves the network
	 */
	public void leave() {
		hasLeaved = true;
        sendMessage(null,nodeHandle,predecessor,SET_SUCC,REFRESH, new NodeMessage(finger[0]));
        sendMessage(null,nodeHandle,finger[0],SET_PRE,REFRESH,new NodeMessage(predecessor));
		Logger.log("LEAVING NODE " + id, Logger.EVENT_LOG);
	}
	/**
	 * Verify node immediate successor and tell the successor about this,
	 * moreover generate and update the successor list.
	 */
	protected void stabilize() {
		RouteMessage aMsg = null;
        
		if (hasReceivedSucc)
		{
			hasReceivedSucc = false;
            String key = GenericFactory.generateKey(); 
            sendMessage(key,nodeHandle,finger[0],GET_PRE,REQUEST,null);
            addMessageListener(key, new GetPreListener(this));
		}
		
		//if the links are been correct then sends the request to successor
		if (finger[0] != null && !finger[0].equals(this.nodeHandle)
				&& predecessor != null && !finger[0].equals(predecessor)) {
                sendMessage(null,nodeHandle,finger[0],SUCC_LIST,REQUEST,null);
		}
	}
	/**
	 * Verify if the indicated node is the true predecessor
	 * 
	 * @param nh
	 *            node to check
	 */
	protected void notify(NodeHandle nh) {
		if (predecessor == null || nh.getId().between(predecessor.getId(), this.id)) {
			predecessor = nh;
		}
	}
	/**
	 * This method allows to update a finger table position.
	 */
	protected void fixFingers() {
		if (nullPointers == 0) {
			nullPointers++;
		}
		setFinger(nullPointers, findSuccessor(nullPointers));
		nullPointers = (nullPointers + 1) % bitsPerKey;
	}
	
	/**
	 * Send a message to destination node directly.
	 * 
	 * @param message
	 *            Message to deliver from application.
	 * @param hint Proposed node handle for the next hop.
     * @param mode Message mode (REQUEST or REFRESH)
	 */
	protected void sendData(RouteMessage message, NodeHandle hint, int mode) {
		message.setNextHopHandle(hint);
		sendMessage(message);
		Results.incTraffic();
	}
    
	/**
	 * Send a message to unknown destination node via routing.
	 * 
	 * @param message
	 *            Message to deliver from application.
	 * @param nextHop Proposed next hop.
	 */
	protected void routingData(RouteMessage message, NodeHandle nextHop) {
		//enrutar missatge via key del missatge (el idMessage)
		if (predecessor != null && nextHop.getId().betweenE(predecessor.getId(), this.id)) {
			//deliver
			EndPoint endpoint = (EndPoint) endpoints.get(message
					.getApplicationId());
			endpoint.scheduleMessage(message, 0);
		} else if (finger[0] != null && nextHop.getId().betweenE(this.id, finger[0].getId())) {
			sendData(message, finger[0],REFRESH);
		} else {
            sendData(message,closestPrecedingFinger(nextHop.getId()),REQUEST);
			Results.updateHopsMsg(message.getSource().getId(), message.getKey());
		}
	}
	
	/**
	 * Sends a RouteMessage to the ring. If nextHop is null, it shows that the
	 * destination node is unknown and the message will be routed. If nextHop
	 * is not null, it's the destination NodeHandle and it must be reached
	 * directly.
	 * @param appId The applicationId The application identifier of the incoming 
	 * 				application message. 
	 * @param to The destination NodeHandle.
	 * @param nextHop The next hop NodeHandle if it is known.
	 * @param msg The incoming message from above layers. 
	 */
	public void routeData(String appId,NodeHandle to, NodeHandle nextHop, Message msg) {
		RouteMessage toSend = null;
		try {
			toSend = getDataMessage(appId,nodeHandle,to,nextHop,msg);
			if (nextHop != null) this.sendData(toSend, nextHop,REFRESH);
			else this.routingData(toSend, to);
		} catch (InitializationException e) {
			Logger.log("[" + id + "] cannot build a RouteMessage for this data [" + msg + "]",Logger.ERROR_LOG);
		}
	}

	/**
	 * Initiating a Broadcast Message
	 * 
	 * @param appId Application which sends this broadcast.
	 * @param to Destination of the broadcast.
	 * @param nextHop Next hop.
	 * @param msg Message to be sent into the broadcast.
	 */
	public void broadcast(String appId,
			NodeHandle to, NodeHandle nextHop, Message msg) {
		NodeHandle limit;
		NodeHandle r;
		for (int i = 0; i < bitsPerKey - 1; i++) {
			//Skip a redundant finger
			if (!finger[i].equals(finger[i + 1])) {
				r = finger[i];
				limit = finger[i + 1];
				RouteMessage aMsg = null;
				try {
					//String appId,Id from, Id to, Id nextHop
					aMsg = getBroadcastMessage(appId,
							this.nodeHandle, r, r,new BroadcastMessage(msg, limit));
					sendMessage(aMsg);
					Results.incTraffic();
				} catch (InitializationException e) {
					Logger.log("Cannot build a new instance of RouteMessage for BroadcastMessage",
									Logger.ERROR_LOG);
				}
			}
		}
		//Process the last finger
		RouteMessage aMsg = null;
		try {
			//String appId,Id from, Id to, Id nextHop
			aMsg = getBroadcastMessage(appId, this.nodeHandle,
					finger[bitsPerKey - 1],
					finger[bitsPerKey - 1],
					new BroadcastMessage(msg, nodeHandle));
			//I'm the limit of the last finger
			sendMessage(aMsg);
			Results.incTraffic();
		} catch (InitializationException e) {
			Logger.log("ERROR: Cannot get a RouteMessage of MessagePool\n"
					+ e.getMessage(), Logger.ERROR_LOG);
		}
	}
	/**
	 * Treats the messages and according to the case, executes the generic
	 * listeners or listeners specialized, forward the messages or send
	 * responses messages
	 * 
	 * @param msg
	 *            IMessage to treat
	 *  
	 */
	public void dispatcher(RouteMessage msg) {
		//Response message but not successor list type (key == null)
		if (msg.getMode() == REPLY && msg.getKey() != null) {
			String key = msg.getKey();
			try {
				((MessageListener) listeners.get(key)).onMessage(msg);
			} catch (NullPointerException e) {
				Logger.log("I'm [" + id + "]; not exist listener for key ["
						+ msg.getKey() + "]", Logger.ERROR_LOG);
			}
			removeMessageListener(key);
		} else if (msg.getMode() == Globals.ERROR) {
			if (msg.getKey() != null) {
				String key_fp = msg.getKey();
				Logger.log("Node " + this.id + " destroy message key " + key_fp
						+ " type " + Globals.typeToString(msg.getType()) + " content "
						+ msg.getMessage(), Logger.MSG_LOG);
				MessageListener lst = (MessageListener) listeners.get(key_fp);
				if (lst != null) {
					removeMessageListener(key_fp);
				}
			}
			//Successor lost
			if (finger[0] != null && msg.getSource().equals(finger[0])
					&& succList.size() > 0) {
				//if not exists, succ_list is unchanged
				succList.remove(finger[0]);
				if (succList.size() > 0) {
					finger[0] = (NodeHandle) succList.firstElement();
					//send notify
                    this.sendMessage(msg,null,nodeHandle,finger[0],finger[0],SET_PRE,REFRESH,new NodeMessage(nodeHandle));
				}
			} else
				//if source not exists, succ_list is unchanged
				succList.remove(msg.getSource());
		} else {
			switch (msg.getType()) {
				//DATA
				case DATA :
				    dispatchDataMessage(msg,REQUEST,REFRESH);
					break;
				//CONTROL
				//REFRESH
				case SET_SUCC :
					NodeHandle succ = ((NodeMessage) msg.getMessage()).getNode();
					setSucc(succ);
					GenericFactory.freeMessage(msg);
					break;
				case SET_PRE :
					setPred(((NodeMessage) msg.getMessage()).getNode());
					GenericFactory.freeMessage(msg);
					break;
				case NOTIFY :
					notify(msg.getSource());
					GenericFactory.freeMessage(msg);
					break;
				case BROADCAST :
					NodeHandle r, new_limit;
					BroadcastMessage bm = (BroadcastMessage) msg.getMessage();
					NodeHandle limit = bm.getLimit();
					Id limitId = limit.getId();
					planet.commonapi.Message info = bm.getInfo();
					
					for (int i = 0; i < bitsPerKey - 1; i++) {
						//Skip a redundant finger
						if (!finger[i].equals(finger[i + 1])) {
							//Forward while within "Limit"
							if (finger[i].getId().between(this.id, limitId)) {
								r = finger[i];
								//New Limit must not exceed Limit
								if (finger[i + 1].getId().between(this.id, limitId)) {
									new_limit = finger[i + 1];
								} else {
									new_limit = limit;
								}
								//no reuse of RouteMessage msg ==> send one
								// message to different nodes ==> requires
								// different messages
								planet.commonapi.RouteMessage aMsg = null;
								try {
									// String appId,Id from, Id to, Id nextHop
									aMsg = getBroadcastMessage(msg
											.getApplicationId(), this.nodeHandle, r, r,
											new BroadcastMessage(info,new_limit));
									sendMessage(aMsg);
									Results.incTraffic();
								} catch (InitializationException e) {
									Logger.log(
											"ERROR: Cannot get a RouteMessage of MessagePool\n"
											+ e.getMessage(), Logger.ERROR_LOG);
								}
							}
						}
					}
					Logger.log("Broadcast : Node " + this.id + " info : "
							+ info, Logger.EVENT_LOG);
					msg.setMessage(info);
					Results.decTraffic();
					((EndPoint) endpoints.get(msg.getApplicationId())).scheduleMessage(msg, 0);
					break;
				//REQUEST
				case FIND_SUCC :
					//source --> join();
					NodeHandle fSucc = findSuccessor(msg.getSource());
					if (fSucc != null) {
                        this.sendMessage(msg,msg.getKey(),nodeHandle,msg.getSource(),msg.getSource(),msg.getType(),REPLY,new NodeMessage(fSucc));
					} else {
						String key_fp = GenericFactory.generateKey(); 
                        NodeHandle aux = closestPrecedingFinger(msg.getSource().getId());
                        addMessageListener(key_fp, new FindPredListener(this, msg.getKey()));
                        this.sendMessage(msg,key_fp,nodeHandle,aux,aux,FIND_PRE,REQUEST,new IdMessage(msg.getSource().getId()));
					}
					break;
				case FIND_PRE :
				    Id idMesg = ((IdMessage) msg.getMessage()).getNode();
					if (finger[0] != null && idMesg.betweenE(this.id, finger[0].getId())) {
						//return successor
						Id msgId = ((IdMessage) msg.getMessage()).getNode();
						try {
                            sendMessage(msg,msg.getKey(),GenericFactory.buildNodeHandle(msgId,true),
                                    msg.getSource(),msg.getSource(),FIND_PRE,REPLY,new NodeMessage(getSucc()));
						} catch (InitializationException e1) {
							e1.printStackTrace();
						}
					 } else if (idMesg.equals(getId())) {
							try {
                                sendMessage(msg,msg.getKey(),GenericFactory.buildNodeHandle(idMesg, true),
                                        msg.getSource(),msg.getSource(),FIND_PRE,REPLY,new NodeMessage(nodeHandle));
							} catch(InitializationException e) {
								e.printStackTrace();
							}
					 } else {
						//next node
                        NodeHandle aux = closestPrecedingFinger(idMesg);
                        sendMessage(msg,msg.getKey(),msg.getSource(),aux,aux,msg.getType(),msg.getMode(),msg.getMessage());
					}
					break;
				case GET_PRE :
					//origen --> stabilize();
                    sendMessage(msg,msg.getKey(),nodeHandle,msg.getSource(),msg.getSource(),GET_PRE,REPLY,new NodeMessage(predecessor));
					break;
				case SUCC_LIST :
					if (msg.getMode() == REQUEST) {
                        this.sendMessage(msg,msg.getKey(),nodeHandle,msg.getSource(),msg.getSource(),SUCC_LIST,REPLY,new SuccListMessage(succList));
					} else if (msg.getMode() == REPLY) {
						SuccListMessage succs = (SuccListMessage) msg.getMessage();
                        succList.clear();
                        succList.add(msg.getSource());
                        succList.addAll(succs.getSuccs());
                        cleanSuccList();
						GenericFactory.freeMessage(msg);
					}
					break;
			}
		}
	}
	/**
	 * Given a time fraction, treats the messages and executes the periodicas
	 * actions, as for example, the stabilization methods
	 * @param actualStep Actual simulation step.
	 * @return true if the node needs to continue the stabilization
	 * process. false if node is just stabilized.
	 */
	public boolean process(int actualStep) {
		clearFingerChanges();
		super.process(actualStep);
		while (hasMoreMessages()) {
			dispatcher(nextMessage());
		}
		if (getFingerChanges()>0) stabRate = 0;
		else stabRate++;
		invokeByStepToAllApplications();
		return stabRate < realStabilizationRate; 
	}
	
	/**
	 * @see planet.commonapi.Node#isAlive() Test if Node is alive
	 */
	public boolean isAlive() {
		return !hasFailed && !hasLeaved;
	}

	public String toString() {
		return "{ChordNode [" + this.id + "]: succ[" + finger[0] + "]: succ2f["
				+ finger[1] + "]: succList[" + succList + "]: pred["
				+ predecessor + "]}";
	}
	
	/**
	 * Return the local NodeHandle.
	 * 
	 * @see planet.commonapi.Node#getLocalHandle()
	 * @return Actual NodeHandle for this Node.
	 */
	public NodeHandle getLocalHandle() {
		return nodeHandle;
	}
	
	
	/* ************************* MESSAGE LISTENERS ********************/
	
	public class LookupListener implements MessageListener {
		private ChordNode node;
		private Id key;
		public LookupListener(ChordNode node, Id key) {
			this.node = node;
			this.key = key;
		}
		public void onMessage(RouteMessage msg) {
			NodeHandle own = ((NodeMessage) msg.getMessage()).getNode();
			Results.addLookup(key, own.getId());
		}
	}
	
	public class GetPreListener implements MessageListener {
		private ChordNode node;
		public GetPreListener(ChordNode node) {
			this.node = node;
		}
		public void onMessage(RouteMessage msg) {
			// continues stabilize()
		    node.hasReceivedSucc = true;
			NodeHandle msgNh = ((NodeMessage) msg.getMessage()).getNode();
			if (msgNh != null && msgNh.getId().between(node.getId(), node.getSucc().getId())) {
				node.setSucc(msgNh);
			}
			//send notify
            node.sendMessage(msg,null,node.getLocalHandle(),node.getSucc(),node.getSucc(),NOTIFY,REFRESH,msg.getMessage());
		}
	}
	
	
	public class FindSuccListener implements MessageListener {
		private ChordNode node;
		public FindSuccListener(ChordNode node) {
			this.node = node;
		}
		public void onMessage(RouteMessage msg) {
			//continues join()
			NodeHandle succ = ((NodeMessage) msg.getMessage()).getNode();
			node.setSucc(succ);
			GenericFactory.freeMessage(msg);
		}
	}
	
	
	public class FindPredListener implements MessageListener {
		/** Flag that shows that this listener type is for a fix finger purposes. */
		public static final int FIND_PRED_FOR_FIX_FINGER = 1;
		/** Flag that shows that this listener type is for a response on a remote join. */
		public static final int FIND_PRED_FOR_JOIN       = 2;
		/** Reference to the local node. */
		private ChordNode node;
		/** Purpose for this listener. */
		private int listenerPurpose = 0;
		/** The finger position to be set on a FIND_PRED_FOR_FIX_FINGER case. */
		private int fixFingerPosition = 0;
		/** The message key to be set to the RouteMessage on a FIND_PRED_FOR_JOIN case. */
		private String keyForJoinMessage = null;
		
		/**
		 * Builds a FindPredListener for a fix finger purposes.
		 * @param localNode The local Node.
		 * @param fingerPosition The position of the finger to be updated.
		 */
		public FindPredListener(ChordNode localNode, int fingerPosition)
		{
			listenerPurpose = FIND_PRED_FOR_FIX_FINGER;
			node = localNode;
			fixFingerPosition = fingerPosition;
		}
		
		/**
		 * Builds a FindPredListener for a join message response.
		 * @param localNode The local node.
		 * @param msgKey The RouteMessage key for the response.
		 */
		public FindPredListener(ChordNode localNode, String msgKey)
		{
			listenerPurpose = FIND_PRED_FOR_JOIN;
			node = localNode;
			keyForJoinMessage = new String(msgKey); //make a copy of the String!!
		}

		/**
		 * Make a different action using the actual listener purpose.
		 * In a first case, it updates the finger position with the incoming information.
		 * In a second case, it makes a response for a remote join message. 
		 */
		public void onMessage(RouteMessage msg) {
			//n.id from --> n of n.find_successor(n)
			//n1.id to --> n1 of n1.find_successor(n)
			switch(listenerPurpose)
			{
			case FIND_PRED_FOR_FIX_FINGER:
				node.setFinger(fixFingerPosition, ((NodeMessage) msg.getMessage()).getNode());
				GenericFactory.freeMessage(msg);
				break;
			case FIND_PRED_FOR_JOIN:
                node.sendMessage(msg,keyForJoinMessage,msg.getDestination(),msg.getSource(),msg.getSource(),FIND_SUCC,REPLY,msg.getMessage());
				break;
			}
		}
	}
	
	/**
	 * Sets alive flag to false.
	 * @see planet.commonapi.Node#fail()
	 */
	public void fail() {
		this.hasFailed = true;
		this.nodeHandle.setAlive(false);
	}
	
	/**
	 * Overwrites the outMessages to evaluates the internal flag
	 * <b>hasFailed</b> for not to deliver the outgoing queue of
	 * messages. This implementation is only for Chord overlay.
	 * @see planet.commonapi.Node#outMessages()
	 * @see planet.generic.commonapi.NodeImpl#outMessages()
	 * @return null if the node has failed. The outgoing queue
	 * if the node is alive or just has leaved at this simulation step.
	 */
	public Queue outMessages() {
		if (hasFailed) return null;
		return super.outMessages();
	}

	/* END ************************* MESSAGE LISTENERS ********************/
	
	/* ***************** TIMER TASKS *************************************/
	
	/**
	 * Simple TimerTask that invoke stabilize() Node method.
	 * 
	 * @author <a href="mailto: jordi.pujol@estudiants.urv.es">Jordi Pujol </a>
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
				if (finger[0] != null) {
					stabilize();
				}
			}
		}
		/**
		 * Shows the name of the TimerTask, showing if is periodic.
		 * 
		 * @see java.lang.Object#toString()
		 * @return The name of the TimerTask
		 */
		public String toString() {
			return "StabilizeTask: periodic";
		}
	}
	
	/**
	 * Simple TimerTask that invoke fix_fingers() Node method.
	 * 
	 * @author <a href="mailto: jordi.pujol@estudiants.urv.es">Jordi Pujol </a>
	 *         Date: 07/05/2004
	 */
	public class FixFingerTask extends TimerTaskImpl {
		/**
		 * Initialize this StabilizeTask.
		 */
		public FixFingerTask() {
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
				fixFingers();
			}
		}
		/**
		 * Shows the name of the TimerTask, showing if is periodic.
		 * 
		 * @see java.lang.Object#toString()
		 * @return The name of the TimerTask
		 */
		public String toString() {
			return "FixFingerTask: periodic";
		}
	}
	
	/* END ************************ TIMER TASKS ****************************/
	
	/* BEGIN **************************** GML  ****************************/
    /**
     * This method is a callback method used to collect all edges of a graph 
     * according to <b>resultName</b> format.
     * @param resultName Result type name to use.
     * @param edgeCollection Edge collection where to put in all produced ResultEdges.
     * @param constraint ResultsConstraint for edge selection
     */
    public void buildEdges(String resultName, Collection edgeCollection, ResultsConstraint constraint) {
		if (edgeCollection == null || constraint == null) return;
		
		Iterator it;
		
		it = succList.iterator();
		addEdges(resultName, it, constraint, edgeCollection, "#0000FF");
		
        int bitsPerKey = ((ChordProperties)Properties.overlayPropertiesInstance).bitsPerKey;
        Id lastId = null;
		for (int i = 0; i < bitsPerKey; i++) {
            //am I?
			if (finger[i].getId().equals(getId())) continue;
            //is a repeated node?
            if (lastId != null && finger[i].getId().equals(lastId)) continue;
            lastId = finger[i].getId();
			try {
				ResultsEdge e = GenericFactory.buildEdge(resultName, getId(),lastId, false, "#FF0000");
				if (constraint.isACompliantEdge(e)) edgeCollection.add(e);
			} catch (InitializationException e)
			{ e.printStackTrace();}
		}
	}
	
	/* END ************************ GML ****************************/
	
	
	/* ************************ COMMON API METHODS ****************************/

	/**
	 * Returns a list of nodes that can be used as next hops on a route
	 * towards key. If is safe, the expected fraction of faulty nodes in the
	 * list is guaranteed to be no higher than the fraction of
	 * faulty nodes in the overlay. 
	 * <br><br>
	 * In Chord, must be returns the <b>max</b> successors of <b>key</b> in
	 * the node's location table.
	 * @param key Target key
	 * @param max Number of next hops.
	 * @param safe This flag is not consulted.
	 * @return Until a maximum of max nodes to use as next hop, or null if 
	 * key is not in range of node's location table.
	 * @see planet.commonapi.Node#localLookup(planet.commonapi.Id, int, boolean)
	 */
	public Vector localLookup(Id key, int max, boolean safe) {
		Vector nodes = new Vector(); //to permit only one instance of each one
		int first = firstLocalLookup(key);
		//if key is not in finger table range.
		if (first==-1) return null;
		for (int i=first; i<((ChordProperties)Properties.overlayPropertiesInstance).bitsPerKey && nodes.size()<max; i++)
				nodes.add(finger[i]);
		return nodes;
	}
	
	/**
	 * Detects de first position that key is in range of some position of 
	 * finger table.
	 * @param key Id to find the first position of finger table is responsible.
	 * @return -1 if the key is not in range of the finger table, or some value
	 * between 0..Properties.bitsKey-1
	 */
	public int firstLocalLookup(Id key) {
		int i;
		if (key.compareTo(start[((ChordProperties)Properties.overlayPropertiesInstance).bitsPerKey-1])>0 ||
			key.compareTo(start[0])<0) return -1;
		for (i=((ChordProperties)Properties.overlayPropertiesInstance).bitsPerKey-1;i>=0 && key.compareTo(this.start[i])<=0; i++);
		return i; 
	}
	
	
	/**
	 * @see planet.commonapi.Node#neighborSet(int)
	 * Obtains an unordered list of up to max nodes that are neighbors 
	 * of the local node.
	 * @param max Maximum of nodes to return.
	 * @return Neighbor nodes. At the first position appears the predecessor node,
	 * and following the rest of successors nodes.
	 */
	public Vector neighborSet(int max) {
		int maxIndex = Math.min(max-1,succList.size());
		auxCAPI=this.getSuccList(maxIndex);
		auxCAPI.add(0,predecessor);
		return auxCAPI;
	}
	
	/**
	 * @see planet.commonapi.Node#range(planet.commonapi.NodeHandle, planet.commonapi.Id, planet.commonapi.Id, planet.commonapi.Id)
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
	 * the range. It must be initialized to 0 (zero) before this method could be invoked.
	 * @return true if the range could be determined; false otherwise, including
	 * the case of node is not present in the neighbor set returned by neighborSet().
	 * If false is returned, the leftKey and rightKey are not corrects.
	 */
	public boolean range(NodeHandle node, Id rank, Id leftKey, Id rightKey) {
		/* Must be an error if 'node' is not in array returned by neighborSet()
		 * method. In this case, evaluates the best case of neighborSet()
		 * invokation, using all elements in successor list. */
		if (!node.equals(predecessor) && !succList.contains(node)) 
			return false;
		
		try {
			/* Correct the value for 'leftKey'. Only in the case in which the
			 * 'leftkey' is minor than or equals to predecessor for the actual
			 * node. */
			if (leftKey.compareTo(predecessor.getId())<=0) {
				leftKey.add(GenericFactory.buildId(ChordId.ONE_CHORD));
			
			}
		
			/* Sets the value of 'rightKey' to the correct maximum value.
			 * That is, rightKey = min(leftKey+rank,node), once the leftKey 
			 * already has been corrected. */
			rightKey.add(leftKey);
			rightKey.add(rank);
			
			//verify if the sum leaves rank
			BigInteger max = new BigInteger("2").pow(((ChordProperties)Properties.overlayPropertiesInstance).bitsPerKey);
			Id maxId = GenericFactory.buildId(max);
			if (maxId.compareTo(rightKey)<=0) {
				rightKey.subtract(maxId);
			}
			//sets the correct value
			if (rightKey.compareTo(node)>0) {
				rightKey.setValue(node);
			}
		} catch (InitializationException e) {
			// because the value cannot be set, return false
			return false;
		}
		return true;
	}
	
	/**
	 * @see planet.commonapi.Node#replicaSet(planet.commonapi.Id, int)
	 * Returns the replica node set.
	 * The number of successor list is defined in the context.
	 * @param key Key from which obtain the replica set.
	 * @param maxRank Number of nodes in replica set.
	 * @return Up to maxRank nodes as replica set.
	 */
	public Vector replicaSet(Id key, int maxRank) {
		if (!key.betweenE(getPred().getId(),getId())) return null;
		if (!(maxRank>=1)) return null;
		auxCAPI = new Vector();
		int maxIndex = Math.min(maxRank-1,succList.size());
		if (maxIndex>0) { 
			auxCAPI.addAll(getSuccList(maxIndex));
		}
		auxCAPI.add(0,nodeHandle);
		return auxCAPI;
	}
	
	/* END ************************ COMMON API METHODS ****************************/
	
	
	/**
	 * Gets all node connections to other nodes as its NodeHandles. The order of NodeHandles
	 * is unexpected. 
	 * @return A set with all connections as NodeHandles.
	 */
	public Set getAllLinks() {
	    HashSet conns = new HashSet();
	    
	    //recollect all fingers not null
	    for (int i=0; i<finger.length; i++)
	    {
            conns.add(finger[i]);
	    }
	    //recollect the predecessor
        conns.add(getPred());
	    
	    //recollect the successors
	    conns.addAll(succList);
	    
	    //remove the null element if exists
	    conns.remove(null);
	    
	    return conns;
	}
	
	/** 
	 * Returns the NodeHandle closest to <b>id</b>.
	 * @param id Id to find.
	 * @return The NodeHandle closest to <b>id</b>.
	 */
	public NodeHandle getClosestNodeHandle(Id id)
	{
	    return closestPrecedingFinger(id);
	}
    
    
    /**
     * Sets the new Id for this node.
     * @param newId The new Id.
     * @return The same instance, after it has been updated.
     * @see planet.commonapi.Node#setValues(planet.commonapi.Id)
     */
    public Node setValues(Id newId) throws InitializationException {
        super.setValues(newId);
        for (int i = 0; i < bitsPerKey; i++) {
            start[i] = (id.add(deux.shift(-(i - 1), 0)));
        }
        return this;
    }
}
