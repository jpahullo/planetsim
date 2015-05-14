package planet.trivialp2p.behaviours;

import planet.commonapi.Node;
import planet.commonapi.RouteMessage;
import planet.commonapi.behaviours.Behaviour;
import planet.trivialp2p.TrivialNode;

/**
 * This behaviour is available for application level messages. It routes
 * to the successor (in a clockwise proximity) up to the destination node,
 * where the message is delivered.
 * @author <a href="mailto: jordi.pujol@estudiants.urv.es">Jordi Pujol</a>
 * 03-jun-2005
 */
public class DataBehaviour implements Behaviour {
    /* Internal atributes. */
    private TrivialNode trivialNode = null;
    
    /**
     * Gets the behaviour name.
     * @return The behaviour name.
     * @see planet.commonapi.behaviours.Behaviour#getName()
     */
    public String getName() {
        return "DataBehaviour";
    }
    
    /**
     * Returns the behaviour name.
     * @return The behaviour name.
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
        return getName();
    }
    
    /**
     * This method treat any application level message, the only available ones
     * in this TrivialP2P overlay implementation.
     * @param msg A RouteMessage that contains an application level message.
     * @param node The local node.
     * @see planet.commonapi.behaviours.Behaviour#onMessage(planet.commonapi.RouteMessage, planet.commonapi.Node)
     */
    public void onMessage(RouteMessage msg, Node node) {
        trivialNode = (TrivialNode)node;
        //it is necessary build a copy; the 'msg' is free at the end of its process
        trivialNode.dispatchDataMessage(trivialNode.buildMessage(msg),TrivialNode.REQUEST,TrivialNode.REFRESH);
    }
}
