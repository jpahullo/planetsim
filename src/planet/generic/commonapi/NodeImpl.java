package planet.generic.commonapi;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import planet.commonapi.Application;
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
import planet.simulate.Logger;
import planet.simulate.MessageListener;
import planet.simulate.Results;
import planet.util.Properties;
import planet.util.Queue;
import planet.util.QueueFull;
import planet.util.timer.TimerTask;

/**
 * Superclass which represents a node in a peer-to-peer system, regardless of
 * the underlying protocol. All nodes, implement the methods of this class.
 * 
 * @author Pedro García
 * @author Carles Pairot
 * @author Ruben Mondejar
 */
public abstract class NodeImpl
		implements
			planet.commonapi.Node,
			java.io.Serializable {
	
	protected Id id;
	protected transient Hashtable listeners;
	private transient Queue incoming;
	private transient Queue outgoing;
	private int processed = 0;
	/**
	 * NodeHandle for the actual Node.
	 */
	protected NodeHandle nodeHandle = null;
	/**
	 * To contain int[] with [firstTime][period] values for each task.
	 * If period is zero (0), corresponds with a task to execute only once.
	 */
	private Vector timer = null;
	/**
	 * To contain the jobs (TimerTask) to execute at each time.
	 */
	private Vector tasks = null;
	private long timerCount = 0; //counter
	private long timerNext = Long.MAX_VALUE; //next time to activate
	
	/**
	 * Local EndPoints.
	 */
	protected Hashtable endpoints;
	protected boolean role = true;
	
	/**
	 * Initializes internal data structures.
	 */
	public NodeImpl()  {
        timer = new Vector(2);
        tasks = new Vector(2);
        endpoints = new Hashtable();
        init();
    }
	
	/**
	 * Inicialite
	 */
	private void init() {
		listeners = new Hashtable();
		incoming = new Queue(Properties.simulatorQueueSize);
		outgoing = new Queue(Properties.simulatorQueueSize);
	}
	
	/**
	 * The node joins in the network
	 * 
	 * @param bootstrap
	 *            Id of a node in the network
	 */
	public abstract void join(NodeHandle bootstrap);
	
	/**
	 * The node leaves the network
	 */
	public abstract void leave();
	
	/**
	 * Given a time fraction, the node it can do everything what needs during
	 * this
	 * 
	 * @param actualStep
	 *            Actual step in simulation time.
	 * @return Always true.
	 */
	public boolean process(int actualStep) {
		processed = 0;
		processTasks();
		return true;
	}
	
	/**
	 * Invokes to each registered Application, by the related EndPoint,
	 * the <b>byStep()</b> method. This inform to each application for
	 * a new step. This method must to be invoked at the end of each
	 * <b>process(int)</b> node implementation.
	 */
	protected void invokeByStepToAllApplications() {
	    Iterator it = endpoints.values().iterator();
	    while (it.hasNext())
	        ((EndPoint)it.next()).byStep();
	}
	
	/**
	 * Puts a message in the incoming queue of this node
	 * 
	 * @param msg
	 *            received Message
	 */
	public void receive(RouteMessage msg) throws QueueFull {
		Logger.logReceive(id, msg, Logger.MSG_LOG);
		incoming.add(msg);
	}
    
    /**
     * A wrapper method, that sends a RouteMessage with the specified data.
     * If any exception has ocurred during the send, a log with the description
     * is made.
     * @param key Key of the communication.
     * @param from Communication source
     * @param to Communication destination
     * @param nextHop Next hop in the communication.
     * @param type RouteMessage type
     * @param mode RouteMessage mode
     * @param appId Name of the related application.
     * @param msg Data to be sent with the RouteMessage
     * @return A valid RouteMessage with the specified data or null, if
     * any error has ocurred.
     */
    public RouteMessage buildMessage(String key, NodeHandle from, NodeHandle to, NodeHandle nextHop, int type, int mode, String appId, Message msg)
    {
        RouteMessage bMsg = null;
        try {
            bMsg = GenericFactory.getMessage(key,from,to,nextHop,msg,type,mode,appId);
        } catch (InitializationException e) {
            Logger.log("ERROR: Cannot get a RouteMessage of MessagePool\n"
                    + e.getMessage(), Logger.ERROR_LOG);
        }
        return bMsg;
    }
    
    /**
     * Builds a new RouteMessage with all the values appeared in <b>toCopy</b>, and
     * the specified <b>nextHop</b>.
     * @param toCopy Message to be cloned.
     * @return A valid RouteMessage or null if there are any error.
     */
    public RouteMessage buildMessage(RouteMessage toCopy)
    {
        RouteMessage msg = null;
        try {
            msg = GenericFactory.getMessage(toCopy.getKey(),toCopy.getSource(),toCopy.getDestination(),toCopy.getNextHopHandle(),toCopy.getMessage(),toCopy.getType(),toCopy.getMode(),toCopy.getApplicationId());
            //don't update statistics
        } catch (InitializationException e)
        {
            Logger.log("ERROR: Cannot get a RouteMessage of MessagePool\n" + e.getMessage(), Logger.ERROR_LOG);
            GenericFactory.freeMessage(msg);
            msg = null;
        }
        return msg;
    }
	
	/**
	 * Puts a message in the outcoming queue of this node
	 * 
	 * @param msg
	 *            sended Message
	 */
	public void send(RouteMessage msg) throws QueueFull {
		outgoing.add(msg);
		Logger.logSend(id, msg, Logger.MSG_LOG);
	}
	
	/**
	 * Puts the RouteMessage <b>msg</b> to the outgoing queue of this node.
	 * @param msg RouteMessage to be sent.
	 */
	public boolean sendMessage(RouteMessage msg)
	{
		try {
			send(msg);
		} catch (QueueFull e){
			Logger.log("Outgoing Queue of Node " + this.id + " is Full",
					Logger.ERROR_LOG);
			GenericFactory.freeMessage(msg);
            return false;
		}
        return true;
	}
    
    /**
     * A wrapper method, that sends a RouteMessage with the specified data.
     * If any exception has ocurred during the send, a log with the description
     * is made.
     * @param key Key of the communication.
     * @param from Communication source
     * @param to Communication destination
     * @param type RouteMessage type
     * @param mode RouteMessage mode
     * @param msg Data to be sent with the RouteMessage
     */
    public boolean sendMessage(String key, NodeHandle from, NodeHandle to, int type, int mode, Message msg)
    {
        RouteMessage bMsg = null;
        try {
            bMsg = GenericFactory.getMessage(key, from, to, type, mode);
            bMsg.setMessage(msg);
            return sendMessage(bMsg);
        } catch (InitializationException e) {
            Logger.log("ERROR: Cannot get a RouteMessage of MessagePool\n"
                    + e.getMessage(), Logger.ERROR_LOG);
            return false;
        }
    }
    
    /**
     * A wrapper method, that sends a RouteMessage with the specified data.
     * If any exception has ocurred during the send, a log with the description
     * is made.
     * @param key Key of the communication.
     * @param from Communication source
     * @param to Communication destination
     * @param nextHop Next hop in the communication.
     * @param type RouteMessage type
     * @param mode RouteMessage mode
     * @param msg Data to be sent with the RouteMessage
     */
    public boolean sendMessage(String key, NodeHandle from, NodeHandle to, NodeHandle nextHop, int type, int mode, Message msg)
    {
        RouteMessage bMsg = null;
        try {
            bMsg = GenericFactory.getMessage(key,from,to,nextHop,msg,type,mode,null);
            return sendMessage(bMsg);
        } catch (InitializationException e) {
            Logger.log("ERROR: Cannot get a RouteMessage of MessagePool\n"
                    + e.getMessage(), Logger.ERROR_LOG);
            return false;
        }
    }
    
    /**
     * A wrapper method, that send a RouteMessage with the specified data.
     * If any error has ocurred during the send, a log with the description
     * is made.
     * @param rMsg RouteMessage to be used.
     * @param key Communication key
     * @param from Communication source.
     * @param to Communication destination.
     * @param type RouteMessage type
     * @param mode RouteMessage mode
     * @param msg Data to be sent with the RouteMessage
     */
    public void sendMessage(RouteMessage rMsg,String key, NodeHandle from, NodeHandle to, NodeHandle nextHop,int type, int mode, Message msg)
    {
        rMsg.setValues(key,from,to,nextHop,type,mode,msg,rMsg.getApplicationId());
        sendMessage(rMsg);
    }
	
	/**
	 * Returns the present outgoing queue of this node
	 * 
	 * @return outgoing Queue of Messages
	 */
	public Queue outMessages() {
		return outgoing;
	}
    
    public Queue inMessages()
    {
        return incoming;
    }
	
	/**
	 * Checks if the incoming queue have a messages to send
	 * 
	 * @return return true if has incoming messages to process
	 */
	protected boolean hasMoreMessages() {
		return processed < Properties.simulatorProcessedMessages  && incoming.size() > 0;
	}
	
	/**
	 * Return the next message and dequeue this of the incoming queue
	 * 
	 * @return return the next Message
	 */
	protected RouteMessage nextMessage() {
		processed++;
		return (RouteMessage) incoming.remove();
	}

	/**
	 * Returns the id of the node *
	 * 
	 * @return Id node identificator
	 */
	public Id getId() {
		return id;
	}
	
	/**
	 * Adds a listener to the node so that it executes herself when the message
	 * response arrives
	 * 
	 * @param key
	 *            String representation of id routing message
	 * @param listener
	 *            MessageListener linked to Message
	 */
	public void addMessageListener(String key, MessageListener listener) {
		listeners.put(key, listener);
	}
	
	/**
	 * Remove the message listener of the node
	 * 
	 * @param key
	 *            String representation of id routing message
	 */
	public void removeMessageListener(String key) {
		listeners.remove(key);
	}
	
	/**
	 * Returns information of the node
	 * 
	 * @return info Hashtable with the information
	 */
	public abstract Hashtable getInfo();
	private void writeObject(java.io.ObjectOutputStream out)
			throws java.io.IOException {
		out.defaultWriteObject();
	}
	
	private void readObject(java.io.ObjectInputStream in)
			throws java.io.IOException, ClassNotFoundException {
		in.defaultReadObject();
		init();
	}
	
	/**
	 * Returns the local NodeHandle
	 * @see planet.commonapi.Node#getLocalHandle()
	 * @return The actual local NodeHandle
	 */
	public NodeHandle getLocalHandle() {
		return nodeHandle;
	}
	
	/**
	 * Sets a task to be executed periodicly at each <b>period</b> of time.
	 * @see planet.commonapi.Node#setTimer(planet.util.timer.TimerTask, long, long)
	 * @param task Job to do at each activation of the task.
	 * @param firstTime First activation of the task, measured in steps or millis.
	 * Its value is the relative time, not in absolute time.
	 * @param period Number of steps or millis to periodicly execute the task.
	 */
	public void setTimer(TimerTask task, long firstTime, long period) {
		timer.add(new long[]{firstTime,period});
		tasks.add(task);
		timerNext = (firstTime<timerNext)?firstTime:timerNext;
	}
	
	/**
	 * Sets a task to be executed only one time at specified <b>firstTime</b>.
	 * @see planet.commonapi.Node#setTimer(planet.util.timer.TimerTask, long)
	 * @param task Job to do at activation.
	 * @param firstTime Moment to be activated, in steps or millis, measured
	 * in relative time, not in absolute time. 
	 */
	public void setTimer(TimerTask task, long firstTime) {
		setTimer(task,firstTime,0);
	}
	
	/**
	 * Evaluates if requires any timertask. 
	 *
	 */
	private void processTasks() {
		timerCount++;
		if (timerNext>timerCount) return;
		long[] temp = null;
		boolean evaluate;
		int i=0;
		while(i< timer.size()) {
			evaluate = true;
			temp = (long[])timer.get(i);
			temp[0]=temp[0]-timerCount;
			if (temp[0]<=0) {
				((TimerTask)tasks.get(i)).run();
				if (temp[1]==0) { //if period==0 ==> only once
					timer.remove(i);
					tasks.remove(i);
					i--; //update index
					evaluate = false;
				} else {
					temp[0]=temp[1];
				}
			}
			if (evaluate)
				timerNext = (timerNext>temp[0])?temp[0]:timerNext;
			i++;
		}
		timerCount=0;
	}
	
	/**
	 * This returns a VirtualizedNode specific to the given application and
	 * instance name to the application, which the application can then use in
	 * order to send an receive messages.
	 * 
	 * @param app
	 *            The Application
	 * @param instance
	 *            An identifier for a given instance
	 * @return The endpoint specific to this application, which can be used
	 *         for message sending/receiving. Return null if cannot build the
	 *         required EndPoint.
	 */
	public EndPoint registerApplication(Application app, String instance) {
		EndPoint endpoint = null;
		try {
			endpoint = GenericFactory.buildEndPoint(app, this);
			endpoints.put(app.getId(), endpoint);
			app.setEndPoint(endpoint);
		} catch (InitializationException e) {
			Logger.log("Cannot build a new EndPoint for this Application ["
					+ app + "] and Node [" + this.getId() + "].",
					Logger.ERROR_LOG);
			e.printStackTrace();
		}
		return endpoint;
	}
	
	/**
	 * Get the registered application by the <b>instanceName</b> name.
	 * @param instanceName Name of the registered application.
	 * @return null if there isn't an instance of <b>instanceName</b>, or 
	 * the related endpoint for the application.
	 */
	public EndPoint getRegisteredApplication(String instanceName)
	{
	    return (EndPoint)endpoints.get(instanceName);
	}
	/**
	 * Returns all references of the applications that mantains this node.
	 * 
	 * @return An array with all references to the applications that they are
	 *         mantained for this node.
	 */
	public Application[] getRegisteredApplications() {
		Collection values = this.endpoints.values();
		Application[] apps = new Application[values.size()];
		Iterator it = values.iterator();
		for (int i = 0; it.hasNext(); i++) {
			apps[i] = ((EndPoint) it.next()).getApplication();
		}
		return apps;
	}

    /**
	 * The playsGoodRole's method is an extension method for commonapi specs. This method is
	 * used to allow BehavioursPool decide  wether the <b>Node</b> should run behaviours for
	 * good  peers or instead run  behaviours for bad peers. Moreover, this method  lets the
	 * programmer the responsability of decide node's role. Even it allows a node to  have a
	 * transient behaviour, sometimes behaving as a good peer and sometines behaving as a bad
	 * peer. 
	 * @return True if the Node's Role is positive.
	 */
	public boolean playsGoodRole() {
		return role;
	}
	/**
	 * The setGoodRole's method is an extension method for commonapi specs. This method is 
	 * used to set the role of the node inside the overlay network. A true value means the
	 * node will behave according to the underlying overlay protocol. A false  value means
	 * the node will misbehave.
	 * @param role
	 */
	public void setGoodRole(boolean role) {
		this.role = role;
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
		return true;
	}
	
	/* BEGIN **************************** GML  ****************************/
	
    /**
     * Adds new ResultsEdges to <b>edgeCollection</b> according to specified 
     * <b>constraint</b>.
     * @param resultName Result type name to use.
     * @param it Iterator over a collection of nodeHandles.
     * @param constraint Specific constraints to test all built edges.
     * @param edgeCollection Edges collection where they have to be added.
     * @param color Fill color of the edge.
     */
    public void addEdges(String resultName, Iterator it, ResultsConstraint constraint, 
            Collection edgeCollection, String color) {
        ResultsEdge edge = null;
        NodeHandle nh = null;
		while (it.hasNext()) {
			nh = (NodeHandle) it.next();
			if (nh.getId().equals(getId())) continue;
			edge = buildNewEdge(resultName,getId(),nh.getId(),color);
            if (edge!=null)
                if (constraint.isACompliantEdge(edge)) edgeCollection.add(edge);
		}
	}
    
    /**
     * Builds a ResultsEdge with the specified bounds and following the 
     * <b>resultsName</b> type format.
     * @param resultName Result type name to use.
     * @param left One bound of the link.
     * @param right Second bound of the link.
     * @param color Edge color.
     * @return A new instance of the ResultsEdge according the specified <b>resultName</b>
     * or null if any error has ocurred.
     */
    public ResultsEdge buildNewEdge(String resultName, Id left,Id right,String color)
    {
        try {
            return GenericFactory.buildEdge(resultName, left, right, false, color);
        } catch (InitializationException ex)
        { ex.printStackTrace();}
        return null;
    }
	
	/* END ************************ GML ****************************/

	/** 
	 * Returns the NodeHandle closest to <b>id</b>.
	 * @param id Id to find.
	 * @return The NodeHandle closest to <b>id</b>.
	 */
	public abstract NodeHandle getClosestNodeHandle(Id id);
	
	
	/* ****************************  Generic DATA message dispatcher ************************************/
	/**
	 * Make a generic treatment of the DATA messages (application layer messages).
	 * @param msg RouteMessage to be treated.
	 * @param requestMode RouteMessage REQUEST mode for the actual Overlay implementation.
	 * @param refreshMode RouteMessage REFRESH mode for the actual Overlay implementation.
	 */
	public void dispatchDataMessage(RouteMessage msg, int requestMode, int refreshMode)
	{
	    String id_ap = msg.getApplicationId();
	    Id destinationId = msg.getDestination().getId();
		EndPoint endpoint = (EndPoint) endpoints.get(id_ap);
		NodeHandle succ = getSucc();
		
		if (destinationId.betweenE(getPred().getId(),id)) {
		    // Deliver message to End Point
		    boolean forward = endpoint.forward(msg);
		    if (forward) {
		        endpoint.scheduleMessage(msg, 0);
		        Results.decTraffic();
		    } else 
            {
                Results.decTraffic();
                GenericFactory.freeMessage(msg);
            }
		    return;
		}
		
		if (msg.getMode() == requestMode) {
			if (succ != null
					&& destinationId.betweenE(this.id, succ.getId())) 
            {
				//found successor --> send data
				msg.setNextHopHandle(succ);
				if (endpoint.forward(msg))
				{
					msg.setMode(refreshMode);
					try {
						send(msg);
						Results.updateHopsMsg(msg.getSource().getId(),
								msg.getKey());
					} catch (QueueFull e) {
						Logger.log("Outgoing Queue of Node " + this.id + " is Full", Logger.ERROR_LOG);
						GenericFactory.freeMessage(msg);
					}
				} else
                {
				    Results.decTraffic();
                    GenericFactory.freeMessage(msg);
                }
			} else {
				//next node [ROUTING]
				msg.setNextHopHandle(getClosestNodeHandle(
						msg.getDestination().getId()));
				if (endpoint.forward(msg))
				{
					msg.setMode(requestMode);
					try {
						send(msg);
						Results.updateHopsMsg(msg.getSource().getId(),
								msg.getKey());
					} catch (QueueFull e) {
						Logger.log("Outgoing Queue of Node " + this.id + " is Full", Logger.ERROR_LOG);
						GenericFactory.freeMessage(msg);
					}
				} else 
                {
				    Results.decTraffic();
                    GenericFactory.freeMessage(msg);
                }
			}
		} else if (msg.getMode() == refreshMode) {
			// Deliver message to End Point
			boolean forward = endpoint.forward(msg);
			if (forward) {
				endpoint.scheduleMessage(msg, 0);
				Results.decTraffic();
			} else 
            {
                Results.decTraffic();
                GenericFactory.freeMessage(msg);
            }
		} else 
        {
            Results.decTraffic();
            GenericFactory.freeMessage(msg);
        }
	}
	
    
    /**
     * Sets the new Id.
     * @param newId New Id.
     * @return The same instance after has been updated.
     * @see planet.commonapi.Node#setValues(planet.commonapi.Id)
     */
    public Node setValues(Id newId) throws InitializationException {
        this.id = newId;
        nodeHandle = GenericFactory.buildNodeHandle(id, true);
        return this;
    }
}
