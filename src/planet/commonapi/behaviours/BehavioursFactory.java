package planet.commonapi.behaviours;

import planet.commonapi.exception.InitializationException;

/**
 * This interface provides a method to get an instance of any component required 
 * in an overlay behaviours based implementation. When the current overlay
 * don't use behaviours, all these factory methods throws an
 * InitializationException.
 * <br><br>
 * Any future implementation must contain the no argument constructor.
 * <br><br>
 * @author <a href="mailto: jordi.pujol@estudiants.urv.es">Jordi Pujol</a>
 * @author <a href="mailto: marc.sanchez@urv.net">Marc Sanchez</a>
 * 07-jul-2005
 */
public interface BehavioursFactory extends java.io.Serializable {
	
    /**
     * Sets the initial values for this BehavioursFactory.
     * @param pool BehavioursPool class reference.
     * @param filter BehavioursFilter class reference.
     * @param invoker BehavioursInvoker class reference.
     * @param pattern BehavioursPattern class reference.
     * @param roleSelector BehavioursRoleSelector class reference.
     * @return The same instance once it has been updated.
     * @throws InitializationException if some error occurs during the
     * initialization process.
     */
    public BehavioursFactory setValues(Class pool, Class filter, Class invoker, 
            Class pattern, Class roleSelector) throws InitializationException;
    
    /**
     * Builds an instance of the current BehavioursFilter implementation.
     * @return An instance of the current BehavioursFilter implementation.
     * @throws InitializationException when an error occurs during the 
     * initialization or when this factory method is non applicable.
     */
    public BehavioursFilter buildBehavioursFilter() throws InitializationException;
    /**
     * Builds an instance of the current BehavioursInvoker implementation.
     * @return An instance of the current BehavioursInvoker implementation.
     * @throws InitializationException when an error occurs during the 
     * initialization or when this factory method is non applicable.
     */
    public BehavioursInvoker buildBehavioursInvoker() throws InitializationException;
    /**
     * Builds an instance of the current BehavioursPattern implementation.
     * @return An instance of the current BehavioursPattern implementation.
     * @throws InitializationException when an error occurs during the 
     * initialization or when this factory method is non applicable.
     */
    public BehavioursPattern buildBehavioursPattern() throws InitializationException;
    /**
     * Builds an instance of the current BehavioursPool implementation.
     * @return An instance of the current BehavioursPool implementation.
     * @throws InitializationException when an error occurs during the 
     * initialization or when this factory method is non applicable.
     */
    public BehavioursPool buildBehavioursPool() throws InitializationException;
    /**
     * Builds an instance of the current BehavioursRoleSelector implementation.
     * @return An instance of the current BehavioursRoleSelector implementation.
     * @throws InitializationException when an error occurs during the 
     * initialization or when this factory method is non applicable.
     */
    public BehavioursRoleSelector buildBehavioursRoleSelector() throws InitializationException;
}


