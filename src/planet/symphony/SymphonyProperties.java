package planet.symphony;

import planet.commonapi.exception.InitializationException;
import planet.symphony.messages.NeighbourMessagePool;
import planet.util.OverlayProperties;
import planet.util.PropertiesWrapper;

/**
 * This class includes the initialization and the values for all
 * configuration properties of the Chord overlay.
 * @author <a href="mailto: jordi.pujol@estudiants.urv.es">Jordi Pujol</a>
 * @author <a href="mailto: marc.sanchez@estudiants.urv.es">Marc Sanchez</a>
 * Date: 05/07/2004
 */
public class SymphonyProperties implements OverlayProperties { 

	/* *********************** SYMPHONY PROPERTIES ***********************/
	/* Theese must to appear on the properties file */
	
	/**
	 * Symphony property: Default key for maximum number of long distance 
	 * connections.
	 */
	public static final String SYMPHONY_MAX_LONG_DISTANCE = "SYMPHONY_MAX_LONG_DISTANCE";
	/**
	 * Symphony property: Default key for maximum number of members
	 * in successor list.
	 */
	public static final String SYMPHONY_MAX_SUCCESSOR_LIST = "SYMPHONY_MAX_SUCCESSOR_LIST";
	/**
	 * Symphony property: Default key for maximum number of retries
	 * to obtain a connection to the same long distance node.
	 */
	public static final String SYMPHONY_MAX_RETRIES_NEW_LONG_DISTANCE = "SYMPHONY_MAX_RETRIES_NEW_LONG_DISTANCE";
	/**
	 * Symphony property: Default key for maximum number of retries
	 * to enter to the network by the same bootstrap.
	 */
	public static final String SYMPHONY_MAX_JOIN_RETRIES = "SYMPHONY_MAX_JOIN_RETRIES";
	/**
	 * Symphony property: Default key for number of stabilize steps.
	 */
	public static final String SYMPHONY_STABILIZATION_STEPS = "SYMPHONY_STABILIZATION_STEPS";
		
	/* *************** SYMPHONY ATTRIBUTES   ****************/
	/**
	 * Maximum number of long distance connections (K).
	 */
	public int maxLongDistance;
	/**
	 * Maximum number of members in successor list (F).
	 */
	public int maxSuccessorList;
	/**
	 * Maximum number of retries
	 * to obtain a connection to the same long distance node.
	 */
	public int maxRetriesNewLongDistance;
	/**
	 * Maximum number of retries
	 * to enter to the network by the same bootstrap.
	 */
	public int maxJoinRetries;
	/**
	 * Number of steps for statibilization.
	 */
	public int stabilizeSteps;


	/**
	 * Initialize all configuration properties of the Symphony overlay.
	 * @see planet.util.OverlayProperties#init(planet.util.PropertiesWrapper)
	 * @param properties Properties with all (key,value) pairs.
	 * @throws InitializationException
	 */
	public void init(PropertiesWrapper properties) throws InitializationException {

        maxLongDistance = properties.getPropertyAsInt(SYMPHONY_MAX_LONG_DISTANCE);
		if (maxLongDistance < 0) 
			throw new InitializationException("Property '"+SYMPHONY_MAX_LONG_DISTANCE +"' is not a valid number.");
		
		maxSuccessorList = properties.getPropertyAsInt(SYMPHONY_MAX_SUCCESSOR_LIST);
		/*
		 * IMPORTANT: DEFAULT_F>= 2
		 * To calculate correctly the number of members in the 
		 * network each node requires the predecessor and
		 * the predecessor of its predecessor. For this,
		 * the value of this property must be greater than 
		 * or equals to two (2).
		 */
		if (maxSuccessorList < 2) 
			throw new InitializationException("Property '"+SYMPHONY_MAX_SUCCESSOR_LIST +"' is not a valid number. Must be greater than or equals to 2.");

		//read the maxRetriesNewLongDistance
		maxRetriesNewLongDistance = properties.getPropertyAsInt(SYMPHONY_MAX_RETRIES_NEW_LONG_DISTANCE);
		if (maxRetriesNewLongDistance < 0) 
			throw new InitializationException("Property '"+SYMPHONY_MAX_RETRIES_NEW_LONG_DISTANCE +"' is not a valid number.");
		
		//read the maxJoinRetries
		maxJoinRetries = properties.getPropertyAsInt(SYMPHONY_MAX_JOIN_RETRIES);
		if (maxJoinRetries < 0) 
			throw new InitializationException("Property '"+SYMPHONY_MAX_JOIN_RETRIES +"' is not a valid number.");

		//read the defaultStabilizeSteps
		stabilizeSteps = properties.getPropertyAsInt(SYMPHONY_STABILIZATION_STEPS);
		if (stabilizeSteps < 0) 
			throw new InitializationException("Property '"+SYMPHONY_STABILIZATION_STEPS +"' is not a valid number of stabilize steps.");
        
        //initilizes the NeighbourMessagePool only under Symphony overlay simulations
        NeighbourMessagePool.init();
		
	}
	
    /**
     * Makes the postinitialization process. Does nothing.
     * @throws InitializationException if an error occurs during
     * the initialization of the different properties.
     * @param properties A Properties instance with all required configuration properties.
     * @see planet.util.PropertiesInitializer#postinit(planet.util.PropertiesWrapper)
     */
    public void postinit(PropertiesWrapper properties) throws InitializationException
    {
        //does nothing
    }
	
	/**
	 * Returns a String representation of the constant specific values
	 * of type the RouteMessage. Its use is only for human readable 
	 * logs. Based on SymphonyNode implementation.
	 * @param type Value to get its String representation.
	 * @return The String representation of the type.
	 */
	public String typeToString(int type) {
		return SymphonyNode.TYPES[type];
	}
	/**
	 * Returns a string representation of each of event mode and 
	 * RouteMessage mode.
	 * @param mode Mode of the RouteMessage to get its 
	 * String representation.
	 * @return String representation of the mode of RouteMessage.
	 */
	public String modeToString(int mode) {
		return SymphonyNode.MODES[mode];
	}
	
	/**
	 * Returns RouteMessage type for Application level.
	 * @return RouteMessage type for Application level.
	 */
	public int getTypeForApplicationMessage()
	{
	    return SymphonyNode.DATA;
	}
}
