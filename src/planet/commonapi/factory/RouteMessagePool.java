package planet.commonapi.factory;

import java.io.Serializable;

import planet.commonapi.Message;
import planet.commonapi.NodeHandle;
import planet.commonapi.RouteMessage;
import planet.commonapi.exception.InitializationException;

/**
 * Shows a pool of RouteMessage, reusing any existing RouteMessage as possible.
 * If it is invoked a <b>getMessage(..)</b> method and no RouteMessage is available,
 * a new instance is built and returned with the specified values.
 * <br><br>
 * When a RouteMessage is free with the <b>freeMessage(RouteMessage)</b> method,
 * you must ensure that there are not references of the specified RouteMessage
 * into the current whole network.
 * <br><br>
 * For a correct RouteMessagePool operation, the addition of built and reused
 * RouteMessage should be the same as the free ones.
 * @author <a href="mailto: jordi.pujol@estudiants.urv.es">Jordi Pujol</a>
 * 06-jul-2005
 */
public interface RouteMessagePool extends Serializable {
    
    /**
     * Return a RouteMessage with the specified values, setting the <b>nextHop</b>
     * field with the <b>to</b> value.
     * @param key Identification of communication.
     * @param from Source node.
     * @param to Destination node.
     * @param type Type of message.
     * @param mode Mode of message. 
     * @return A RouteMessage with the specified values.
     * @throws InitializationException if occurs any error during the 
     * building process.
     */
    public RouteMessage getMessage(String key,NodeHandle from, NodeHandle to,int type, int mode) throws InitializationException;
    
    /**
     * Builds a new instance of RouteMessage with these specified values.
     * @param key Key of the communication.
     * @param from Source node.
     * @param to Destination node.
     * @param nextHop Identifies the next hop node.
     * @param type Type of the message.
     * @param mode Mode of the message.
     * @param msg Message to be send in this RouteMessage.
     * @param appId Application that has build this <b>msg</b>.
     * @return A RouteMessage with all specified values.
     * @throws InitializationException if cannot build a new instance
     * of the RouteMessage.
     */
    public RouteMessage getMessage(String key, 
            NodeHandle from, NodeHandle to, NodeHandle nextHop, 
        int type, int mode, Message msg, String appId) throws InitializationException;
    
    /**
     * To free a message only pushing it to be reused. When this method is
     * invoked, you must ensure that this <b>msg</b> never again will be used
     * with the actual values, and there are not references to this <b>msg</b>
     * into the current whole network.
     * @param msg The RouteMessage to be released.
     */
    public void freeMessage (RouteMessage msg);
    
    /* ************************ STATISTIC METHODS ****************************/
    /**
     * Gets the total number of built RouteMessages.
     * @return The total number of built RouteMessages.
     */
    public int getBuiltRouteMessages();
    /**
     * Gets the total number of reused RouteMessages.
     * @return The total number of reused RouteMessages.
     */
    public int getReusedRouteMessages();
    /**
     * Gets the total number of free RouteMessages.
     * @return The total number of free RouteMessages.
     */
    public int getFreeRouteMessages();
}
