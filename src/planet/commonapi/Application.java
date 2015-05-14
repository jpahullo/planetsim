package planet.commonapi;

/**
 * Interface which an application must implement in order to run on top of
 * the EndPoint interface. 
 * <p>
 * Any implementation must include a no argument constructor.
 * </p>
 * @author <a href="mailto: jordi.pujol@estudiants.urv.es">Jordi Pujol</a>
 * 07-jul-2005
 */
public interface Application extends java.io.Serializable {
	
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
	  public boolean forward(Message message);

	  /**
	   * This method is called on the application at the destination node
	   * for the given id.
	   *
	   * @param id The destination node id of the message
	   * @param message The message being sent
	   */
	  public void deliver(Id id, Message message);

	  /**
	   * Gets the identification of this application.
	   * @return Identification of this application.
	   */
	  public String getId();
	  
	  /**
	   * Sets the String identification of this application.
	   * @param appId Identification of this application.
	   */
	  public void setId(String appId);


	  /**
	   * Sets the EndPoint which permits communicate with underlying node.
	   * @param endPoint EndPoint to contact that knows how talk with
	   * underlying node.
	   */
	  public void setEndPoint(EndPoint endPoint);
	  
	  /**
	   * Informs to the application that the node has either joined or
	   * left the neighbor set of the local node, as that set
	   * would be returned by the neighborSet call.
	   * @param node Node that has joined or left the neighbor set.
	   * @param joined If true, the node has joined to the neighbor set.
	   * In other case, the node has left the neighbor set.
	   */
	  public void update(NodeHandle node, boolean joined);
		
	  /**
	   * An upcall to inform this Application for a new step.
	   * This method is invoked at the end of each simulation step. 
	   * Before this, may arrive any number of application Messages,
	   * depending on your own main application.
	   */
	  public void byStep();
      
      /**
       * Sets the name for this applicaton.
       * @param applicationName Name for this application.
       * @return The same instance, once it has been updated.
       */
      public Application setValues(String applicationName);
}
