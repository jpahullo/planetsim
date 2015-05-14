package planet.test.helloworld;
import planet.commonapi.*;
import planet.commonapi.exception.InitializationException;
import planet.generic.commonapi.factory.GenericFactory;

/**
 * This application only make two tasks: send a "Hello World" message and
 * receive it.
 * 
 * @author Carles Pairot <cpairot@etse.urv.es>
 * @author Jordi Pujol <jordi.pujol@estudiants.urv.es>
 * @version 1.0
 */
public class DHTApplication implements Application {
	private EndPoint endPoint = null;
	/**
	 * Identification of the application.
	 */
	public static String applicationId = "DHTApplication";
	/**
	 * Identification of the application instance.
	 */
	private String appId = applicationId;
	/**
	 * Constructor
	 */
	public DHTApplication() {
	}
	
	/**
	 * An upcall to inform this Application for a new step.
     * This method is invoked at the end of each simulation step. 
	 * Before this, may arrive any number of application Messages,
	 * depending on your own main application.
	 */
	public void byStep(){}
	
	/**
	 * Build a new DHTPeerTestMessage with the specified data.
	 * 
	 * @param data
	 *            Data to be sent with the new message.
	 * @return New generated Message with the data.
	 */
	public Message makeTestMessage(String data) {
		return new DHTPeerTestMessage(data);
	}
	/**
	 * Sets the underlying EndPoint for interact with the Node, for sending and
	 * receiving messages.
	 * 
	 * @param ep
	 *            EndPoint to interact.
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
	 * 
	 * @param message
	 *            Application Message to test if is forwarding or not.
	 * 
	 * @return Whether or not to forward the message further. Always true.
	 */
	public boolean forward(Message message) {
		System.out.println("[" + appId + "] sobre [" + endPoint.getId()
				+ "]: Fent forward del missatge...");
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
	public void deliver(Id id, Message message) {
		if (message instanceof DHTPeerTestMessage) {
			DHTPeerTestMessage mesg = (DHTPeerTestMessage) message;
			System.out.println("Delivered Message: "
					+ ((DHTPeerTestMessage) message).getData());
			System.out.println("Destination Node : " + this.endPoint.getId());
			System.out.println("Message Id       : " + id);
		}
	}
	/**
	 * Gets the identification of the Application
	 * 
	 * @see planet.commonapi.Application#getId()
	 */
	public String getId() {
		return appId;
	}
	/**
	 * Sets the identification of the Application
	 * 
	 * @see planet.commonapi.Application#setId(java.lang.String)
	 * @param appId
	 *            New String identification of this application.
	 */
	public void setId(String appId) {
		this.appId = appId;
	}
	/**
	 * This method permits to send a message with the specified key <b>textKey
	 * </b>
	 * 
	 * @param textKey
	 *            The String to use as key of the message.
	 * @param mess
	 *            Message to be sent.
	 */
	public void send(String textKey, DHTPeerTestMessage mess) {
		try {
			endPoint.route(GenericFactory.buildKey(textKey), mess, null);
		} catch (InitializationException e) {
			System.out.println("Cannot to be sent the message [" + mess
					+ "] with this key [" + textKey + "]");
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
