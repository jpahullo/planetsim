package planet.generic.commonapi;

import java.util.Set;
import java.util.Vector;

import planet.commonapi.Application;
import planet.commonapi.EndPoint;
import planet.commonapi.Id;
import planet.commonapi.Node;
import planet.commonapi.NodeHandle;
import planet.commonapi.RouteMessage;
import planet.commonapi.exception.InitializationException;
import planet.generic.commonapi.factory.GenericFactory;
import planet.generic.commonapi.message.DataMessage;
import planet.simulate.Logger;

/**
 * Interface which represents a node in a peer-to-peer system, regardless of
 * the underlying protocol.  This represents the *local* node, upon which applications
 * can call methods.
 * @author Jordi Pujol
 * @author Carles Pairot
 */
public class EndPointImpl implements EndPoint {
	
	private Node node = null; 
	private Application app = null; //application
	
	/**
	 * Builds a no initialized EndPoint. It is required to invoke to the
     * setValues() method.
	 */
	public EndPointImpl() {}
	
	/**
	 * An upcall to inform to the top Application for a new step.
	 * This method is invoked at the end of each simulation step. 
	 * Before this, may arrive any number of application Messages,
	 * depending on your own main application.
	 */
	public void byStep()
	{
	    app.byStep();
	}

	
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
	 * @param hint The destination desired known node. If unknown node, it may be null.
	 */
	public void route(Id id, planet.commonapi.Message message, NodeHandle hint) {
		// Route message if unknown 'hint' or is not alive.
		NodeHandle nextHop = null;
		if (hint!=null && hint.isAlive()) {
			nextHop = hint;
		}
		try {
			node.routeData(getApplicationId(),
					GenericFactory.buildNodeHandle(id,true),
					nextHop,
					new DataMessage(id,message));
		} catch (InitializationException e) {
			Logger.log("Error creating a NodeHandle with message key",Logger.ERROR_LOG);
		}
	}
	
	/**
	 * Sends a broadcast message to the network.
	 * @param key The destination Id of the message.
	 * @param message The message to deliver
	 */
	public void broadcast(Id key, planet.commonapi.Message message) {
		try {
			node.broadcast(getApplicationId(),
					GenericFactory.buildNodeHandle(key,true),
					null,
					new DataMessage(key,message));
		} catch (InitializationException e) {
			Logger.log("Error creating a NodeHandle with message key",Logger.ERROR_LOG);
		}
	}

	/**
	 * Returns a handle to the local node below this endpoint.  This node handle is serializable,
	 * and can therefore be sent to other nodes in the network and still be valid.
	 *
	 * @return A NodeHandle referring to the local node.
	 */
	public NodeHandle getLocalNodeHandle() {
		return node.getLocalHandle(); 
	}

	/**
	 * Schedules a message to be delivered to this application after the provided number of
	 * milliseconds.
	 *
	 * @param message The message to be delivered
	 * @param delay The number of milliseconds to wait before delivering the message
	 */
	public void scheduleMessage(RouteMessage message, long delay) {
		if (delay <= 0) {
			DataMessage msg = (DataMessage)message.getMessage();
			app.deliver(msg.getMessageKey(),msg.getMessage());
            GenericFactory.freeMessage(message);
		} else {
			MessageScheduler ms = new MessageScheduler(message,delay,node);
			ms.start();
		}
	}

	/**
	 * Deliver only the content Message in RouteMessage to the application
	 * and returns if this message must be forward. This method receives
     * a RouteMessage with this structure:
     * <pre>
     * ----------------------------------------------------------------
     * |             RouteMessage                                     |
     * |               ---------------------------------------------- |
     * |               |             DataMessage                    | |
     * |  Route        | ----------  ------------------------------ | |
     * | Message       | |Id (Key)|  |Specific Application Message| | |
     * |  Header       | ----------  ------------------------------ | |
     * |               ---------------------------------------------- |
     * ----------------------------------------------------------------
     * </pre>
	 * @see planet.commonapi.EndPoint#forward(planet.commonapi.RouteMessage)
	 * @param message RouteMessage to be forwarding.
	 */
	public boolean forward(RouteMessage message) {
		return app.forward(((DataMessage)message.getMessage()).getMessage());
	}
	
