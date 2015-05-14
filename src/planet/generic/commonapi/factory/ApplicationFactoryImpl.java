package planet.generic.commonapi.factory;

import planet.commonapi.Application;
import planet.commonapi.exception.InitializationException;
import planet.commonapi.factory.ApplicationFactory;
import planet.util.Properties;

/**
 * This factory abstracts the functionality of building new instances of
 * Application. 
 * @author <a href="mailto: jordi.pujol@estudiants.urv.es">Jordi Pujol</a>
 * 07-jul-2005
 */
public class ApplicationFactoryImpl implements ApplicationFactory {
	/**
	 * Class for Applications to build.
	 */
	protected Class application = null;
	
	
	/**
     * Builds an uninitialized ApplicationFactoryImpl. Requires the
     * <b>setValues(...)</b> invokation.
	 */
	public ApplicationFactoryImpl(){}
    
    /**
     * Sets the initial values for this ApplicationFactory.
     * @param application Class reference of the current Application implementation.
     * @return The same instance once it has been updated.
     * @throws InitializationException if some error occurs during
     * initialization process.
     * @see planet.commonapi.factory.ApplicationFactory#setValues(java.lang.Class)
     */
    public ApplicationFactory setValues(Class application) throws InitializationException {
		this.application = application;
        return this;
	}
		
	/**
	 * Builds a new instance of the actual Application's class with the 
	 * default name.
	 * @see planet.commonapi.factory.ApplicationFactory#buildApplication()
	 * @return A new instance of the actual Application's class.
	 */
	public Application buildApplication() throws InitializationException {
        return (Application)GenericFactory.newInstance(application);
	}

	/**
	 * Builds a new instance of the specified Application's class <b>app</b> with the
	 * default name. 
	 * @see planet.commonapi.factory.ApplicationFactory#buildApplication(java.lang.String)
	 * @param app Application's class to be generated.
	 * @return A new instance of the specified Application's class.
	 */
	public Application buildApplication(String app) throws InitializationException {
		Class appClass;
		try {
			appClass = Class.forName(app);
            return (Application) appClass.newInstance();
		} catch (ClassNotFoundException e) {
			throw new InitializationException("The Application classname '"+app+"' not found.",e);
		} catch (SecurityException e) {
			throw new InitializationException("Cannot access to the default constructor '"+app+"()'",e);
		} catch (Exception e) {
		    throw new InitializationException("Cannot access to the class '"+app+"'",e);
        }
	}
	
	/**
	 * Generates a new instance of the actual class of Application.
	 * @see planet.commonapi.factory.ApplicationFactory#buildApplication()
	 * @return A new instance of the actually specified Application class.
	 * @see planet.commonapi.Application Application
	 * @throws InitializationException 
	 * @param name Name for the application to generate.
	 */
	public Application buildApplicationWithName(String name) throws InitializationException {
		return ((Application)GenericFactory.newInstance(Properties.factoriesApplication)).setValues(name);
	}
	
	/**
	 * Generates a new instance of the specified Application class <b>app</b>.
	 * @see planet.commonapi.factory.ApplicationFactory#buildApplication(java.lang.String)
	 * @param app Application class to generate this new instance.
	 * @return A new instance of the Application class <b>app</b>
	 * @param name Name for the application to generate.
	 */
	public Application buildApplicationWithName(String app,String name) throws InitializationException {
		
		try {
			Class appClass = Class.forName(app);
            return ((Application)appClass.newInstance()).setValues(name);
		} catch (ClassNotFoundException e) {
			throw new InitializationException("The Application classname '"+app+"' not found.",e);
		} catch (SecurityException e) {
			throw new InitializationException("Cannot access to the Constructor '"+app+"(Id)'",e);
		} catch (Exception e) {
			throw new InitializationException("Cannot build a new instance of '"+app+"'",e);
		}
	}
}
