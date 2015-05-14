package planet.generic.commonapi.behaviours;

import java.util.Enumeration;
import java.util.HashSet;
import java.util.StringTokenizer;
import java.util.Vector;

import planet.commonapi.exception.InitializationException;
import planet.generic.commonapi.factory.GenericFactory;
import planet.util.Interfaces;
import planet.util.PropertiesInitializer;
import planet.util.PropertiesWrapper;

/**
 * This class allows to add new behaviours to bad nodes on the overlay.
 * Each behaviour is binded to the type of message exchanged between nodes.
 * The message are defined by a type and mode field. It also allow the use
 * of the wildcard '*', that means, whatever type if is specified in type's
 * field or either whatever mode if is specified in mode's field. Through 
 * the method init(), so that their values are initialized. 
 * @author <a href="mailto: marc.sanchez@estudiants.urv.es">Marc Sanchez</a>
 * Date: 10/10/2004
 */
public class BehavioursPropertiesImpl implements PropertiesInitializer {

    /* *************************** SPECIFIC BEHAVIOURS PROPERTIES ************/
    /**
     * Behaviours property: Default key specified in the properties file that
     * identifies percentage of faulty nodes.
     */
    public static final String BEHAVIOURS_PROPERTIES_FAULTY_NODES               = "BEHAVIOURS_PROPERTIES_FAULTY_NODES";
    /**
     * Behaviours property: Default key specified in the properties file that
     * identifies distribution of malicious node.
     */
    public static final String BEHAVIOURS_PROPERTIES_MALICIOUS_DISTRIBUTION     = "BEHAVIOURS_PROPERTIES_MALICIOUS_DISTRIBUTION";
    /**
     * Behaviours property: Default key specified in the properties file that
     * identifies when to show specific debug info for behaviours applying.
     */
    public static final String BEHAVIOURS_PROPERTIES_DEBUG                      = "BEHAVIOURS_PROPERTIES_DEBUG";
    /**
     * Behaviours property: Default starting string for the keys that 
     * identifies concrete instances for the required behaviours. Each
     * key must be ended with a incremental integer number to make them
     * different.
     */
    public static final String BEHAVIOURS_PROPERTIES_INSTANCE                   = "BEHAVIOURS_PROPERTIES_INSTANCE";

    
    /* *********************** SPECIFIC BEHAVIOURS ATTRIBUTES *****************/
    /**
     * Behaviours property: Identifies percentage [0..100]% of faulty nodes.
     */
    public int faultyNodes                                                      = 0;
    /**
     * Behaviours property: Identifies distribution of malicious node.
     */
    public String maliciousDistribution                                         = null;
    /**
     * Behaviours property: Identifies distribution of malicious node.
     */
    public int maliciousDistributionAsInt                                       = 0;
    /**
     * Behaviours property: Specifies an uniform malicious distribution into
     * the current overlay.
     */
    public static final String BEHAVIOUR_UNIFORM_MALICIOUS_DISTRIBUTION         = "uniform";
    /**
     * Behaviours property: Specifies a chain malicious distribution into the 
     * current overlay.
     */
    public static final String BEHAVIOUR_CHAIN_MALICIOUS_DISTRIBUTION           = "chain";
    /**
     * Behaviours property: Specifies an uniform malicious distribution into
     * the current overlay.
     */
    public static final int BEHAVIOUR_UNIFORM_MALICIOUS_DISTRIBUTION_MASK       = 0x1;
    /**
     * Behaviours property: Specifies a chain malicious distribution into the 
     * current overlay.
     */
    public static final int BEHAVIOUR_CHAIN_MALICIOUS_DISTRIBUTION_MASK         = 0x2;
    /**
     * Behaviours property: Identifies when to show specific debug information 
     * for behaviours applying.
     */
    public boolean debug                                                        = false;
    /**
     * Internal behaviours property: once this instance has been initialized,
     * will contain all specified patterns into the configuration file.
     */
    public Vector patterns                                                      = null;
    
