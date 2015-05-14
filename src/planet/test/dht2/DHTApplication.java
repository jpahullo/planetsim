package planet.test.dht2;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import planet.commonapi.Application;
import planet.commonapi.EndPoint;
import planet.commonapi.Id;
import planet.commonapi.NodeHandle;
import planet.commonapi.exception.InitializationException;
import planet.generic.commonapi.factory.GenericFactory;

/**
 * Application that contains all key/values pairs, required for owner Node Id.
 * 
 * @author Carles Pairot <cpairot@etse.urv.es>
 * @author Jordi Pujol <jordi.pujol@estudiants.urv.es>
 * @author Marc Sanchez <marc.sanchez@estudiants.urv.es>
 */
public class DHTApplication implements Application {
	public static final String APPLICATION_ID = "SymphonyDHTApplication";
	private String appId = APPLICATION_ID;
	private Id id;
	private Hashtable data = new Hashtable();
	private EndPoint endPoint = null;
	/**
	 * Constructor
	 */
	public DHTApplication() {
	}
	/**
	 * Simple constructor where is specified the application identification.
	 * 
	 * @param name New name to set to this application.
	 */
	public DHTApplication(String name) {
		this.appId = name;
	}
	
	/**
	 * An upcall to inform this Application for a new step.
     * This method is invoked at the end of each simulation step. 
	 * Before this, may arrive any number of application Messages,
	 * depending on your own main application.
	 */
	public void byStep(){}
	
	/**
	 * This method is invoked on applications when the underlying node is about
	 * to forward the given message with the provided target to the specified
	 * next hop. Applications can change the contents of the message, specify a
	 * different nextHop (through re-routing), or completely terminate the
	 * message.
	 * 
	 * @param message The message being sent, containing an internal message along
	 * with a destination key and nodeHandle next hop.
	 * 
	 * @return Whether or not to forward the message further
	 */
	public boolean forward(planet.commonapi.Message message) { 	return true; }
	
	/**
	 * This method stores key/value pair on the distributed repository of DHT application.
	 * @param msg The message exchanged between applications. 
	 */
	public void store (DHTMessage msg) {
		String key = msg.getKey();
		if (data.containsKey(key)) { 
			Vector members = (Vector) data.get(key);
			members.add(msg.getValue());
		} else { 
			Vector members = new Vector();
			members.add(msg.getValue());
			data.put(key, members);
		}
		System.out.println("\n\tINFO: '" + msg.getValue() + "' Stored At '" + this.id + "'\n");
	}
	/**
	 * This method retrieves value from a key from the dsitributed repository build on top of
	 * Symphony Lookup protocol.
	 * @param msg The message exchanged between applications.
	 */
	public void retrieve(DHTMessage msg) {
		String key = msg.getKey();
		if (msg.getVectorValue() == null) {
			DHTMessage reply = new DHTMessage(this.id, DHTMessage.LOOKUP, key, (Vector) data.get(key));
			endPoint.route(msg.getSource(), reply, null);
		} else {
			try {
				Id target = GenericFactory.buildId(key, "SHA");
				System.out.println("\n\tINFO: '" + msg.getVectorValue() + "' Binded to '" + target + "'\t....OK\n");
			} catch (InitializationException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * This method is called on the application at the destination node for the
	 * given id.
	 * 
	 * @param id The destination id of the message
	 * @param message The message being sent
	 */
	public void deliver(Id id, planet.commonapi.Message message) {
		DHTMessage msg = (DHTMessage) message;
		switch(msg.getType()) {
		 case DHTMessage.INSERT: store(msg);
		 break;
		 case DHTMessage.LOOKUP: retrieve(msg);
		 break;
		}
	}
	/**
	 * Generate a string with a representation of keys and values existing at
	 * this application.
	 * 
	 * @return A string representation of keys and values existing at this
	 *         application.
	 */
	public String gatherStatistics() {
		Set s = data.keySet();
		String str = "-------------------------------------------------------------\nNode "
				+ this.id
				+ ". Contents in this node (hash collisions): "
				+ s.size() + "\n\n";
		if (s.size() != 0) {
			str += "Contents list for node "
					+ this.id
					+ "\n-------------------------------------------------------------\n";
			Iterator i = s.iterator();
			while (i.hasNext()) {
				String k = (String) i.next();
				Iterator it = ((Vector) data.get(k)).iterator();
				str += k + ";{";
				while (it.hasNext()) {
					str += (String) it.next() + ", ";
				}
				str += "}\n";
			}
		}
		return str;
	}
	/**
	 * Gets the identification of this instance of Application.
	 * 
	 * @see planet.commonapi.Application#getId()
	 */
	public String getId() {
		return appId;
	}
	/**
	 * Sets the identification of this instance of Application.
	 * 
	 * @see planet.commonapi.Application#setId(java.lang.String)
	 * @param appId
	 *            New identification of this instance of Application.
	 */
	public void setId(String appId) {
		this.appId = appId;
	}
	/**
	 * Sets the actual EndPoint to use to communication with underlying
	 * network.
	 * 
	 * @see planet.commonapi.Application#setEndPoint(planet.commonapi.EndPoint)
	 */
	public void setEndPoint(EndPoint endPoint) {
		this.endPoint = endPoint;
		this.id = endPoint.getId();
	}
	/**
	 * Owner SymphonyDHTApplication method which permits to send a Message with a
	 * key/value pair, for INSERT. IMPORTANT: the <b>key </b> is in hexadecimal
	 * format.
	 * 
	 * @param key
	 *            Text to be used as material for construct Message Id, in
	 *            hexadecimal format.
	 * @param value
	 *            Related value to that key.
	 */
	public void insert(String key, String value) {
		try {
			DHTMessage msg = new DHTMessage(this.id, DHTMessage.INSERT, key, value);
			Id target = GenericFactory.buildId(key, "SHA");
			System.out.println(" INSERT '" + target + "'\t Tied To '" + value + "'");
			endPoint.route(target, msg, null);
		} catch (InitializationException e) {
			e.printStackTrace();
		}
	}
	/**
	 * Owner SymphonyDHTApplication method which permits to send a Message with a
	 * key/value pair, for LOOKUP. IMPORTANT: the <b>key </b> is in hexadecimal
	 * format.
	 * 
	 * @param key
	 *            Text to be used as material for construct Message Id, in
	 *            hexadecimal format.
	 */
	public void lookup(String key) {
		try {
			DHTMessage msg = new DHTMessage(this.id, DHTMessage.LOOKUP, key, (Vector) null);
			Id target = GenericFactory.buildId(key, "SHA");
			System.out.println(" LOOKUP Of Target '" + target + "'");
			endPoint.route(target, msg, null);
		} catch (InitializationException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Shows that the node has been <b>joinded</b> or not to the network.
	 * @see planet.commonapi.Application#update(planet.commonapi.NodeHandle, boolean)
	 * @param node Node that has been <b>joined</b> or leaved.
	 * @param joined If true, the <b>node</b> has been joinded. If false, the
	 * <b>node</b> has been leaved to the network.
	 */
	public void update(NodeHandle node, boolean joined) {
	}

    /**
     * Sets the name for this applicaton.
     * @param applicationName Name for this application.
     * @return The same instance, once it has been updated.
     */
    public Application setValues(String applicationName)
    {
        this.appId = applicationName;
        return this;
    }

}
