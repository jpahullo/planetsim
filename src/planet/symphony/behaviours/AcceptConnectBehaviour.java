package planet.symphony.behaviours;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import planet.commonapi.Node;
import planet.commonapi.NodeHandle;
import planet.commonapi.RouteMessage;
import planet.commonapi.behaviours.Behaviour;
import planet.symphony.SymphonyNode;
import planet.symphony.SymphonyProperties;
import planet.util.Properties;
/**
 * AcceptConnectBehaviour agrees to accept a long link connection. Source node calls getNewLongDistance()
 * method while the outcoming connections ratio is less than K. For that, it sends QUERY_CONNECT messages to the
 * aspirants determined by routing primitive and priori unknown. When an aspirant answers (is immediate successor
 * of the key on QUERY_CONNECT message got by Symphony pdf function), it sends to originator of the QUERY_CONNECT
 * message and ACCEPT_CONNECT message. Then the originator checks if the long peer is valid i.e, is not included
 * on neighbour's set. When enough long valid peers are available they form the new outcommingSet and the long
 * connections of old outcommingSet must be closed because it can be stale and no longer used for originator peer.
 * 
 * @author Marc Sanchez <marc.sanchez@estudiants.urv.es>
 * @author Helio Tejedor <heliodoro.tejedor@estudiants.urv.es>
 */
public class AcceptConnectBehaviour implements Behaviour { 
    /* Internal attributes. */
    private SymphonyNode symphony                  = null;
    private List outcommingSet                     = null;
    private List incommingSet                      = null;
    private NodeHandle longHandle                  = null;
    private boolean alreadyInOutcommingSet         = false;
    private Set staleSet                           = null;
    private Iterator it                            = null;
    private RouteMessage newQueryConnect           = null;
    private NodeHandle longPeerWithStaleConnection = null;
    private NodeHandle stale                       = null;
    private RouteMessage close                     = null;
    
	/**
	 * Given a RouteMessage and a Node as input, onMessage's method do execution 
	 * steps for the behaviour. Node should be casted to Node's class in order to
	 * manage data structures and protocol of designed overlay.
	 * @param msg RouteMessage taken as input.
	 * @param node Node taken as input.
	 */
	public void onMessage(RouteMessage msg, Node node) {
		symphony = (SymphonyNode) node;
		
		outcommingSet = symphony.getOutcommingSet();
		incommingSet  = symphony.getIncommingSet();
		
		/* We suppose long links are bidireccional TCP connections.
		 */ 
		longHandle = msg.getSource();
		alreadyInOutcommingSet = outcommingSet.contains(longHandle);

        /* If this new long distance is accepted, the remote node only may contain
         * this local node in its incomming set (this local node only may contain the
         * remote node in the neighbour set or outcomming set).
         * If this new long distance already exists in the neighbour set, 
         * the long distance is closed and attempt to find a new long distance.
		 */
		if (symphony.neighbourSetContains(longHandle)) 
        {
            symphony.sendMessage(symphony.buildMessage(symphony.getLocalHandle(), longHandle, 
                    SymphonyNode.CLOSE_LONG_CONNECT, SymphonyNode.REFRESH, null));
            getNewLongDistance();
            return;
        } else 
        {
            if (incommingSet.contains(longHandle))
            {
                //this case only occurs when two nodes simultaneously send
                //a QUERY_CONNECT to the other one in the same step.
                if (symphony.getId().compareTo(longHandle.getId()) < 0)
                {
                    //do'nt require a CLOSE_CONNECT message
                    incommingSet.remove(longHandle);
                    if (!alreadyInOutcommingSet)
                        outcommingSet.add(longHandle); 
                } else
                {
                    getNewLongDistance();
                    return;
                }
            } else
            {
                if (!alreadyInOutcommingSet)
                    outcommingSet.add(longHandle);
            }
        }
            
		
		/*
		 *  If enough long link's aspirants, the new outcommingSet is the new  long distances set
		 *  and the old outcomming's Set is now stale. Therefore, the peers must close the stale 
		 *  long connections. Otherwise, we call getNewLongDistance() method.
		 *
         */
        if (outcommingSet.size() > SymphonyNode.getLongDistanceNumber()) {
            //the stalest node is the first in the outcomming set (the vector adds at the end)
            stale  = (NodeHandle) outcommingSet.remove(0);
                
            close = symphony.buildMessage(symphony.getLocalHandle(), stale, 
                        SymphonyNode.CLOSE_LONG_CONNECT, SymphonyNode.REFRESH, null);
            symphony.sendMessage(close);
		} else if (outcommingSet.size() < SymphonyNode.getLongDistanceNumber())
            getNewLongDistance();
	}
    
    /**
     * Attempts to send a QUERY_CONNECT for a new long distance link.
     */
    private void getNewLongDistance()
    {
        if (!alreadyInOutcommingSet) 
            symphony.retriesNewLongDistance = 0;
        else { 
            symphony.retriesNewLongDistance++;
            if (symphony.retriesNewLongDistance >= ((SymphonyProperties) Properties.overlayPropertiesInstance).maxRetriesNewLongDistance) {
                return;
            }
        }
        newQueryConnect = symphony.getNewLongDistance();
        if (newQueryConnect != null) 
            symphony.sendMessage(newQueryConnect);
    }
    
	/**
	 * @return Returns the name of behaviour. 
	 */
	public String getName() {
		return "AcceptConnectBehaviour";
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
