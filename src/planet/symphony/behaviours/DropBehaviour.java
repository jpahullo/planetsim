package planet.symphony.behaviours;

import planet.commonapi.Node;
import planet.commonapi.RouteMessage;
import planet.commonapi.behaviours.Behaviour;

/**
 * This behaviour drops whatever message.
 * @author Marc Sanchez
 */
public class DropBehaviour implements Behaviour { 
    private planet.symphony.SymphonyNode symphony = null;
	/**
	 *  Given a RouteMessage and a Node as input, onMessage's method do execution 
	 * steps for the behaviour. Node should be casted to Node's class in order to
	 * manage data structures and protocol of designed overlay.
	 * @param msg RouteMessage taken as input.
	 * @param node Node taken as input.
	 */
	public void onMessage(RouteMessage msg, Node node) {
		symphony = (planet.symphony.SymphonyNode) node;
		//planet.results.LinkStateResults.updateDropped(symphony.getId());
	}
	/**
	 * @return Returns the name of behvaiour. 
	 */
	public String getName() {
		return "DropBehaviour";
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
