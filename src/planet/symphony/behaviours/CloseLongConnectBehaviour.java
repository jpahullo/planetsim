package planet.symphony.behaviours;

import java.util.List;

import planet.commonapi.Node;
import planet.commonapi.NodeHandle;
import planet.commonapi.RouteMessage;
import planet.commonapi.behaviours.Behaviour;
import planet.results.LinkStateResults;
import planet.symphony.SymphonyNode;

/**
 * CloseLongConnectBehaviour attemps to handle a CLOSE_LONG_CONNECT request. Two cases are possible:
 * 		~ Node is stable: it has lost a long link and must send a new QUERY_CONNECT message in order
 *                        to get K links.
 *      ~ Node is not stable: it is already trying to get K long links, so not a new QUERY_CONNECT
 *                        message is necessary.
 * @author Marc Sanchez <marc.sanchez@estudiants.urv.es>
 * @author Helio Tejedor <heliodoro.tejedor@estudiants.urv.es> 
 */
public class CloseLongConnectBehaviour implements Behaviour  { 
    /* internal attributes */
    private SymphonyNode symphony = null;
    private NodeHandle sourceHandle = null;
    private List outcommingSet = null;
    private List incommingSet  = null;
    private boolean outRemoved;
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
		
		sourceHandle = msg.getSource();
		
		outcommingSet = symphony.getOutcommingSet();
		incommingSet  = symphony.getIncommingSet();
		
		//LinkStateResults.updateHopsByTypeOf(msg);
		incommingSet.remove(sourceHandle);
		outRemoved = outcommingSet.remove(sourceHandle);
		if (outRemoved) {
			// Node is stable and has lost a long link. Therefore, it should send a new QUERY_CONNECT.
			newQueryConnect = symphony.getNewLongDistance();
			if (newQueryConnect == null) return;
			//LinkStateResults.updateOutcoming(symphony.getId());
            symphony.sendMessage(newQueryConnect);
		}
	}
	/**
	 * @return Returns the name of behaviour. 
	 */
	public String getName() {
		return "CloseLongConnectBehaviour";
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
