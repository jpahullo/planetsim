package planet.symphony.behaviours;

import planet.commonapi.EndPoint;
import planet.commonapi.Id;
import planet.commonapi.Node;
import planet.commonapi.NodeHandle;
import planet.commonapi.RouteMessage;
import planet.commonapi.behaviours.Behaviour;
import planet.generic.commonapi.message.DataMessage;
import planet.simulate.Results;
import planet.symphony.SymphonyNode;
import planet.test.dht2.DHTMessage;

/**
 * DataBehaviour handles DATA request messages. When a node receives a DATA request addressed to
 * it, then it forwads it to application layer through the commonapi interface.
 * 
 * @author Marc Sanchez <marc.sanchez@estudiants.urv.es>
 * @author Helio Tejedor <heliodoro.tejedor@estudiants.urv.es>
 */
public class DataBehaviour implements Behaviour {
    /* internal attributes */
    private SymphonyNode symphony = null;
    
	/**
	 *  Given a RouteMessage and a Node as input, onMessage's method do execution 
	 * steps for the behaviour. Node should be casted to Node's class in order to
	 * manage data structures and protocol of designed overlay.
	 * @param msg RouteMessage taken as input.
	 * @param node Node taken as input.
	 */
	public void onMessage(RouteMessage msg, Node node) {
        symphony   = (SymphonyNode)node;
        symphony.dispatchDataMessage(symphony.buildMessage(msg),SymphonyNode.REQUEST,SymphonyNode.REFRESH);
	}
	
	/**
	 * @return Returns the name of behaviour. 
	 */
	public String getName() {
		return "DataBehaviour";
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