    /**
     * 
     * @param properties
     * @throws InitializationException
     * @see planet.util.PropertiesInitializer#init(planet.util.PropertiesWrapper)
     */
    public void init(PropertiesWrapper properties) throws InitializationException {
        faultyNodes = properties.getPropertyAsInt(BEHAVIOURS_PROPERTIES_FAULTY_NODES);
        if (faultyNodes < 0 ||
            faultyNodes > 100)
            throw new InitializationException("The percentage of faulty nodes is not in [0..100] range. Its actual value is '"+faultyNodes+"'");

        maliciousDistribution = properties.getProperty(BEHAVIOURS_PROPERTIES_MALICIOUS_DISTRIBUTION);
        if (!isValidMaliciousDistribution(maliciousDistribution))
            throw new InitializationException("A non valid value is specified into the property '"+
                    BEHAVIOURS_PROPERTIES_MALICIOUS_DISTRIBUTION+"'. Requires one of the following values:"+
                    BEHAVIOUR_UNIFORM_MALICIOUS_DISTRIBUTION+", "+BEHAVIOUR_CHAIN_MALICIOUS_DISTRIBUTION);
        if (maliciousDistribution.equalsIgnoreCase(BEHAVIOUR_UNIFORM_MALICIOUS_DISTRIBUTION))
            maliciousDistributionAsInt = BEHAVIOUR_UNIFORM_MALICIOUS_DISTRIBUTION_MASK;
        if (maliciousDistribution.equalsIgnoreCase(BEHAVIOUR_CHAIN_MALICIOUS_DISTRIBUTION))
            maliciousDistributionAsInt = BEHAVIOUR_CHAIN_MALICIOUS_DISTRIBUTION_MASK;
        
            
        debug = properties.getPropertyAsBoolean(BEHAVIOURS_PROPERTIES_DEBUG);
    }
    
    /**
     * Load all patterns once the simulator context has been initialized.
     * @throws InitializationException if an error occurs during
     * the initialization of the different properties.
     * @param properties A Properties instance with all required configuration properties.
     * @see planet.util.PropertiesInitializer#postinit(planet.util.PropertiesWrapper)
     */
    public void postinit(PropertiesWrapper properties) throws InitializationException
    {
        loadPatterns(properties);
    }
    
    /**
     * Test if the <b>dist</b> is a valid name of malicious distribution.
     * @param dist Loaded name of malicious distribution.
     * @return true when the name is correct. false in other case.
     */
    private static boolean isValidMaliciousDistribution(String dist)
    {
        return dist!=null &&
                ( dist.equalsIgnoreCase(BEHAVIOUR_UNIFORM_MALICIOUS_DISTRIBUTION) ||
                  dist.equalsIgnoreCase(BEHAVIOUR_CHAIN_MALICIOUS_DISTRIBUTION));
    }
    
