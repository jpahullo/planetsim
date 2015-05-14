package planet.generic.commonapi;

import planet.commonapi.*;
import planet.simulate.Globals;
import java.util.*;

/**
 * Message that wold be routed throwghout network.
 * @author Jordi Pujol
 */
public class RouteMessageWithPathImpl implements RouteMessage {
	/**
	 * The application Id who generates the related data and
	 * will receive on to remote Node.
	 */
	protected String appId;
	/**
	 * Message (data) to be routed
	 */
	protected planet.commonapi.Message message;
	/**
	 * Next node to arrive
	 */
	protected NodeHandle nextHop;
	/**
	 * Destination Node Id who will receive this message.
	 */
	protected NodeHandle destination;
	/**
	 * Source Node Id who sends this message.
	 */
	protected NodeHandle source;
	/**
	 * Type of this message.
	 */
	protected int type;
	/**
	 * Mode of this message.
	 */
	protected int mode;
	/**
	 * Identification key of this message, necessary to known
	 * relationships between sent messages and new incoming messages.
	 */
	protected String key;
	
	/* *********** ADDITIONAL FIELDS **************/
	/**
	 * Previous node on path to current node.
	 */
	protected NodeHandle previousHop;
	/**
	 * RouteMessage additional options.
	 */
	protected Object options;
	/**
	 * Route from the source to the destination.
	 */
	protected List route;
	/**
	 * Time to live for the packet. 
	 */
	protected int ttl;
	/* END ************ ADDITIONAL FIELDS *****************/
	
	/**
	 * Empty constructor. It requires a setValues(...) invocation to update 
	 * all internal values.
	 */
	public RouteMessageWithPathImpl() {
        route = new Vector();
    }
	
	/**
	 * Get the destination node's NodeHandle. 
	 * @see planet.commonapi.RouteMessage#getDestination()
	 */
	public NodeHandle getDestination() {
		return destination;
	}

	/**
	 * Get the Message to route.
	 * @see planet.commonapi.RouteMessage#getMessage()
	 */
	public planet.commonapi.Message getMessage() {
		return message;
	}

	/**
	 * Get next node's NodeHandle to send this message.
	 * @see planet.commonapi.RouteMessage#getNextHopHandle()
	 */
	public NodeHandle getNextHopHandle() {
		return nextHop;
	}

	/**
	 * Sets the destination's NodeHandle.
	 * @see planet.commonapi.RouteMessage#setDestination(planet.commonapi.NodeHandle)
	 */
	public void setDestination(NodeHandle handle) {
		destination = handle;

	}

	/**
	 * Sets the Message to route.
	 * @see planet.commonapi.RouteMessage#setMessage(planet.commonapi.Message)
	 */
	public void setMessage(planet.commonapi.Message message) {
		this.message = message;

	}

	/** 
	 * Sets the next node's Id to send immediately this message.
	 * @see planet.commonapi.RouteMessage#setNextHopHandle(planet.commonapi.NodeHandle)
	 */
	public void setNextHopHandle(NodeHandle nextHop) {
		this.nextHop = nextHop;
	}
	
	/**
	 * Gets the related application's identification.
	 * @see planet.commonapi.RouteMessage#getApplicationId()
	 * @return Identification of the application
	 */
	public String getApplicationId() {
		return appId;
	}
	/**
	 * Gets de source Node NodeHandle of the message
	 * @see planet.commonapi.RouteMessage#getSource()
	 * @return Source Node NodeHandle of the message
	 */
	public NodeHandle getSource() {
		return source;
	}
	/**
	 * Sets the application's identification who sends and will receive this message.
	 * @see planet.commonapi.RouteMessage#setApplicationId(java.lang.String)
	 * @param app Application's identification who generates this message.
	 */
	public void setApplicationId(String app) {
		this.appId = app;
	}
	
	/**
	 * Sets the source Node NodeHandle who sends this message. 
	 * @see planet.commonapi.RouteMessage#setSource(planet.commonapi.NodeHandle)
	 * @param handle The Node NodeHandle who sends this message through de ring.
	 */
	public void setSource(NodeHandle handle) {
		this.source = handle;
	}

	/**
	 * Gets the type of this message.
	 * @see planet.commonapi.RouteMessage#getType()
	 * @return Type of this message
	 */
	public int getType() {
		return type;
	}

	/**
	 * Gets the mode of this message.
	 * @see planet.commonapi.RouteMessage#getMode()
	 * @return Mode of this message.
	 */
	public int getMode() {
		return mode;
	}

	/**
	 * Gets the identification key of this message. This key is
	 * different between any pair of sent meesages to the same destination node.
	 * @see planet.commonapi.RouteMessage#getKey()
	 * @return Identification key of this message.
	 */
	public String getKey() {
		return key;
	}

