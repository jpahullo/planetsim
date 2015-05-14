package planet.symphony.behaviours;

import java.util.List;

import planet.commonapi.Node;
import planet.commonapi.NodeHandle;
import planet.commonapi.RouteMessage;
import planet.commonapi.behaviours.Behaviour;
import planet.symphony.SymphonyNode;

/**
 * QueryConnectBehaviour determines if the QUERY_CONNECT issuer can connect or not  to a 
 * remote node. This Behaviour is provided in order to stablish long links between nodes. 
 * The behaviour checks if already holds a connection from the incoming  node looking up
 * to outcoming, incoming  and neighbour sets. Otherwise, it  accepts the new connection 
 * only if the incoming connection's rate is fewer than K.     
 * 
 * @author Marc Sanchez <marc.sanchez@estudiants.urv.es>
 * @author Helio Tejedor <heliodoro.tejedor@estudiants.urv.es>
 */
public class QueryConnectBehaviour implements Behaviour { 
    
    /* internal atributes */
    private SymphonyNode symphony   = null;
    private NodeHandle sourceHandle = null;
    private String key              = null;
    private List outcommingSet      = null;
    private List incommingSet       = null;
    private boolean accepted        = false;
    private RouteMessage toSend     = null;
    
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
		key = msg.getKey();
		
		outcommingSet = symphony.getOutcommingSet();
		incommingSet  = symphony.getIncommingSet();
		
        if (symphony.neighbourSetContains(sourceHandle)  //also contains the local Id
				|| outcommingSet.contains(sourceHandle)) {
			accepted = false;
		} else if (incommingSet.contains(sourceHandle)) {
			accepted = true;
		} else if (incommingSet.size() < SymphonyNode.getLongDistanceNumber()) {
            incommingSet.add(sourceHandle);
            accepted = true;
		} else accepted = false;
		
        toSend = symphony.buildMessage(msg);
		toSend.setType(accepted ? SymphonyNode.ACCEPT_CONNECT : SymphonyNode.CANCEL_CONNECT );
		toSend.setDestination(toSend.getSource());
        toSend.setNextHopHandle(toSend.getSource());
		toSend.setSource(symphony.getLocalHandle());
		//planet.results.LinkStateResults.newMessage(toSend);
		//planet.results.LinkStateResults.updateOutcoming(symphony.getId());
        symphony.sendMessage(toSend);
	}
	/**
	 * @return Returns the name of behaviour. 
	 */
	public String getName() {
		return "QueryConnectBehaviour";
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
