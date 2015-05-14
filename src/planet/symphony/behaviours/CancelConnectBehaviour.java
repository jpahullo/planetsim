package planet.symphony.behaviours;

import planet.commonapi.Node;
import planet.commonapi.RouteMessage;
import planet.commonapi.behaviours.Behaviour;
import planet.symphony.SymphonyNode;
import planet.symphony.SymphonyProperties;
import planet.util.Properties;
/**
 *  CancelConnectBehaviour's executes  when  a prior QUERY_CONNECT message has been rejected.
 *  Then the node attempts to get new long distances until the limit of attempts is achieved. 
 *  Then, the node is conisdered to be stable  with not enough long links. In order to get a 
 *  long links the Symphony's pdf function is used trough the getNewLongDistance method.
 *  
 * @author Marc Sanchez <marc.sanchez@estudiants.urv.es>
 * @author Helio Tejedor <heliodoro.tejedor@estudiants.urv.es>
 */
public class CancelConnectBehaviour  implements Behaviour { 
    private SymphonyNode symphony = null;
    private RouteMessage newQueryConnect = null;
	/**
	 *  Given a RouteMessage and a Node as input, onMessage's method do execution 
	 * steps for the behaviour. Node should be casted to Node's class in order to
	 * manage data structures and protocol of designed overlay.
	 * @param msg RouteMessage taken as input.
	 * @param node Node taken as input.
	 */
	public void onMessage(RouteMessage msg, Node node) {
		symphony = (SymphonyNode) node;
		
		symphony.retriesNewLongDistance++;
		if (symphony.retriesNewLongDistance < ((SymphonyProperties) Properties.overlayPropertiesInstance).maxRetriesNewLongDistance) { 
			newQueryConnect = symphony.getNewLongDistance();
			if (newQueryConnect != null)
                symphony.sendMessage(newQueryConnect);
		}
	}
    
	/**
	 * @return Returns the name of behaviour. 
	 */
	public String getName() {
		return "CancelConnectBehaviour";
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
