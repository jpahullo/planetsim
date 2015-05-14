package planet.test.dht;
import java.math.BigInteger;
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
 */
public class DHTApplication implements Application {
	public static final String APPLICATION_ID = "DHTApplication";
	private String appId = APPLICATION_ID;
	private String node;
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
	 * @param name
	 *            New name to set to this application.
	 */
	public DHTApplication(String name) {
		this.appId = name;
	}
	/**
	 * Sets the identification of the node over which this instance of
	 * application is running.
	 * 
	 * @param node
	 *            Identification of the local node.
	 */
	public void setNode(String node) {
		this.node = node;
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
	 * @param message
	 *            The message being sent, containing an internal message along
	 *            with a destination key and nodeHandle next hop.
	 * 
	 * @return Whether or not to forward the message further
	 */
	public boolean forward(planet.commonapi.Message message) {
		System.out.println("Forwarding message at node ["
				+ this.endPoint.getId() + "]...");
		return true;
	}
	/**
	 * This method is called on the application at the destination node for the
	 * given id.
	 * 
	 * @param id
	 *            The destination id of the message
	 * @param message
	 *            The message being sent
	 */
	public void deliver(Id id, planet.commonapi.Message message) {
        DHTMessage mesg = (DHTMessage) message;
        boolean found = false;
        //Insert a key with a value
        if (mesg.getType() == DHTMessage.INSERT) {
            Set s = data.keySet();
            Iterator i = s.iterator();
            while (i.hasNext()) {
                String k = (String) i.next();
                if (mesg.getKey().equals(k)) {
                    // This key has already got members
                    Vector v = (Vector) data.get(k);
                    v.add(mesg.getValue());
                    found = true;
                }
            }
            s = null;
            //is the first instance of the key
            if (!found) {
                Vector v = new Vector();
                v.add(mesg.getValue());
                data.put(mesg.getKey(), v);
            }
        }
        // Message of type LOOKUP
        else {
            System.out.println("The local node is["+endPoint.getId()+"], and the mesg.originNode is ["+mesg.getOriginNode().getId()+"]");
            //the response arrive to the source sender node
            if (endPoint.getId().equals(mesg.getOriginNode().getId()))
            {
                //if there is no contents, the key not exits into the DHT
                if (mesg.getVectorValue() == null)
                {
                    System.out.println("Fail: The key {"+mesg.getKey()+
                            "} not found");
                } else
                {
                    System.out.println("Success: The key {"+mesg.getKey()+
                            "} has the values {"+mesg.getVectorValue()+"}");
                }
            }
            //the message arrives to the final receiver node.
            else
            {
                DHTMessage msg = new DHTMessage(mesg.getOriginNode(),
                        DHTMessage.LOOKUP, mesg.getKey(), (Vector) data
                                .get(mesg.getKey()));
                endPoint.route(mesg.getOriginNode().getId(), msg, mesg.getOriginNode());
            }
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
				+ node
				+ ". Contents in this node (hash collisions): "
				+ s.size() + "\n\n";
		if (s.size() != 0) {
			str += "Contents list for node "
					+ node
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
		node = endPoint.getId().toString();
	}
	/**
	 * Owner DHTApplication method which permits to send a Message with a
	 * key/value pair, for INSERT. IMPORTANT: the <b>key </b> is in hexadecimal
	 * format.
	 * 
	 * @param key
	 *            Text to be used as material for construct Message Id, in
	 *            hexadecimal format.
	 * @param value
	 *            Related value to that key.
	 */
	public void send(String key, String value) {
        try {
            DHTMessage mesg = new DHTMessage(endPoint.getLocalNodeHandle(), 
                    DHTMessage.INSERT, key, value);
            endPoint.route(GenericFactory.buildId(new BigInteger(key, 16)),
                    mesg, null);
        } catch (InitializationException e) {
            e.printStackTrace();
            System.exit(-1);
        }
	}
	/**
	 * Owner DHTApplication method which permits to send a Message with a
	 * key/value pair, for LOOKUP. IMPORTANT: the <b>key </b> is in hexadecimal
	 * format.
	 * 
	 * @param key
	 *            Text to be used as material for construct Message Id, in
	 *            hexadecimal format.
	 */
	public void send(String key) {
        try {
            DHTMessage mesg = new DHTMessage(endPoint.getLocalNodeHandle(), 
                    DHTMessage.LOOKUP, key, (Vector) null);
            endPoint.route(GenericFactory.buildId(new BigInteger(key, 16)),
                    mesg, null);
        } catch (InitializationException e) {
            e.printStackTrace();
            System.exit(-1);
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
