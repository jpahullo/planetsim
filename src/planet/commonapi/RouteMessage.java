package planet.commonapi;

/**
 * This interface is a container which represents a message, as it is
 * about to be forwarded to another node. 
 * 
 * @author <a href="mailto: jordi.pujol@estudiants.urv.es">Jordi Pujol</a>
 * @author <a href="mailto: marc.sanchez@urv.net">Marc Sanchez</a>
 * 07-jul-2005
 */
public interface RouteMessage extends java.io.Serializable {
	
	 /**
	  * Returns the application Id for this message
	  *
	  * @return The application Id
	  */
	 public String getApplicationId();

	/**
	  * Returns the destination NodeHandle for this message
	  *
	  * @return The destination NodeHandle
	  */
	 public NodeHandle getDestination();
	 
	 /**
	  * Returns the source NodeHandle for this message
	  *
	  * @return The source NodeHandle
	  */
	 public NodeHandle getSource();
	 
	 /**
	  * Returns the next hop handle for this message.
	  *
	  * @return The next hop
	  */
	 public NodeHandle getNextHopHandle();
	 
	  /**
	  * Returns the enclosed message inside of this message
	  *
	  * @return The enclosed message
	  */
	 public Message getMessage();

	 /**
	  * Gets the identification key of this communication.
	  * @return Identification key of this communication.
	  */
	 public String getKey();
	 
	 /**
	  * Gets the type of the actual Message.
	  * @return The type of the actual Message.
	  */
	 public int getType();
	
	 /**
	  * Gets the mode of the actual Message.
	  * @return The mode of the actual Message.
	  */
	 public int getMode();
	 
	 /**
	  * Sets the application name
	  *
	  * @param app The application name
	  */
	 public void setApplicationId(String app);

	 /**
	  * Sets the destination NodeHandle for this message
	  *
	  * @param handle The destination NodeHandle
	  */
	 public void setDestination(NodeHandle handle);

	 /**
	  * Sets the source NodeHandle for this message
	  *
	  * @param handle The source NodeHandle
	  */
	 public void setSource(NodeHandle handle);


	 /**
	  * Sets the next hop handle for this message
	  *
	  * @param nextHop The next hop for this handle
	  */
	 public void setNextHopHandle(NodeHandle nextHop);
	 
	 /**
	  * Sets the internal message for this message
	  *
	  * @param message The internal message
	  */
	 public void setMessage(Message message); 
	 
	 /**
	  * Sets the identification key of this communication.
	  * @param key key of this communication.
	  */
	 public void setKey(String key);
	    
	 /**
	  * Sets the mode of the actual Message. It is required for
	  * when a destination of any message is unexisting, the message
	  * is automatically returned with mode sets to Globlas.ERROR
	  * @param mode The mode of the actual Message.
	  * @see planet.simulate.Globals Globals
	  */
	 public void setMode(int mode);
	 
	 /**
	  * Sets the type of the actual Message.
	  * @param type The type of the actual Message.
	  */
	 public void setType(int type);
	 
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
	  */
	 public void setValues(String key, NodeHandle from, NodeHandle to,
	 		NodeHandle nh, int type,int mode, Message msg, String appId);
}
