package planet.test.broadcast;

import planet.commonapi.*;

import java.util.*;

/**
 * @author Carles Pairot   <cpairot@etse.urv.es>
 * @version 1.0
 * Message to be send in the Broadcast Test.
 */
public class DHTPeerTestMessage implements Message {
  private String data = null;
  private Id sourceNodeId = null;
  private Properties props = null;

  /**
   * Constructor for this Message. 
   * @param sourceNodeId Id of the source Node.
   * @param data String with all data to be send.
   */
  public DHTPeerTestMessage (Id sourceNodeId, String data) {
  	this.sourceNodeId = sourceNodeId;
    this.data = data;
  }

  /**
   * Gets the data.
   * @return String with all data of this message.
   */
  public String getData() {
    return data;
  }
  
  /**
   * Sets new data.
   * @param data New data to be set.
   */
  public void setData(String data) {
  	this.data = data;
  }
  
  /**
   * Gets Id of source Node, who sends this message.
   * @return Id of source Node.
   */
  public Id getSourceNodeId() {
  	return sourceNodeId;
  }
  
  /**
   * Sets a new Id of source Node, who sends (re-send)
   * this message.
   * @param id Id of the new source Node.
   */
  public void setSourceNodeId(Id id) {
  	this.sourceNodeId = id;
  }
}