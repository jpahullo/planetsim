package planet.symphony.behaviours;

import planet.commonapi.Node;
import planet.commonapi.NodeHandle;
import planet.commonapi.RouteMessage;


/**
 * This behaviour routes RouteMessages towards RemoteNodes.
 * @author Marc Sanchez
 */
public class RoutingBehaviour implements planet.commonapi.behaviours.Behaviour{ 
    
    /* internal attributes */
    private planet.symphony.SymphonyNode symphony = null;
    private NodeHandle proposed = null;
    private RouteMessage toSend = null;
    
	/**
	 *  Given a RouteMessage and a Node as input, onMessage's method do execution 
	 * steps for the behaviour. Node should be casted to Node's class in order to
	 * manage data structures and protocol of designed overlay.
	 * @param msg RouteMessage taken as input.
	 * @param node Node taken as input.
	 */
	public void onMessage(RouteMessage msg, Node node) {
		symphony = (planet.symphony.SymphonyNode) node;
		proposed = symphony.route(msg.getDestination());
        toSend = symphony.buildMessage(msg,proposed);
        
		//planet.results.LinkStateResults.updateOutcoming(symphony.getId());
		//planet.results.LinkStateResults.updateHopsByTypeOf(toSend);
        symphony.sendMessage(toSend);
	}
	/**
	 * @return Returns the name of behvaiour. 
	 */
	public String getName() {
		return "RoutingBehaviour";
	}
	/**
	 * @return Returns a string representation of the behaviour. In general, the toString
	 * method returns a string that "textually represents" this behaviour. The result should 
	 * be a concise but informative representation that is easy for a person to read. 
	 */ 
	public String toString() {
		return getName();
	}
}
