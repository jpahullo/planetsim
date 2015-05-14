package planet.symphony.behaviours;

import planet.commonapi.Node;
import planet.commonapi.RouteMessage;
import planet.commonapi.behaviours.Behaviour;
import planet.results.LinkStateResults;
import planet.symphony.SymphonyNode;
import planet.symphony.messages.NeighbourMessage;
import planet.symphony.messages.NeighbourMessagePool;
/**
 * This behaviours updates successor's and predecessor's set of
 * newcomer on bootstrapping. 
 * 
 * @author Marc Sanchez 	<marc.sanchez@estudiants.urv.es>
 * @author Helio Tejedor <heliodoro.tejedor@estudiants.urv.es>
 */
public class SetInfoBehaviour implements Behaviour { 
    private SymphonyNode symphony         = null;
    private NeighbourMessage setNeighbour = null;
    private boolean alreadyAsNeighbour    = false;
    
	/**
	 *  Given a RouteMessage and a Node as input, onMessage's method do execution 
	 * steps for the behaviour. Node should be casted to Node's class in order to
	 * manage data structures and protocol of designed overlay.
	 * @param msg RouteMessage taken as input.
	 * @param node Node taken as input.
	 */
	public void onMessage(RouteMessage msg, Node node) {
		symphony = (SymphonyNode) node;
		//LinkStateResults.updateHopsByTypeOf(msg);
		setNeighbour = (NeighbourMessage) msg.getMessage();
        
        alreadyAsNeighbour = symphony.neighbourSetContains(msg.getSource());
        if (!symphony.addToNeighbourSet(setNeighbour.getNeighbourhoodSet(),true)
                && !symphony.neighbourSetContains(msg.getSource()) && !alreadyAsNeighbour)
        {
            symphony.sendMessage(symphony.buildMessage(symphony.getLocalHandle(), msg.getSource(), msg.getSource(), SymphonyNode.CLOSE_NEIGHBOUR_CONNECT,
                      SymphonyNode.REFRESH, NeighbourMessagePool.getMessage(symphony.getFarthestNeighbours())));
        }
        NeighbourMessagePool.freeMessage(setNeighbour);
	}
    
	/**
	 * @return Returns the name of behaviour. 
	 */
	public String getName() {
		return "SetInfoBehaviour";
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
