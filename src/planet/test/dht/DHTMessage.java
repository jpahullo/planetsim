package planet.test.dht;

import java.util.Vector;

import planet.commonapi.NodeHandle;

/**
 * Message to be used at DHTTest main application. It permits
 * two operations:
 * <ol>
 * <li>Insert: Insert a key/value pair.</li>
 * <li>Lookup: Lookup a key at ring.</li>
 * </ol>
 * @author Carles Pairot   <cpairot@etse.urv.es>
 * @author Jordi Pujol     <jordi.pujol@estudiants.urv.es>
 * @version 1.0
 */
public class DHTMessage implements planet.commonapi.Message {
  private NodeHandle originNode = null;
  private String key = null;
  private String value = null;
  private Vector vValue = null;
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
   * @param origNode
   * @param type
   */
  public DHTMessage (NodeHandle origNode, int type) {
    originNode = origNode;
    this.type = type;
  }
  
  /**
   * Builds a new Message with the specified source Node and type,
   * containing initially the specified <b>key/value</b> pair.
   * @param origNode Source node.
   * @param type Type of message
   * @param key Key of the pair.
   * @param value Value of the pair.
   */
  public DHTMessage (NodeHandle origNode, int type, String key, String value) {
    originNode = origNode;
    this.type = type;
    this.key = key;
    this.value = value;
  }

  /**
   * Builds a Message with the specifid source Node, type and key, with value
   * a Vector with all values of the required key.
   * @param origNode Source Node.
   * @param type Type of message.
   * @param key Key of the pair.
   * @param value Value of the pair.
   */
  public DHTMessage (NodeHandle origNode, int type, String key, Vector value) {
    originNode = origNode;
    this.type = type;
    this.key = key;
    this.vValue = value;
  }

  public NodeHandle getOriginNode() {
    return originNode;
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
    return vValue;
  }
  
  public String toString() {
  	return "DHTMessage: SourceNode["+originNode+"] Type["+type+"] Key["+key+"] Value["+value+"] VectorValue{"+vValue+"}";
  }
}