    /**
     * Loads the specified patterns in the configuration properties. They starts
     * by BEHAVIOURS_PROPERTIES_INSTACE String.
     * @param properties Properties with all configuration properties.
     * @throws InitializationException if any error occurs during the 
     * initialization process.
     */
    private void loadPatterns(PropertiesWrapper properties) throws InitializationException
    {
        patterns = new Vector();
        //Behaviour's List
        String key = null;
        StringTokenizer parser = null;
        Class behaviourClass = null;
        String typeOf = null;
        String modeOf = null;
        String whenTo = null;
        String roleOf = null;
        double pdf;
        boolean validKey = false;
        
        //initialization process
        HashSet when = new HashSet();
        HashSet role = new HashSet();
        
        when.add(BehavioursPatternImpl.RUN_LOCAL);
        when.add(BehavioursPatternImpl.RUN_REMOTE);
        when.add(BehavioursPatternImpl.RUN_ALWAYS);
        
        role.add(BehavioursPatternImpl.ROLE_GOOD);
        role.add(BehavioursPatternImpl.ROLE_BAD);
        role.add(BehavioursPatternImpl.ROLE_NEUTRAL);

        Enumeration props = properties.propertyNames();

        //loading process
        while(props.hasMoreElements()) {
            //loading the next key
            key = (String)props.nextElement();
            //testing for behaviours instances property names
            while (!(validKey = key.startsWith(BEHAVIOURS_PROPERTIES_INSTANCE)) && props.hasMoreElements())
            {
                key = (String)props.nextElement();
            }
            //if not is a behaviour instance key ==> exits 
            if (!validKey) return;
            
            //loads this key 
            parser = new StringTokenizer(properties.getProperty(key),",");
            
            //testing the number of tokens
            if (parser.countTokens() != 6)
                throw new InitializationException("There are not six items in the property '"+key+"'. The format is the following: "+
                        BEHAVIOURS_PROPERTIES_INSTANCE+"_X = {1. class reference}, {2. type}, {3. mode}, {4. probability}, {5. locality}, {6. role}");
            
            //the first item is a class name
            behaviourClass = PropertiesWrapper.getValueAsClass(parser.nextToken().trim(),key);
            Interfaces.ensureImplementedInterface(behaviourClass,Interfaces.BEHAVIOURS_BEHAVIOUR,key);
            
            //the second item is type of the RouteMessage
            typeOf = parser.nextToken().trim();
 
            //the third item is the mode of the RouteMessage
            modeOf = parser.nextToken().trim();

            //the fourth item is the probability of Behaviour execution
            pdf = PropertiesWrapper.getValueAsDouble(parser.nextToken().trim(),key);
            if (!isNormalized(pdf))
                throw new InitializationException("The probability that appears at property '"+key+"' must to be in [0.0 .. 1.0] range.");
            
            //the fifth item is the locality behaviour attribute
            whenTo = parser.nextToken().trim();
            if (!when.contains(whenTo))
                throw new InitializationException("The locality attribute at property '"+key+"' is unknown. Possible values: "+
                      BehavioursPatternImpl.RUN_LOCAL+", "+BehavioursPatternImpl.RUN_REMOTE+", "+BehavioursPatternImpl.RUN_ALWAYS+".");
            
            //the sixth item is the role behaviour attribute
            roleOf = parser.nextToken().trim();
            if (!role.contains(roleOf))
                throw new InitializationException("The role attribute at property '"+key+"' is unknown. Possible values: "+
                      BehavioursPatternImpl.ROLE_GOOD+", "+BehavioursPatternImpl.ROLE_BAD+", "+BehavioursPatternImpl.ROLE_NEUTRAL+".");
            
            //building the pattern
            addPattern(behaviourClass,typeOf,modeOf,pdf,whenTo,roleOf);
        }
    }
    
    /**
     * This method checks wether a double value lies between (0.0, 1.0] range.
     * @param d double value to check
     * @return true if d lies between (0.0, 1.0].
     */
    private static boolean isNormalized(double d) {
        return d > 0.0 && d <= 1.0;
    }
    
    /**
     * This method add a new pattern to the vector sorted from more-to-less specific.
     * Specific criteria:
     * <pre>
     *         Type  Mode   
     *      1) Tag,  Tag
     *      2) Tag,   ?
     *      3) Tag,   *
     *      4)  ? ,  Tag
     *      5)  * ,  Tag
     *      6)  ? ,   *
     *      7)  * ,   ?
     *      8)  * ,   *
     * </pre>
     * 
     * @param behaviourClass Behaviour class for the current pattern.
     * @param type RouteMessage type for applying the current behaviour.
     * @param mode RouteMessage mode for applying the current behaviour.
     * @param pdf Probability of execution for the current behaviour.
     * @param when Locality specification for the current behaviour.
     * @param role Role specification for the current behaviour.
     * @throws InitializationException if some error occurs during the addition.
     */
    private void addPattern(Class behaviourClass, String type, String mode, double pdf, String when, String role) throws InitializationException
    {
        BehavioursPatternImpl pattern = (BehavioursPatternImpl)((BehavioursPatternImpl)GenericFactory.buildBehavioursPattern()).setValues(behaviourClass,type,mode,pdf,when,role);
        int at; 
        for (at = 0; at < patterns.size(); at++) {
            BehavioursPatternImpl p = (BehavioursPatternImpl) patterns.get(at);
            if (p.compareTo(pattern) < 0)  break;
        }
        patterns.add(at, pattern);
    }
}