	/**
	 * Returns the identification of the related Application
	 * @see planet.commonapi.EndPoint#getApplicationId()
	 * @return Identification of the related Application
	 */
	public String getApplicationId() {
		return app.getId();
	}
	
	/**
	 * Gets the associated Application.
	 * @return The associated application.
	 */
	public Application getApplication() {
		return app;
	}

	
	/**
	 * Returns the identification of the related Node
	 * @see planet.commonapi.EndPoint#getId()
	 * @return Identification of the related Node
	 */
	public Id getId() {
		return node.getId();
	}
	
	/**
	 * @see planet.commonapi.EndPoint#replicaSet(planet.commonapi.Id, int)
	 * Returns the replica node set.
	 * The number of successor list is defined in the context.
	 * @param key Key from which obtain the replica set.
	 * @param maxRank Number of nodes in replica set.
	 * @return Up to maxRank nodes as replica set.
	 */
	public Vector replicaSet(Id key, int maxRank) {
		return this.node.replicaSet(key,maxRank);
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
	 */
	public Vector localLookup(Id key, int max, boolean safe) {
		return this.node.localLookup(key,max,safe);
	}
	
	/**
	 * Obtains an unordered list of up to max nodes that are neighbors 
	 * of the local node.
	 * @param max Maximum of nodes to return.
	 * @return Neighbor nodes.
	 */
	public Vector neighborSet(int max) {
		return this.node.neighborSet(max);
	}
	/**
	 * @see planet.commonapi.EndPoint#range(planet.commonapi.NodeHandle, planet.commonapi.Id, planet.commonapi.Id, planet.commonapi.Id)
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
	 * the case of node is not present in the neighbor set returned by replicaSet.
	 */
	public boolean range(NodeHandle node, Id rank, Id leftKey, Id rightKey) {
		return this.node.range(node,rank,leftKey,rightKey); 
	}
	
	
	/******************************   MESSAGE SCHEDULER *****************************/
	/**
	 * Implements an scheduler for sent messages to this application with a specified delay.
	 * @author Jordi Pujol
	 */
	public class MessageScheduler extends java.lang.Thread {
		//Message to send
		private RouteMessage message;
		//Delay to apply at send
		private long delay;
		//Node
		private Node node;
		
		/**
		 * Fixes the message an delay to send the message.
		 * @param message
		 * @param delay
		 */
		public MessageScheduler(RouteMessage message, long delay, Node node) {
			this.message = message;
			this.delay   = delay;
			this.node    = node;
		}
		
		/**
		 * Sends the message after delays the specified time.
		 * @see java.lang.Runnable#run()
		 */
		public void run() {
			//delay
			if (delay > 0) {
				try {
					Thread.sleep(delay);
				} catch (InterruptedException e) {
				}
			}
			//deliver message to application
			DataMessage msg = (DataMessage) message.getMessage();
			app.deliver(msg.getMessageKey(), msg.getMessage());
            GenericFactory.freeMessage(message);
		}
	}
	
	
	/**
	 * Shows a String representatio of this EndPoint
	 * @see java.lang.Object#toString()
	 * @return A String representation of this EndPoint
	 */
	public String toString() {
		return "EndPoint: Application["+this.app+"] over Node["+this.node+"]";
	}

    /**
     * Returns all node links.
     * @return All node links.
     * @see planet.commonapi.EndPoint#getAllLinks()
     */
    public Set getAllLinks() {
        return node.getAllLinks();
    }
    
    /**
     * Sets the initial values for this EndPoint.
     * @param app Uplying application.
     * @param node Underlaying node.
     * @return The same instance, once it has been updated.
     */
    public EndPoint setValues(Application app, Node node)
    {
        this.app = app;
        this.node = node;
        return this;
    }
}
