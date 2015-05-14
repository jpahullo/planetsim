package planet.generic.commonapi;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;

import planet.commonapi.Application;
import planet.commonapi.EndPoint;
import planet.commonapi.Id;
import planet.commonapi.NodeHandle;
import planet.commonapi.RouteMessage;
import planet.commonapi.exception.InitializationException;
import planet.generic.commonapi.factory.GenericFactory;
import planet.simulate.Logger;
import planet.simulate.MessageListener;
import planet.util.Properties;
import planet.util.Queue;
import planet.util.QueueFull;
import planet.util.timer.ThreadTimer;
import planet.util.timer.Timer;
import planet.util.timer.TimerTask;

/**
 * Superclass which represents a node in a peer-to-peer system, regardless of
 * the underlying protocol. All nodes, implement the methods of this class.
 * 
 * This contains all required elements to use java.util.Timer (JDK standard)
 * for real execution context. Only the required concrete Node implementation
 * must extend this abstract class, instead of planet.generic.commonapi.NodeImpl.
 * Another required action is shows at properties file for simulator the
 * class to use for Timer as follows:
 *  
 * # Default Timer Class
 * DEFAULT_TIMER = planet.util.timer.ThreadTimer
 * 
 * @author Pedro García
 * @author Carles Pairot
 * @author Ruben Mondejar
 * @author Jordi Pujol
 */
public abstract class NetworkNodeImpl
		implements
			planet.commonapi.Node,
			java.io.Serializable {
	
	protected Id id;
	protected transient Hashtable listeners;
	private transient Queue incoming;
	private transient Queue outgoing;
	private int processed = 0;
	protected boolean alive = true;
	/**
	 * Timer to process each TimerTask.
	 */
	private Timer timer = null;
	/**
	 * NodeHandle for the actual Node.
	 */
	protected NodeHandle nodeHandle = null;
	/**
	 * Local EndPoints.
	 */
	protected Hashtable endpoints;
	
	
	/**
	 * Constructor, create a new Node instance with node Id
	 * 
	 * @param id
	 *            Id
	 */
	public NetworkNodeImpl(Id id) throws InitializationException {
		this.id = id;
		try {
			timer = new ThreadTimer();
		} catch (Exception e) {
			throw new InitializationException("Cannot build the Node's Timer", e);
		}
		nodeHandle = GenericFactory.buildNodeHandle(id, true);
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
	public abstract void join(Id bootstrap);
	
	/**
	 * The node leaves the network
	 */
	public abstract void leave();
	
	/**
	 * Lookup of a key
	 * 
	 * @param key
	 *            Id
	 */
	public abstract void lookup(Id key);
	
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
		return true;
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
	 * Puts a message in the outcoming queue of this node
	 * 
	 * @param msg
	 *            sended Message
	 */
	public void send(RouteMessage msg) throws QueueFull {
		try {
			outgoing.add(msg);
		} catch (QueueFull e) {
			System.exit(0);
		}
		Logger.logSend(id, msg, Logger.MSG_LOG);
	}
	
	/**
	 * Returns the present outgoing queue of this node
	 * 
	 * @return outgoing Queue of Messages
	 */
	public Queue outMessages() {
		return outgoing;
	}
	
	/**
	 * Checks if the incoming queue have a messages to send
	 * 
	 * @return return true if has incoming messages to process
	 */
	protected boolean hasMoreMessages() {
		return processed < Properties.simulatorQueueSize && processed < incoming.size();
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
	 * Given a time fraction, the node runs and do everything what needs during
	 * this
	 * 
	 * @param time
	 *            fraction
	 */
	protected void run(int time) {
		processed = 0;
		if (alive) {
			process(time);
		}
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
	 * Its value is the absolute time, not relative for the actual time.
	 * @param period Number of steps or millis to periodicly execute the task.
	 */
	public void setTimer(TimerTask task, long firstTime, long period) {
		this.timer.setTimerTask(task,firstTime,period);
	}
	
	/**
	 * Sets a task to be executed only one time at specified <b>firstTime</b>.
	 * @see planet.commonapi.Node#setTimer(planet.util.timer.TimerTask, long)
	 * @param task Job to do at activation.
	 * @param firstTime Moment to be activated, in steps or millis, measured
	 * in absolute time, not relative of actual time. 
	 */
	public void setTimer(TimerTask task, long firstTime) {
		this.timer.setTimerTask(task,firstTime);
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
}
