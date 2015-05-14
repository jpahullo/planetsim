package planet.test.trivialp2ptest;

import planet.commonapi.Application;
import planet.commonapi.EndPoint;
import planet.commonapi.Id;
import planet.commonapi.Message;
import planet.commonapi.NodeHandle;

/**
 * This simple application makes only a one way sending message. 
 * @author Carles Pairot <cpairot@etse.urv.es>
 * @author Jordi Pujol <jordi.pujol@estudiants.urv.es>
 * @version 1.0
 */
public class TrivialApplication implements Application {
	private EndPoint endPoint = null;
	/**
	 * Identification of the application.
	 */
	public static String applicationId = "TrivialApplication";
	/**
	 * Identification of the application instance.
	 */
	private String appId = applicationId;
	/**
	 * Constructor
	 */
	public TrivialApplication() {
	}
	/**
	 * Builds a new application with the specified name as application id.
	 * @param name New name of the application.
	 */
	public TrivialApplication(String name) {
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
	 * Build a new TrivialMessage with the specified data.
	 * @param data The key to be found.
	 * @return New generated Message with the data.
	 */
	public Message makeTestMessage(Id data) {
		return new TrivialMessage(data);
	}
    
	/**
	 * Sets the underlying EndPoint for interact with the Node, for sending and
	 * receiving messages.
	 * @param ep EndPoint to interact.
	 */
	public void setEndPoint(EndPoint ep) {
		endPoint = ep;
	}
	/**
	 * This method is invoked on applications when the underlying node is about
	 * to forward the given message with the provided target to the specified
	 * next hop. Applications can change the contents of the message, specify a
	 * different nextHop (through re-routing), or completely terminate the
	 * message.
	 * @param message Application Message to test if is forwarding or not.
	 * @return Whether or not to forward the message further. Always true.
	 */
	public boolean forward(Message message) {
        //Uncomment for an exhaustive feedback
//		System.out.println("[" + appId + "] over [" + endPoint.getId() + "]: Forwarding message...");
		return true;
	}
	/**
	 * This method is called on the application at the destination node for the
	 * given id.
	 * @param id The destination id of the message
	 * @param message The message being sent
	 */
	public void deliver(Id id, Message message) {
		if (message instanceof TrivialMessage) {
			TrivialMessage mesg = (TrivialMessage) message;
            //Uncomment for an exhaustive feedback
			//System.out.println("The key [" + id+ "] is responsibility of the node [" + endPoint.getId() + "]");
		}
	}
	/**
	 * Gets the identification of the Application
	 * @see planet.commonapi.Application#getId()
	 */
	public String getId() {
		return appId;
	}
	/**
	 * Sets the identification of the Application
	 * @see planet.commonapi.Application#setId(java.lang.String)
	 * @param appId New String identification of this application.
	 */
	public void setId(String appId) {
		this.appId = appId;
	}
	/**
	 * This method permits to send a message with the specified key <b>key</b>
	 * @param key The key to be found.
	 */
	public void find(Id key) {
	    endPoint.route(key, makeTestMessage(key), null);
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
