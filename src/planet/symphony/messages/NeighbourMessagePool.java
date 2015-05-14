package planet.symphony.messages;

import java.util.Stack;

/**
 * The purpose of this pool is to save the memory used on the simulation, for 
 * the intensive communication with NeighbourMessages.
 * @author <a href="mailto: jordi.pujol@estudiants.urv.es">Jordi Pujol</a>
 * 21-jun-2005
 */
public class NeighbourMessagePool {
    
    /** The pool of NeighbourMessage is based on a stack. */
    private static Stack pool = null;
    /** A temporal NeighbourMessage for internal uses. */
    private static NeighbourMessage temp = null;
    /** Number of created NeighbourMessages. */
    public static int createdMessages;
    /** Number of reused NeighbourMessages. */
    public static int reusedMessages;
    /** Number of free NeighbourMessages. */
    public static int freeMessages;
    
    /**
     * Initializes the pool of NeighbourMessages.
     */
    public static void init()
    {
        pool = new Stack();
        createdMessages = 0;
        reusedMessages = 0;
        freeMessages = 0;
    }
    
    /**
     * Returns an existing NeighbourMessage if exists or a new NeighbourMessage.
     * @return An existing or a new NeighbourMessage.
     */
    public static NeighbourMessage getMessage()
    {
        if (pool.size()>0)
        {
            reusedMessages++;
            return (NeighbourMessage)pool.pop();
        }
        else
        {
            createdMessages++;
            return new NeighbourMessage();
        }
    }
    
    /**
     * Returns an existing or a new NeighbourMessage with the specified internal value.
     * @param value The internal value for the NeighbourMessage.
     * @return An existing or a new NeighbourMessage with the specified internal value.
     */
    public static NeighbourMessage getMessage(java.util.Collection value)
    {
        temp = getMessage();
        temp.neighbourhoodSet = value;
        return temp;
    }
    
    /**
     * Free the unused NeighbourMessage <b>msg</b>
     * @param msg An unused NeighbourMessage.
     */
    public static void freeMessage(NeighbourMessage msg)
    {
        freeMessages++;
        msg.neighbourhoodSet = null;
        pool.push(msg);
    }

}
