package planet.util;

import planet.commonapi.exception.InitializationException;

/**
 * @author <a href="mailto: jordi.pujol@estudiants.urv.es">Jordi Pujol</a>
 * 06-jul-2005
 */
public class Interfaces {
    
    /* ************************** FACTORY CLASS NAMES ******************/
    
    /* REQUIRED ATTRIBUTES */
    /* Factories: */
    /**
     * Factories property: The fully qualified name of the interface to be
     * implemented by any NetworkFactory implementation.
     */
    public static final String FACTORIES_NETWORKFACTORY                         = "planet.commonapi.factory.NetworkFactory";
    /**
     * Factories property: The fully qualified name of the interface to be
     * implemented by any IdFactory implementation.
     */
    public static final String FACTORIES_IDFACTORY                              = "planet.commonapi.factory.IdFactory";
    /**
     * Factories property: The fully qualified name of the interface to be
     * implemented by any NodeHandleFactory implementation.
     */
    public static final String FACTORIES_NODEHANDLEFACTORY                      = "planet.commonapi.factory.NodeHandleFactory";
    /**
     * Factories property: The fully qualified name of the interface to be
     * implemented by any NodeFactory implementation.
     */
    public static final String FACTORIES_NODEFACTORY                            = "planet.commonapi.factory.NodeFactory";
    /**
     * Factories property: The fully qualified name of the interface to be
     * implemented by any RouteMessagePool implementation.
     */
    public static final String FACTORIES_ROUTEMESSAGEPOOL                       = "planet.commonapi.factory.RouteMessagePool";

    /* Specific classes: */
    /**
     * Factories property: The fully qualified name of the interface to be
     * implemented by any Network implementation.
     */
    public static final String FACTORIES_NETWORK                                = "planet.commonapi.Network";
    /**
     * Factories property: The fully qualified name of the interface to be
     * implemented by any NodeHandle implementation.
     */
    public static final String FACTORIES_NODEHANDLE                             = "planet.commonapi.NodeHandle";
    /**
     * Factories property: The fully qualified name of the interface to be
     * implemented by any RouteMessage implementation.
     */
    public static final String FACTORIES_ROUTEMESSAGE                           = "planet.commonapi.RouteMessage";

    /* OPTIONAL ATTRIBUTES */
    /* Factories: */
    /**
     * Factories property: The fully qualified name of the interface to be
     * implemented by any ApplicationFactory implementation.
     */
    public static final String FACTORIES_APPLICATIONFACTORY                     = "planet.commonapi.factory.ApplicationFactory";
    /**
     * Factories property: The fully qualified name of the interface to be
     * implemented by any EndPointFactory implementation.
     */
    public static final String FACTORIES_ENDPOINTFACTORY                        = "planet.commonapi.factory.EndPointFactory";

    /* Specific classes: */
    /**
     * Factories property: The fully qualified name of the interface to be
     * implemented by any Application implementation.
     */
    public static final String FACTORIES_APPLICATION                            = "planet.commonapi.Application";
    /**
     * Factories property: The fully qualified name of the interface to be
     * implemented by any EndPoint implementation.
     */
    public static final String FACTORIES_ENDPOINT                               = "planet.commonapi.EndPoint";

    /* *********************** BEHAVIOURS PROPERTIES NAMES ***********************/
    /* Overlay dependant: All these attributes are optionals */
    
