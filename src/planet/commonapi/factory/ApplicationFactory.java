package planet.commonapi.factory;

import planet.commonapi.Application;
import planet.commonapi.exception.InitializationException;

/**
 * This interface abstracts the task of generate new instances of Application,
 * using the factory method design pattern.
 * <br><br>
 * Any future implementation must contain the no argument constructor.
 * <br><br>
 * @see planet.commonapi.Application Application
 * @author <a href="mailto: jordi.pujol@estudiants.urv.es">Jordi Pujol</a>
 * 07-jul-2005
 */
public interface ApplicationFactory {
    
    /**
     * Initializes the ApplicationFactory with the specified values.
     * @param application Related Class instance for the current Application in use.
     * @return The same instance once it has been updated.
     * @throws InitializationException if any error occurs during the 
     * initialization process.
     */
    public ApplicationFactory setValues(Class application) throws InitializationException;
    
	/**
	 * Generates a new instance of the actual Application class. The name of
	 * Application is setting the default one by its implementation.
	 * @return A new instance of the Application
	 * @see planet.commonapi.Application
	 */
	public Application buildApplication() throws InitializationException;
	
	/**
	 * Generates a new instance of the Application class <b>app</b>. This value
	 * must be an existing Application implementation. The Application's name is
	 * setting by the implementation owner.
	 * @param app Class completly defined of the Application to generate only by this method call.
	 * @return A new instance of Application. Its class is <b>app</b>
	 * @see planet.commonapi.Application
	 */
	public Application buildApplication(String app) throws InitializationException;
	
	/**
	 * Generates a new instance of the actual Application class. The name of Application is
	 * overwriting by <b>name</b>.
	 * @return A new instance of the Application
	 * @see planet.commonapi.Application
	 * @param name Name for the application to generate.
	 */
	public Application buildApplicationWithName(String name) throws InitializationException;
	
	/**
	 * Generates a new instance of the Application class <b>app</b>. This value
	 * must be an existing Application implementation. The name of Application is
	 * overwriting by <b>name</b>.
	 * @param app Class completly defined of the Application to generate only by this method call.
	 * @return A new instance of Application. Its class is <b>app</b>
	 * @see planet.commonapi.Application
	 * @param name Name for the application to generate.
	 */
	public Application buildApplicationWithName(String app,String name) throws InitializationException;
}
