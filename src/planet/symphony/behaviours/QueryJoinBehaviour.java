package planet.symphony.behaviours;

import java.util.Collection;

import planet.commonapi.Node;
import planet.commonapi.NodeHandle;
import planet.commonapi.RouteMessage;
import planet.commonapi.behaviours.Behaviour;
import planet.results.LinkStateResults;
import planet.symphony.SymphonyNode;
import planet.symphony.messages.*;

/**
 * This behaviour accepts QUERY_JOIN requests. The node which receives the
 * QUERY_JOIN request is the immediate successor of the newbie's Id. Hence,
 * it handles incoming JOIN requests. It sends a SET_INFO message to newbie
 * with its neighbour's Set. 
 * 
 * @author Marc Sanchez <marc.sanchez@estudiants.urv.es>
 * @author Helio Tejedor <heliodoro.tejedor@estudiants.urv.es>
 */
public class QueryJoinBehaviour implements Behaviour { 
    /* internal attributes */
    private SymphonyNode symphony = null;
    private JoinMessage joinMsg = null;
    private NodeHandle nodeHandle = null;
    private NodeHandle newNode    = null;
    private Collection neighbourSet = null;
    private NeighbourMessage newNeighbours = null;
    private RouteMessage newNeighbour = null;
    
	/**
	 *  Given a RouteMessage and a Node as input, onMessage's method do execution 
	 * steps for the behaviour. Node should be casted to Node's class in order to
	 * manage data structures and protocol of designed overlay.
	 * @param msg RouteMessage taken as input.
	 * @param node Node taken as input.
	 */
	public void onMessage(RouteMessage msg, Node node) {
		symphony = (SymphonyNode) node;
		
		joinMsg = (JoinMessage) msg.getMessage();
		
		//LinkStateResults.updateHopsByTypeOf(msg);
		
		// NODEHANDLE
		nodeHandle = symphony.getLocalHandle();
		newNode    = joinMsg.getNewNode();
		
		neighbourSet = symphony.getNeighbourSet();
		newNeighbours = NeighbourMessagePool.getMessage(neighbourSet);
		newNeighbour = symphony.buildMessage(nodeHandle, newNode, newNode, SymphonyNode.SET_INFO,
		           SymphonyNode.REFRESH, newNeighbours);
		symphony.addToNeighbourSet(newNode);
		
		//LinkStateResults.newMessage(newNeighbour);
		//LinkStateResults.updateOutcoming(symphony.getId());
        symphony.sendMessage(newNeighbour);
	}
	/**
	 * @return Returns the name of behaviour. 
	 */
	public String getName() {
		return "QueryJoinBehaviour";
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
