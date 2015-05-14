
package planet.commonapi.behaviours;

import planet.commonapi.Node;
import planet.commonapi.RouteMessage;

/**
* This interface provides a method to add functionality
* to a behvaiour.  
* @author Marc Sanchez
*/
public interface Behaviour extends java.io.Serializable {
	/**
	 *  Given a RouteMessage and a Node as input, onMessage's method do execution 
	 * steps for the behaviour. Node should be casted to Node's class in order to
	 * manage data structures and protocol of designed overlay. 
     * <br><br>
     * IMPORTANT: The original RouteMessage <b>msg</b> must suffer no changes.
     * Any response must be made with a new RouteMessage, obtained from
     * MessagePool.
	 * @param msg RouteMessage taken as input.
	 * @param node Node taken as input.
     * @see planet.generic.commonapi.factory.RouteMessagePoolImpl
	 */
	public void onMessage(RouteMessage msg, Node node);
	/**
	 * @return Returns the name of behaviour. 
	 */
	public String getName();
	/**
	 * @return Returns a string representation of the behaviour. In general, the toString
	 * method returns a string that "textually represents" this behaviour. The result should 
	 * be a concise but informative representation that is easy for a person to read. 
	 */ 
	public String toString();
}
