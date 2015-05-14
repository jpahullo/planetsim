package planet.chord;

import planet.commonapi.exception.InitializationException;
import planet.util.OverlayProperties;
import planet.util.PropertiesWrapper;

/**
 * This class includes the initialization and the values for all
 * configuration properties of the Chord overlay.
 * @author <a href="mailto: jordi.pujol@estudiants.urv.es">Jordi Pujol</a>
 * Date: 05/07/2004
 */
public class ChordProperties implements OverlayProperties {

	/* *********************** CHORD PROPERTIES ***********************/
	/* Theese must to appear on the properties file */
	/**
	 * Chord property: Default key for number of stabilize steps.
	 */
	public static final String CHORD_STABILIZATION_STEPS                        = "CHORD_STABILIZATION_STEPS";
	/**
	 * Chord property: Default key for number of steps to fix finger tables.
	 */
	public static final String CHORD_FIX_FINGER_STEPS                           = "CHORD_FIX_FINGER_STEPS";
	/**
	 * Chord property: Default key for size of successor list.
	 */
	public static final String CHORD_SUCCESSOR_LIST_SIZE                        = "CHORD_SUCCESSOR_LIST_SIZE";
    /**
     * Chord property: Default key for number of bits for ChordIds.
     */
    public static final String CHORD_BITS_PER_KEY                               = "CHORD_BITS_PER_KEY";
    

	
	/* *************** ATTRIBUTES FOR CHORD  ****************/
	/**
	 * Number of steps for statibilization.
	 */
	public int stabilizeSteps;
	/**
	 * Number of steps to fix fingers.
	 */
	public int fixFingerSteps;
	/**
	 * Max size for successor list.
	 */
	public int succListMax;
    /**
     * Number of bits per key.
     */
    public int bitsPerKey;
	
	/**
	 * Initialize all configuration properties of the Chord overlay.
	 * @see planet.util.OverlayProperties#init(planet.util.PropertiesWrapper)
	 * @param properties Properties with all (key,value) pairs.
	 * @throws InitializationException
	 */
	public void init(PropertiesWrapper properties) throws InitializationException {
		stabilizeSteps = properties.getPropertyAsInt(CHORD_STABILIZATION_STEPS);
		if (stabilizeSteps < 0) 
			throw new InitializationException("Property '"+CHORD_STABILIZATION_STEPS +"' is not a valid number of stabilize steps (must be positive).");
		
		fixFingerSteps = properties.getPropertyAsInt(CHORD_FIX_FINGER_STEPS);
		if (fixFingerSteps < 0) 
			throw new InitializationException("Property '"+CHORD_FIX_FINGER_STEPS +"' is not a valid number of steps to fix finger tables (must be positive).");

		succListMax = properties.getPropertyAsInt(CHORD_SUCCESSOR_LIST_SIZE);
		if (succListMax < 0) 
			throw new InitializationException("Property '"+CHORD_SUCCESSOR_LIST_SIZE +"' is not a valid size of successor list (must be positive).");
        
        bitsPerKey = properties.getPropertyAsInt(CHORD_BITS_PER_KEY);
        if (!isValidValue(bitsPerKey))
            throw new InitializationException("Property '"+CHORD_BITS_PER_KEY+"' is not a valid value. Must be multiple of 32 in range of [32..160].");
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
     * Test if the <b>bitsPerKey</b> is multiple of 32 within the range 
     * [32..192].
     * @param bitsPerKey Number of bits per key to be tested.
     * @return true if the preconditions are accomplished or false in other case.
     */
    public static boolean isValidValue(int bitsPerKey)
    {
        return bitsPerKey > 0 && bitsPerKey <= 160 && bitsPerKey%32==0;
    }
	
	
	/**
	 * Returns a String representation of the constant specific values
	 * of type and mode of the RouteMessage. Its use is only for
	 * human readable logs. Based on ChordNode implementation.
	 * @param type Value to get its String representation.
	 * @return The String representation of the type.
	 */
	public String typeToString(int type) {
		return ChordNode.TYPES[type];
	}
	
	/**
	 * Returns a string representation of each of event mode and 
	 * RouteMessage mode.
	 * @param mode Mode of the RouteMessage to get its 
	 * String representation.
	 * @return String representation of the mode of RouteMessage.
	 */
	public String modeToString(int mode) {
		return ChordNode.MODES[mode];
	}
	/**
	 * Returns RouteMessage type for Application level.
	 * @return RouteMessage type for Application level.
	 */
	public int getTypeForApplicationMessage()
	{
	    return ChordNode.DATA;
	}

}
