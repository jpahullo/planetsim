package planet.util;

import java.io.Serializable;

import planet.commonapi.exception.InitializationException;

/**
 * Offers the minimal functionallity for a properties initializer of any type.
 * @author <a href="mailto: jordi.pujol@estudiants.urv.es">Jordi Pujol</a>
 * 22/02/2005
 */
public interface PropertiesInitializer extends Serializable {
    
    /**
	 * Initialize correctly the configuration properties.
	 * @throws InitializationException if an error occurs during
	 * the initialization of the different properties.
	 * @param properties A Properties instance with all required configuration properties. 
	 */
	public void init(PropertiesWrapper properties) throws InitializationException;
    
    /**
     * Makes the postinitialization process. Sometimes one require make some
     * initialization tasks after whole simulator context has been initialized.
     * @throws InitializationException if an error occurs during
     * the initialization of the different properties.
     * @param properties A Properties instance with all required configuration properties.
     */
    public void postinit(PropertiesWrapper properties) throws InitializationException;

}
