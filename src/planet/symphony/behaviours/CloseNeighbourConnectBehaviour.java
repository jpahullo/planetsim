package planet.symphony.behaviours;


import planet.commonapi.Node;
import planet.commonapi.NodeHandle;
import planet.commonapi.RouteMessage;
import planet.commonapi.behaviours.Behaviour;
import planet.symphony.SymphonyNode;
import planet.symphony.messages.NeighbourMessage;
import planet.symphony.messages.NeighbourMessagePool;

/**
 * The CloseNeighbourConnectBehaviour is used to close a neighbour connection on the neighbour's set.
 * When a node wants to make a voluntary depart, it sends a CLOSE_NEIGHBOUR_CONNECT to all neighbours
 * in order to notify them it is departing and the connection is going down.
 *
 * @author Marc Sanchez <marc.sanchez@estudiants.urv.es>
 * @author Helio Tejedor <heliodoro.tejedor@estudiants.urv.es>
 */
public class CloseNeighbourConnectBehaviour implements Behaviour  { 
    /* internal attributes */
    private SymphonyNode symphony = null;
    private NodeHandle sourceHandle = null;
    private NeighbourMessage setNeighbour = null;
    
	/**
	 *  Given a RouteMessage and a Node as input, onMessage's method do execution 
	 * steps for the behaviour. Node should be casted to Node's class in order to
	 * manage data structures and protocol of designed overlay.
	 * @param msg RouteMessage taken as input.
	 * @param node Node taken as input.
	 */
	public void onMessage(RouteMessage msg, Node node) {
		symphony = (SymphonyNode) node;
		
		// Remove predecessor from Predecessor's Set.

		sourceHandle = msg.getSource();
        setNeighbour = (NeighbourMessage) msg.getMessage();
        symphony.addToNeighbourSet(setNeighbour.getNeighbourhoodSet(),false);
        symphony.removeNeighbour(sourceHandle);
        NeighbourMessagePool.freeMessage(setNeighbour);
	}
    
	/**
	 * @return Returns the name of behaviour. 
	 */
	public String getName() {
		return "CloseNeighbourConnectBehaviour";
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
