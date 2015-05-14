package planet.test.broadcast;

import planet.commonapi.*;
import planet.commonapi.exception.InitializationException;
import planet.generic.commonapi.factory.GenericFactory;

import java.util.*;

/**
 * @author Carles Pairot   <cpairot@etse.urv.es>
 * @author Jordi Pujol     <jordi.pujol@estudiants.urv.es>
 * @version 1.0
 */
public class DHTApplication implements Application {
  private Properties store = null;
  private EndPoint endPoint = null;
  /**
   * Identification of the application.
   */
  public static String applicationId= "DHTApplication";
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
   * Sets a new application id.
   * @param name New application id.
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
   * Makes a new instance of a Message to be sent with
   * the specified data.
   * @param data Data to be sent.
   * @return A new Message to be sent.
   */
  public Message makeTestMessage (String data) {
      return new DHTPeerTestMessage(this.endPoint.getId(),data);
  }

  /**
   * Sets the underlying EndPoint of this application.
   * @param ep Underlying EndPoint to be used in Message comunication.
   */
  public void setEndPoint(EndPoint ep) {
    endPoint = ep;
  }

  /**
   * This method is invoked on applications when the underlying node
   * is about to forward the given message with the provided target to
   * the specified next hop.  Applications can change the contents of
   * the message, specify a different nextHop (through re-routing), or
   * completely terminate the message.
   *
   * @param message The message being sent, containing an internal message
   * along with a destination key and nodeHandle next hop.
   *
   * @return Whether or not to forward the message further
   */
  public boolean forward(Message message) {
    System.out.println ("["+appId+"] over ["+endPoint.getId()+"]: Message forwarding...");
    return true;
  }

  /**
   * This method is called on the application at the destination node
   * for the given id.
   *
   * @param id The destination id of the message
   * @param message The message being sent
   */
  public void deliver (Id id, Message message) {
    if (message instanceof DHTPeerTestMessage) {
        DHTPeerTestMessage mesg = (DHTPeerTestMessage) message;
        System.out.println("["+this.endPoint.getId()+"] has received ["+ mesg.getData()+"] from ["+mesg.getSourceNodeId()+"]");
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
	 * Sets the String identification of this application.
	 * @see planet.commonapi.Application#setId(java.lang.String)
	 * @param appId New identification of this application.
	 */
	public void setId(String appId) {
		this.appId = appId;
	}

	
	/**
	 * Owner DHTApplication method which permits to send a broadcast
	 * Message.
	 * @param textKey Text to be used as material for construct Message Id.
	 * @param mess Message to be send with broadcast.
	 */
	public void send(String textKey, DHTPeerTestMessage mess) {
		try {
			mess.setSourceNodeId(this.endPoint.getId());
			endPoint.broadcast(GenericFactory.buildKey(textKey), mess);
		} catch (InitializationException e) {
			e.printStackTrace();
            System.exit(-1);
		}
	}
	
	/**
	 * Shows a String representation of the actual Application.
	 * @see java.lang.Object#toString()
	 * @return String representation of this Application with its ApplicationId
	 * and its EndPoint reference.
	 */
	public String toString() {
		return "DHTApplication {id=["+this.appId+"] endPoint=["+this.endPoint+"]}";
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
