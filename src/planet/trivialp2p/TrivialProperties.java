package planet.trivialp2p;

import planet.commonapi.exception.InitializationException;
import planet.util.OverlayProperties;
import planet.util.PropertiesWrapper;

/**
 * This class includes the initialization and the values for all
 * configuration properties of the Chord overlay.
 * @author <a href="mailto: jordi.pujol@estudiants.urv.es">Jordi Pujol</a>
 * @author <a href="mailto: marc.sanchez@estudiants.urv.es">Marc Sanchez</a>
 * Date: 05/07/2004
 */
public class TrivialProperties implements OverlayProperties { 

	/* *********************** TRIVIALP2P PROPERTIES ***********************/
	/* Theese must to appear on the properties file */
	
    /**
     * TrivialP2P property: Default key for 'debug' flag.
     */
    public static final String TRIVIAL_DEBUG = "TRIVIAL_DEBUG";
	/* *********************** TRIVIALP2P ATTRIBUTES   **********************/
    /**
     * When true, shows information for debug purposes.
     */
    public boolean debug;


	/**
	 * Initialize all configuration properties of the Symphony overlay.
	 * @see planet.util.OverlayProperties#init(planet.util.PropertiesWrapper)
	 * @param properties Properties with all (key,value) pairs.
	 * @throws InitializationException
	 */
	public void init(PropertiesWrapper properties) throws InitializationException {
		//Load properties
        debug = properties.getPropertyAsBoolean(TRIVIAL_DEBUG);
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
		return TrivialNode.TYPES[type];
	}
	/**
	 * Returns a string representation of each of event mode and 
	 * RouteMessage mode.
	 * @param mode Mode of the RouteMessage to get its 
	 * String representation.
	 * @return String representation of the mode of RouteMessage.
	 */
	public String modeToString(int mode) {
		return TrivialNode.MODES[mode];
	}
	
	/**
	 * Returns RouteMessage type for Application level.
	 * @return RouteMessage type for Application level.
	 */
	public int getTypeForApplicationMessage()
	{
	    return TrivialNode.DATA;
	}
}
