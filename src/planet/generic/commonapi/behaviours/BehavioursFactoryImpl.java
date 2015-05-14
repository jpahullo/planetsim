package planet.generic.commonapi.behaviours;

import planet.commonapi.behaviours.BehavioursFactory;
import planet.commonapi.behaviours.BehavioursFilter;
import planet.commonapi.behaviours.BehavioursInvoker;
import planet.commonapi.behaviours.BehavioursPattern;
import planet.commonapi.behaviours.BehavioursPool;
import planet.commonapi.behaviours.BehavioursRoleSelector;
import planet.commonapi.exception.InitializationException;
import planet.generic.commonapi.factory.GenericFactory;
import planet.util.Properties;

/**
 * This class provides an implementation of BehavioursFactory interface.
 * @author <a href="mailto: marc.sanchez@urv.net">Marc Sanchez</a>
 * @author <a href="mailto: jordi.pujol@estudiants.urv.es">Jordi Pujol</a>
 * 07-jul-2005
 */
public class BehavioursFactoryImpl implements BehavioursFactory {
    /**
     * The BehavioursPool class to use to build new instances.
     */
    private Class behavioursPool = null;
    /**
     * The BehavioursFilter class to use to build new instances.
     */
    private Class behavioursFilter = null;
    /**
     * The BehavioursInvoker class to use to build new instances.
     */
    private Class behavioursInvoker = null;
    /**
     * The BehavioursPattern class to use to build new instances.
     */
    private Class behavioursPattern = null;
    /**
     * The BehavioursRoleSelector class to use to build new instances.
     */
    private Class behavioursRoleSelector = null;
    
    /**
     * Empty constructor, it does nothing. It requires the
     * <b>setValues(...)</b> invokation.
     */
	public BehavioursFactoryImpl() {}
    
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
            Class pattern, Class roleSelector) throws InitializationException 
    {
        behavioursPool = pool;
        behavioursFilter = filter;
        behavioursInvoker = invoker;
        behavioursPattern = pattern;
        behavioursRoleSelector = roleSelector;
        return this; 
    }
    
	/**
     * Builds a new instance of the current implementation of BehavioursPool.
     * @return A new instance of BehavioursPool.
     * @throws InitializationException when an error occurs during the
     * initialization of the BehavioursPool or when this factory method is
     * nonapplicable, because the current overlay doesn't use behaviours.
	 */
	public BehavioursPool buildBehavioursPool() throws InitializationException {
        ensureBehavioursUse();
        return (BehavioursPool)GenericFactory.newInstance(Properties.behavioursPool);
	}
    
    /**
     * Builds a new instance of the current implementation of BehavioursFilter.
     * @return A new instance of BehavioursFilter.
     * @throws InitializationException when an error occurs during the
     * initialization of the BehavioursFilter or when this factory method is
     * nonapplicable, because the current overlay doesn't use behaviours.
     * @see planet.commonapi.behaviours.BehavioursFactory#buildBehavioursFilter()
     */
    public BehavioursFilter buildBehavioursFilter()
            throws InitializationException {
        ensureBehavioursUse();
        return (BehavioursFilter)GenericFactory.newInstance(Properties.behavioursFilter);
    }
    
    /**
     * Builds a new instance of the current implementation of BehavioursInvoker.
     * @return A new instance of BehavioursInvoker.
     * @throws InitializationException when an error occurs during the
     * initialization of the BehavioursInvoker or when this factory method is
     * nonapplicable, because the current overlay doesn't use behaviours.
     * @see planet.commonapi.behaviours.BehavioursFactory#buildBehavioursInvoker()
     */
    public BehavioursInvoker buildBehavioursInvoker()
            throws InitializationException {
        ensureBehavioursUse();
        return (BehavioursInvoker)GenericFactory.newInstance(Properties.behavioursInvoker);
    }
    /**
     * Builds a new instance of the current implementation of BehavioursPattern.
     * @return A new instance of BehavioursPattern.
     * @throws InitializationException when an error occurs during the
     * initialization of the BehavioursPattern or when this factory method is
     * nonapplicable, because the current overlay doesn't use behaviours.
     * @see planet.commonapi.behaviours.BehavioursFactory#buildBehavioursPattern()
     */
    public BehavioursPattern buildBehavioursPattern()
            throws InitializationException {
        ensureBehavioursUse();
        return (BehavioursPattern)GenericFactory.newInstance(Properties.behavioursPattern);
    }
    /**
     * Builds a new instance of the current implementation of BehavioursRoleSelector.
     * @return A new instance of BehavioursRoleSelector.
     * @throws InitializationException when an error occurs during the
     * initialization of the BehavioursRoleSelector or when this factory method is
     * nonapplicable, because the current overlay doesn't use behaviours.
     * @see planet.commonapi.behaviours.BehavioursFactory#buildBehavioursRoleSelector()
     */
    public BehavioursRoleSelector buildBehavioursRoleSelector()
            throws InitializationException {
        ensureBehavioursUse();
        return (BehavioursRoleSelector)GenericFactory.newInstance(Properties.behavioursRoleSelector);
    }
    
    /**
     * If the current overlay implementation don't use behaviours, throws
     * an InitializationException showing this situation.
     * @throws InitializationException when the current overlay
     * implementation don't use behaviours.
     */
    private void ensureBehavioursUse() throws InitializationException
    {
        if (!Properties.overlayWithBehaviours)
        {
            throw new InitializationException("The current overlay implementation doesn't use behaviours.");
        }
    }    
}