    /**
     * Behaviours property: The fully qualified name of the interface to be
     * implemented by any BehavioursFactory implementation.
     */
    public static final String BEHAVIOURS_FACTORY                               = "planet.commonapi.behaviours.BehavioursFactory";
    /**
     * Behaviours property: The fully qualified name of the interface to be
     * implemented by any BehavioursPool implementation.
     */
    public static final String BEHAVIOURS_POOL                                  = "planet.commonapi.behaviours.BehavioursPool";
    /**
     * Behaviours property: The fully qualified name of the interface to be
     * implemented by any BehavioursRoleSelector implementation.
     */
    public static final String BEHAVIOURS_ROLESELECTOR                          = "planet.commonapi.behaviours.BehavioursRoleSelector";
    /**
     * Behaviours property: The fully qualified name of the interface to be
     * implemented by any BehavioursInvoker implementation.
     */
    public static final String BEHAVIOURS_INVOKER                               = "planet.commonapi.behaviours.BehavioursInvoker";
    /**
     * Behaviours property: The fully qualified name of the interface to be
     * implemented by any BehavioursFilter implementation.
     */
    public static final String BEHAVIOURS_FILTER                                = "planet.commonapi.behaviours.BehavioursFilter";
    /**
     * Behaviours property: The fully qualified name of the interface to be
     * implemented by any BehavioursPattern implementation.
     */
    public static final String BEHAVIOURS_PATTERN                               = "planet.commonapi.behaviours.BehavioursPattern";
    /**
     * Behaviours property: The fully qualified name of the interface to be
     * implemented by any PropertiesInitializer implementation.
     */
    public static final String BEHAVIOURS_PROPERTIES                            = "planet.util.PropertiesInitializer";
    /**
     * Behaviours property: The fully qualified name of the interface to be
     * implemented by any Behaviour implementation.
     */
    public static final String BEHAVIOURS_BEHAVIOUR                             = "planet.commonapi.behaviours.Behaviour";
    

    /* *********************** OVERLAY PROPERTIES NAMES ***********************/
    /* Required: */
    
    /**
     * Overlay property: The fully qualified name of the interface to be
     * implemented by any Id implementation.
     */
    public static final String OVERLAY_ID                                       = "planet.commonapi.Id";
    /**
     * Overlay property: The fully qualified name of the interface to be
     * implemented by any Node implementation.
     */
    public static final String OVERLAY_NODE                                     = "planet.commonapi.Node";
    /**
     * Overlay property: The fully qualified name of the interface to be
     * implemented by any PropertiesInitializer implementation.
     */
    public static final String OVERLAY_PROPERTIES                               = "planet.util.OverlayProperties";
    

    /* *********************** RESULTS PROPERTIES NAMES ***********************/
    /* Test dependant: All these attributes are optionals */
    
    /**
     * Results property: The fully qualified name of the interface to be
     * implemented by any results Factory implementation.
     */
    public static final String RESULTS_FACTORY                                  ="planet.commonapi.results.ResultsFactory";
    /**
     * Results property: The fully qualified name of the interface to be
     * implemented by any results Edge implementation.
     */
    public static final String RESULTS_EDGE                                     ="planet.commonapi.results.ResultsEdge";
    /**
     * Results property: The fully qualified name of the interface to be
     * implemented by any results Constraint implementation.
     */
    public static final String RESULTS_CONSTRAINT                               ="planet.commonapi.results.ResultsConstraint";
    /**
     * Results property: The fully qualified name of the interface to be
     * implemented by any results Generator implementation.
     */
    public static final String RESULTS_GENERATOR                                ="planet.commonapi.results.ResultsGenerator";
    /**
     * Results property: The fully qualified name of the interface to be
     * implemented by any results PropertiesInitializer implementation.
     */
    public static final String RESULTS_PROPERTIES                               ="planet.util.PropertiesInitializer";

    
    
    /**
     * Returns true when the <b>classReference</B> or any of its superclasses
     * implements the <b>fullyQualifiedInterface</b> interface.
     * @param classReference Class reference to be tested.
     * @param fullyQualifiedInterface Fully qualified name of an interface.
     * @return true when the <b>classReference</b> or any of its superclasses 
     * implements the specified interface. false in other case.
     */
    public static final boolean implementedInterface(Class classReference, String fullyQualifiedInterface)
    {
        if (classReference == null) return false;
        
        Class aux = classReference;
        Class[] implementedInterfaceTemp = null;
        while (!aux.getName().equalsIgnoreCase("java.lang.Object"))
        {    
            implementedInterfaceTemp = aux.getInterfaces();
            for (int it = 0; it < implementedInterfaceTemp.length; it++)
                if (implementedInterfaceTemp[it].getName().equals(fullyQualifiedInterface)) return true;
            aux = aux.getSuperclass();
        }
        return false;
    }
    
