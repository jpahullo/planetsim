package planet.generic.commonapi.factory;

import java.util.Stack;

import planet.commonapi.Message;
import planet.commonapi.NodeHandle;
import planet.commonapi.RouteMessage;
import planet.commonapi.exception.InitializationException;
import planet.commonapi.factory.RouteMessagePool;
import planet.util.Properties;

/**
 * Shows a pool of RouteMessage, reusing any existing RouteMessage as possible.
 * If it is invoked a <b>getMessage(..)</b> method and no RouteMessage is available,
 * a new instance is built and returned with the specified values.
 * <br><br>
 * This implementation and the released overlays in this distribution
 * accomplish the RouteMessagePool operation: the addition of built and reused
 * RouteMessage should be the same as the free ones.
 * @author <a href="mailto: jordi.pujol@estudiants.urv.es">Jordi Pujol</a>
 * 06-jul-2005
 */
public class RouteMessagePoolImpl implements RouteMessagePool {
    
    /**
     * Class reference of the current RouteMessage implementation.
     */
    protected Class routeMessage        = null;
    /**
     * RouteMessages just created.
     */
    private Stack messages           = null;
    /**
     * Only for temporal use.
     */
    private RouteMessage tmp         = null;
    /**
     * Total number of created messages.
     */
    private int createdMessages       = 0;
    /**
     * Total number of free messages.
     */
    private int freeMessages          = 0;
    /**
     * Total number of reused messages.
     */
    private int reusedMessages        = 0;
    
      
    /**
     * Builds a RouteMessagePool. 
     */
    public RouteMessagePoolImpl() 
    {
        routeMessage = Properties.factoriesRouteMessage;
        messages     = new Stack();
    }
      
    /**
     * To free a message only pushing it to be reused. When this method is
     * invoked, you must ensure that this <b>msg</b> never again will be used
     * with the actual values, and there are not references to this <b>msg</b>
     * into the current whole network.
     * @param msg The RouteMessage to be released.
     * @see planet.commonapi.factory.RouteMessagePool#freeMessage(planet.commonapi.RouteMessage)
     */
    public void freeMessage(RouteMessage msg) {
        if (msg == null) return;
        messages.push (msg);
        freeMessages ++;
    }
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
     * @see planet.commonapi.factory.RouteMessagePool#getMessage(java.lang.String, planet.commonapi.NodeHandle, planet.commonapi.NodeHandle, int, int)
     */
    public RouteMessage getMessage(String key, NodeHandle from, NodeHandle to,
            int type, int mode) throws InitializationException {
        if (messages.size() > 0) {
            tmp = (RouteMessage)messages.pop();
            tmp.setValues(key,from,to,to,type,mode,null,"");
            reusedMessages ++;
        } else {
            try {
                tmp = (RouteMessage)routeMessage.newInstance();
                tmp.setValues(key,from,to,to,type,mode,null,"");
                createdMessages ++;
            } catch (Exception e) {
                throw new InitializationException("Cannot generate a new instance of RouteMessage.",e);
            }
        }
        return tmp;
    }
    
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
     * @see planet.commonapi.factory.RouteMessagePool#getMessage(String, NodeHandle, NodeHandle, NodeHandle, int, int, Message, String)
     */
    public RouteMessage getMessage(String key, NodeHandle from, NodeHandle to,
            NodeHandle nextHop, int type, int mode, Message msg, String appId)
            throws InitializationException {
        if (messages.size() > 0) {
            tmp = (RouteMessage)messages.pop();
            tmp.setValues(key,from,to,nextHop,type,mode,msg,appId);
            reusedMessages ++;
        } else {
            try {
              tmp = (RouteMessage)routeMessage.newInstance();
              tmp.setValues(key,from,to,nextHop,type,mode,msg,appId);
              createdMessages ++;
            } catch (Exception e) {
                throw new InitializationException ("Cannot generate a new instance of RouteMessage.",e);
            }
        }
        return tmp;
    }
    
    
    /**
     * Gets the total number of built RouteMessages.
     * @return The total number of built RouteMessages.
     * @see planet.commonapi.factory.RouteMessagePool#getBuiltRouteMessages()
     */
    public int getBuiltRouteMessages() {
        return createdMessages;
    }
    /**
     * Gets the total number of free RouteMessages.
     * @return The total number of free RouteMessages.
     * @see planet.commonapi.factory.RouteMessagePool#getFreeRouteMessages()
     */
    public int getFreeRouteMessages() {
        return freeMessages;
    }
    /**
     * Gets the total number of reused RouteMessages.
     * @return The total number of reused RouteMessages.
     * @see planet.commonapi.factory.RouteMessagePool#getReusedRouteMessages()
     */
    public int getReusedRouteMessages() {
        return reusedMessages;
    }
}
