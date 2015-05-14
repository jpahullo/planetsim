package planet.commonapi.behaviours;

import planet.commonapi.Node;
import planet.commonapi.RouteMessage;

/**
* This interface provides a method filter RouteMessages
* before BehaviourPool attempt to match patterns. Filter can be viewed as
* precondition handler which RouteMessage's has to satisfy.   
* @author Marc Sanchez
*/
public interface BehavioursFilter extends java.io.Serializable {
	/**
	 * Given a RouteMessage and a Node as input, filter's method filters the 
	 * input RouteMessage if does not satisfy filter's precondition. 
	 * @param msg RouteMessage taken as input.
	 * @param node Node taken as input.
	 * @return Returns either an array of RouteMessages or null when no messages
	 * need to transmit this node. 
	 */
	public boolean filter(RouteMessage msg, Node node);
	/**
	 * @return Returns the name of the filter. 
	 */
	public String getName();
	/**
	 * @return Returns a string representation of the filter. In general, the toString
	 * method returns a string that "textually represents" this behaviour. The result should 
	 * be a concise but informative representation that is easy for a person to read. 
	 */ 
	public String toString();
}