	/**
	 * Sets the type of this message.
	 * @see planet.commonapi.RouteMessage#setType(int)
	 * @param type Type of message.
	 */
	public void setType(int type) {
		this.type = type;
	}

	/**
	 * Sets the mode of this messge.
	 * @see planet.commonapi.RouteMessage#setMode(int)
	 * @param mode Mode of message.
	 */
	public void setMode(int mode) {
		this.mode = mode;
	}

	/**
	 * Sets the identification key of this communication.
	 * @see planet.commonapi.RouteMessage#setKey(java.lang.String)
	 * @param key Identification key of this communication.
	 */
	public void setKey(String key) {
		this.key = key;
	}
	
     /**
     * Sets all values of the actual RouteMessage, before to be sent.
     * @param key Identification key for this communication with the remote node.
     * @param from Source Id of this RouteMessage.
     * @param to Destination Id of this RouteMessage.
     * @param nh NodeHandle for next hop.
     * @param type Type of this message.
     * @param mode Mode of this message.
     * @param msg Message wrapped to this RouteMessage.
     * @param appId Identification of the Application which generate this
     * message.
     * @see planet.commonapi.RouteMessage#setValues(java.lang.String, planet.commonapi.NodeHandle, planet.commonapi.NodeHandle, planet.commonapi.NodeHandle, int, int, planet.commonapi.Message, java.lang.String)
     */
    public void setValues(String key, NodeHandle from, NodeHandle to,
            NodeHandle nh, int type, int mode, Message msg, String appId) {
		this.appId = appId;
		this.destination = to;
		this.source = from;
		this.nextHop = nh;
		this.message = msg;
		this.key = key;
		this.type = type;
		this.mode = mode;
		route = new Vector();
	}

	/**
	 * Reverse path. The destination become source and source become destination.
	 */
	public void inverse() {
		NodeHandle temp = destination;
		destination = source;
		source = destination;
	}
	
	
	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "RouteMessageImpl: \n\tFrom:       "+this.source+
		  ";\n\tDestination:"+this.destination+
		  ";\n\tNextHop:    "+this.nextHop+
		  ";\n\tKey:"+this.key+
		  ";\n\tType:"+Globals.typeToString(this.type)+
		  ";\n\tMode:"+Globals.modeToString(this.mode)+
		  ";\n\tOptions:"+this.options+
		  ";\n\tContent:"+this.message;
	}
	
	/* **************** ADDITIONAL METHODS ***********************/
	/**
	  * Gets the previous hop handle for this message.
	  * @return The previous hop.
	  */
	 public NodeHandle getPreviousHopHandle() {
	 	return this.previousHop;
	 }
	 
	 /**
	  * Sets the previous hop handle for this message.
	  * @param previousHop The previous hop to Set. 
	  */
	 public void setPreviousHopHandle(NodeHandle previousHop) {
	 	this.previousHop = previousHop;
	 }
	 
	 /**
	  * Set RouteMessage additional options.
	  * @param o additional options.
	  */
	 public void setOptions(Object o) {
	 	this.options = o;
	 }
	 /**
	  * @return RouteMessage additional options for future use.
	  */
	 public Object getOptions() {
	 	return this.options;
	 }

	 /**
	  * Adds a new node along the current path. 
	  * @param node the new node to be added.
	  */
	 public void setNewNodeAlongRoute(NodeHandle node) {
	 	route.add(node);
	 }
	 
	 /**
	  * Returns the route from the source to the destination. 
	  * @return returns the route from the source to the destination.
	  */
	 public java.util.Collection getRoute() {
	 	return route;
	 }
	 
	 /**
	  * Sets the route for the new path from the source to the destination
	  * @param route route from the source to the destination.
	  * @throws planet.commonapi.exception.InitializationException.
	  */
	 public void setRoute(Collection route) throws planet.commonapi.exception.InitializationException {
	 	if (route == null) throw new planet.commonapi.exception.InitializationException("Route is null");
	  	Iterator it = route.iterator();
	 	while(it.hasNext())
	 		this.route.add((NodeHandle) it.next()); 
	 }
	 /**
	  * Sets Time to Live For The Message.
	  * @param ttl time to live number.
	  */
	 public void setTimeToLive(int ttl) {
	 	this.ttl = ttl;
	 }
	 /**
	  * Gets the time to live for the packet.
	  * @return returns the time to live for the packet.
	  */
	 public int getTimeToLive() {
	 	return this.ttl;
	 }
	 /**
	  * Returns true if RouteMessage has expired.
	  * @return true if RouteMessage has finished its time to live.
	  */
	 public boolean routeMessagesHasExpired() {
	 	return this.ttl <= 0;
	 }
	 /**
	  * Decreases the Time to Live for the packet.
	  */
	 public void decTimeToLive() {
	 	this.ttl--;
	 }

}