    /**
     * Returns true when the <b>classReference</B> or any of its superclasses
     * extends the <b>fullyQualifiedClass</b> class.
     * @param classReference Class reference to be tested.
     * @param fullyQualifiedClass Fully qualified name of a class.
     * @return true when the <b>classReference</b> or any of its superclasses 
     * the the specified class. false in other case.
     */
    public static final boolean extendedClass(Class classReference, String fullyQualifiedClass)
    {
        if (classReference == null) return false;
        
        Class superClass = classReference.getSuperclass();
        while ( superClass != null && 
                !superClass.getName().equalsIgnoreCase("java.lang.Object") )
        {    
            if (superClass.getName().equalsIgnoreCase(fullyQualifiedClass)) return true;
            superClass = superClass.getSuperclass();
        }
        return false;
    }
    
    /**
     * Test if the <b>classReference</B> or any of its superclasses
     * implements the <b>fullyQualifiedInterface</b> interface.
     * @param classReference Class reference to be tested.
     * @param fullyQualifiedInterface Fully qualified name of an interface.
     * @param propertyName The property name where appears the this <b>classReference</b>
     * (only for logging purposes).
     * @throws InitializationException if the <b>classReference</b> does not
     * implement the <b>fullyQualifiedInterface</b>.
     */
    public static final void ensureImplementedInterface(Class classReference, String fullyQualifiedInterface, String propertyName)
        throws InitializationException
    {
        if (!Interfaces.implementedInterface(classReference,fullyQualifiedInterface))
            throw new InitializationException("The class '"+classReference+
                    "' specified in the property '"+propertyName+
                    "' does not implement the interface '"+
                    fullyQualifiedInterface+"'");
    }
    
    /**
     * Test if the <b>classReference</B> or any of its superclasses
     * extends the <b>fullyQualifiedClass</b> class (or abstract class).
     * @param classReference Class reference to be tested.
     * @param fullyQualifiedClass Fully qualified name of a class.
     * @param propertyName The property name where appears the this <b>classReference</b>
     * (only for logging purposes).
     * @throws InitializationException if the <b>classReference</b> does not
     * extend the <b>fullyQualifiedClass</b>.
     */
    public static final void ensureExtendedClass(Class classReference, String fullyQualifiedClass, String propertyName)
        throws InitializationException
    {
        if (!Interfaces.extendedClass(classReference,fullyQualifiedClass))
            throw new InitializationException("The class '"+classReference+
                    "' specified in the property '"+propertyName+
                    "' does not extend the class '"+
                    fullyQualifiedClass+"'");
    }
    
    /**
     * Test if the <b>classReference</B> or any of its superclasses
     * extends the <b>fullyQualifiedClass</b> class (or abstract class).
     * @param classReference Class reference to be tested.
     * @param fullyQualifiedClass Fully qualified name of a class.
     * @param propertyName The property name where appears the this <b>classReference</b>
     * (only for logging purposes).
     * @throws InitializationException if the <b>classReference</b> does not
     * extend the <b>fullyQualifiedClass</b>.
     */
    public static final void ensureImplementedInterfaceOrClass(Class classReference, String fullyQualifiedClass, String propertyName)
        throws InitializationException
    {
        if (! Interfaces.implementedInterface(classReference,fullyQualifiedClass) &&
            ! Interfaces.extendedClass(classReference,fullyQualifiedClass))
            throw new InitializationException("The class '"+classReference+
                    "' specified in the property '"+propertyName+
                    "' does not implement the interface or class '"+
                    fullyQualifiedClass+"'");
    }
}
