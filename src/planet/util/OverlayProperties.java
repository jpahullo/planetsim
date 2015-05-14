package planet.util;

/**
 * This interface permits to implement as static form
 * This interficie allows to implement all the own properties of configuration
 * of the overlay network under a same instance.  It establishes a minimum 
 * contract, the method init(), so that their values are initialized. For this
 * reason, cast will have to be made in each place where it is necessary, 
 * to the concrete implementation.
 * @author <a href="mailto: jordi.pujol@estudiants.urv.es">Jordi Pujol</a>
 * Date: 05/07/2004
 */
public interface OverlayProperties extends PropertiesInitializer {
	
    /**
	 * Returns a String representation of the constant specific values
	 * of type the RouteMessage. Its use is only for human readable 
	 * logs.
	 * @param type Value to get its String representation.
	 * @return The String representation of the type.
	 */
	public String typeToString(int type);
	/**
	 * Returns a string representation of each of event mode and 
	 * RouteMessage mode.
	 * @param mode Mode of the RouteMessage to get its 
	 * String representation.
	 * @return String representation of the mode of RouteMessage.
	 */
	public String modeToString(int mode);
	/**
	 * Returns RouteMessage type for Application level.
	 * @return RouteMessage type for Application level.
	 */
	public int getTypeForApplicationMessage();
}
