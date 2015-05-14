package planet.test.dht2;

import java.util.*;
import planet.commonapi.Id;

/**
 * Message to be used at ChordDHT main application. It permits
 * two operations:
 * <ol>
 * <li>Insert: Insert a key/value pair.</li>
 * <li>Lookup: Lookup a key at ring.</li>
 * </ol>
 * @author Carles Pairot   <cpairot@etse.urv.es>
 * @author Jordi Pujol     <jordi.pujol@estudiants.urv.es>
 * @author Marc Sanchez	   <marc.sanchez@estudiants.urv.es>
 * @version 1.0
 */
public class DHTMessage implements planet.commonapi.Message {
  private Id	 source     = null;
  private String key        = null;
  private String value      = null;
  private Vector MultiValue = null;
  private int type;

  /**
   * Insert message type to be used at constructor.
   */
  public static final int INSERT = 0;
  /**
   * Lookup message type to be used at constructor.
   */
  public static final int LOOKUP = 1;
  /**
   * Builds a new Message with the specified source Node <b>origNode</b>
   * and specified type <b>type</b>
   * @param source The source node where a remote or local application launched an INSERT/LOOKUP operation.
   * @param type Type of DHT operation: INSERT/LOOKUP.
   */
  public DHTMessage (Id source, int type) {
    this.source = source;
    this.type = type;
  }
  
  /**
   * Builds a new Message with the specified source Node and type, key/value pairs.
   * @param source The source node where a remote or local application launched an INSERT/LOOKUP operation.
   * @param type Type of DHT operation: INSERT/LOOKUP
   * @param key Key of the pair.
   * @param value Value of the pair.
   * @param MultiValue List of hash collision for the same <b>key</b> stored on the same node's DHT repository.
   */
  public DHTMessage (Id source, int type, String key, String value, Vector MultiValue) {
    this.source = source;
    this.type   = type;
    this.key    = key;
    this.value  = value;
    this.MultiValue = MultiValue;
  }
  
  public DHTMessage (Id source, int type, String key, String value) {
    this.source = source;
    this.type   = type;
    this.key    = key;
    this.value  = value;
  }
  
  public DHTMessage (Id source, int type, String key, Vector MultiValue) {
    this.source = source;
    this.type   = type;
    this.key    = key;
    this.MultiValue = MultiValue;
  }
  
  public Id getSource() {
    return this.source;
  }
  
  public void setSource(Id source) {
  	this.source = source;
  }

  public int getType() {
    return type;
  }

  public String getKey() {
    return key;
  }

  public String getValue() {
    return value;
  }

  public Vector getVectorValue() {
    return this.MultiValue;
  }
  
  public String toString() {
  	return "SymphonyDHTMessage: SourceNode["+source+"] Type["+type+"] Key["+key+"] Value["+value+"] MuliValue{"+MultiValue+"}";
  }
}
