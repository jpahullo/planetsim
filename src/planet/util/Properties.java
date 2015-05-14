package planet.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.Vector;

import planet.commonapi.exception.InitializationException;

/**
 * This class loads all mandatory attributes of the specified properties file.
 * The loading process follows these steps:
 * <ol type="1">
 * <li><b>Open the specified master properties file.</b> This file contains the
 * filename with the required attributes for the current simulation, under 
 * certain <b>master key</B>.</li>
 * <li><b>Open the current properties file.</b> Opens the properties file
 * in a normal serialization (no XML) and loads all mandatory attributes and
 * these followings optional parts: 
 * <ul>
 * <li><b>Behaviours</b> properties when the OVERLAY_WITH_BEHAVIOURS attribute 
 * is true.</li>
 * <li><b>Serialization</b> properties when the FACTORIES_NETWORKTOPOLOGY
 * attribute has the <b>serialized</b> value.</li>
 * </ul>
 * </li>
 * </ol>
 * Any other part must be explicitly activated with the related methods
 * wich starts their names with <b>activate...()</b>.
 * @author <a href="jordi.pujol@estudiants.urv.es">Jordi Pujol</a>
 * @author <a href="marc.sanchez@urv.net">Marc Sanchez</a> 
 * 30-jun-2005
 */
public class Properties {
	/* ************************** FACTORIES PROPERTIES NAMES ******************/
    
    /* REQUIRED ATTRIBUTES */
    /* Factories: */
    /**
     * Factories property: The default NetworkFactory key specified in the properties file.
     */
    public static final String FACTORIES_NETWORKFACTORY                         = "FACTORIES_NETWORKFACTORY";
    /**
     * Factories property: The default IdFactory key specified in the properties file.
     */
    public static final String FACTORIES_IDFACTORY                              = "FACTORIES_IDFACTORY";
    /**
     * Factories property: The default NodeHandleFactory key specified in the properties file.
     */
    public static final String FACTORIES_NODEHANDLEFACTORY                      = "FACTORIES_NODEHANDLEFACTORY";
    /**
     * Factories property: The default NodeFactory key specified in the properties file.
     */
    public static final String FACTORIES_NODEFACTORY                            = "FACTORIES_NODEFACTORY";
    /**
     * Factories property: The default RouteMessagePool key specified in the properties file.
     */
    public static final String FACTORIES_ROUTEMESSAGEPOOL                       = "FACTORIES_ROUTEMESSAGEPOOL";

    /* Specific classes: */
    /**
     * Factories property: Default key specified in the properties file that
     * identifies implementation class for Network interface. 
     */
    public static final String FACTORIES_NETWORK                                = "FACTORIES_NETWORK";
    /**
     * Factories property: Default key specified in the properties file that
     * identifies implementation class for NodeHandle interface. 
     */
    public static final String FACTORIES_NODEHANDLE                             = "FACTORIES_NODEHANDLE";
    /**
     * Factories property: Default key specified in the properties file that
     * identifies implementation class for RouteMessage interface. 
     */
    public static final String FACTORIES_ROUTEMESSAGE                           = "FACTORIES_ROUTEMESSAGE";
    /**
     * Factories property: Default key specified in the properties file that
     * identifies the network topology.
     */
    public static final String FACTORIES_NETWORKTOPOLOGY                        = "FACTORIES_NETWORKTOPOLOGY";
    /**
     * Factories property: Default key specified in the properties file that
     * identifies the network size.
     */
    public static final String FACTORIES_NETWORKSIZE                            = "FACTORIES_NETWORKSIZE";

    /* OPTIONAL ATTRIBUTES */
    /* Factories: */
    /**
     * Factories property: The default ApplicationFactory key specified in the properties file.
     */
    public static final String FACTORIES_APPLICATIONFACTORY                     = "FACTORIES_APPLICATIONFACTORY";
    /**
     * Factories property: The default EndPointFactory key specified in the properties file.
     */
    public static final String FACTORIES_ENDPOINTFACTORY                        = "FACTORIES_ENDPOINTFACTORY";

    /* Specific classes: */
    /**
     * Factories property: Default key specified in the properties file that
     * identifies implementation class for Application interface.
     */
    public static final String FACTORIES_APPLICATION                            = "FACTORIES_APPLICATION";
    /**
     * Factories property: Default key specified in the properties file that
     * identifies implementation class for EndPoint interface.
     */
    public static final String FACTORIES_ENDPOINT                               = "FACTORIES_ENDPOINT";

    
    /* *********************** SIMULATOR PROPERTIES NAMES ***********************/
    
    /* REQUIRED */
    /**
     * Simulator property: Default key specified in the properties file that
     * identifies the number of stabilization steps for any node at join or leave.
     */
    public static final String SIMULATOR_SIMULATION_STEPS                       = "SIMULATOR_SIMULATION_STEPS";
    /**
     * Simulator property: Default key specified in the properties file that
     * identifies the log level.
     */
    public static final String SIMULATOR_LOG_LEVEL                              = "SIMULATOR_LOG_LEVEL";
    /**
     * Simulator property: Default key specified in the properties file that
     * identifies the print level for whole network.
     */
    public static final String SIMULATOR_PRINT_LEVEL                            = "SIMULATOR_PRINT_LEVEL";
    /**
     * Simulator property: Default key specified in the properties file that
     * identifies the environment for the current simulation.
     */
    public static final String SIMULATOR_ENVIRONMENT                            = "SIMULATOR_ENVIRONMENT";
    /**
     * Simulator property: Default key specified in the properties file that
     * identifies the queue size.
     */
    public static final String SIMULATOR_QUEUE_SIZE                             = "SIMULATOR_QUEUE_SIZE";
    /**
     * Simulator property: Default key specified in the properties file that
     * identifies the message processing.
     */
    public static final String SIMULATOR_PROCESSED_MESSAGES                     = "SIMULATOR_PROCESSED_MESSAGES";

    /* OPTIONAL */
    /**
     * Simulator property: Default key specified in the properties file that
     * identifies the events filename to load.
     */
    public static final String SIMULATOR_EVENT_FILE                             = "SIMULATOR_EVENT_FILE";
    

    /* *********************** SERIALIZATION PROPERTIES NAMES ***********************/
    /* Test dependant: All these attributes are optionals */

    /**
     * Serialization property: Default key specified in the properties file
     * that identifies serialized file that contains the network to be loaded.
     */
    public static final String SERIALIZATION_INPUT_FILE                         = "SERIALIZATION_INPUT_FILE";
    /**
     * Serialization property: Default key specified in the properties file
     * that identifies filename to which serialize the final state.
     */
    public static final String SERIALIZATION_OUTPUT_FILE                        = "SERIALIZATION_OUTPUT_FILE";
    /**
     * Serialization property: Default key specified in the properties file
     * that identifies if the output file must be replaced with new outputs.
     */
    public static final String SERIALIZATION_REPLACE_OUTPUT_FILE                = "SERIALIZATION_REPLACE_OUTPUT_FILE";
    
    
    /* *********************** BEHAVIOURS PROPERTIES NAMES ***********************/
    /* Overlay dependant: All these attributes are optionals */
    
    /**
     * Behaviours property: Default key specified in the properties file that
     * identifies implementation class for BehaviourFactory interface.
     */
    public static final String BEHAVIOURS_FACTORY                               = "BEHAVIOURS_FACTORY";
    /**
     * Behaviours property: Default key specified in the properties file that
     * identifies implementation class for BehaviourPool interface.
     */
    public static final String BEHAVIOURS_POOL                                  = "BEHAVIOURS_POOL";
    /**
     * Behaviours property: Default key specified in the properties file that
     * identifies implementation class for BehaviourRoleSelector interface.
     */
    public static final String BEHAVIOURS_ROLESELECTOR                          = "BEHAVIOURS_ROLESELECTOR";
    /**
     * Behaviours property: Default key specified in the properties file that
     * identifies implementation class for BehaviourInvoker interface.
     */
    public static final String BEHAVIOURS_INVOKER                               = "BEHAVIOURS_INVOKER";
    /**
     * Behaviours property: Default key specified in the properties file that
     * identifies implementation class for BehaviourFilter interface.
     */
    public static final String BEHAVIOURS_FILTER                                = "BEHAVIOURS_FILTER";
    /**
     * Behaviours property: Default key specified in the properties file that
     * identifies implementation class for BehaviourPattern interface.
     */
    public static final String BEHAVIOURS_PATTERN                               = "BEHAVIOURS_PATTERN";
    /**
     * Behaviours property: Default key specified in the properties file that
     * identifies implementation class for PropertiesInitializer interface.
     */
    public static final String BEHAVIOURS_PROPERTIES                            = "BEHAVIOURS_PROPERTIES";
    /**
     * Behaviours property: Default key specified in the properties file that
     * identifies the number of message types used in the current overlay.
     */
    public static final String BEHAVIOURS_NUMBEROFTYPES                         = "BEHAVIOURS_NUMBEROFTYPES";
    /**
     * Behaviours property: Default key specified in the properties file that
     * identifies the number of message modes used in the current overlay.
     */
    public static final String BEHAVIOURS_NUMBEROFMODES                         = "BEHAVIOURS_NUMBEROFMODES";
    

    /* *********************** OVERLAY PROPERTIES NAMES ***********************/
    /* Required: */
    
    /**
     * Overlay property: Default key specified in the properties file that
     * identifies implementation class for Id interface.
     */
    public static final String OVERLAY_ID                                       = "OVERLAY_ID";
    /**
     * Overlay property: Default key specified in the properties file that
     * identifies implementation class for Node interface.
     */
    public static final String OVERLAY_NODE                                     = "OVERLAY_NODE";
    /**
     * Overlay property: Default key specified in the properties file that
     * identifies implementation class for OverlayProperties interface.
     */
    public static final String OVERLAY_PROPERTIES                               = "OVERLAY_PROPERTIES";
    /**
     * Overlay property: Default key specified in the properties file that
     * identifies if this overlay implementation uses behaviours.
     */
    public static final String OVERLAY_WITH_BEHAVIOURS                          = "OVERLAY_WITH_BEHAVIOURS";
    

    /* *********************** RESULTS PROPERTIES NAMES ***********************/
    /* Test dependant: All these attributes are optionals */
    
    /**
     * Results property: Default key specified in the properties file that
     * identifies implementation class for ResultsFactory interface.
     */
    public static final String RESULTS_FACTORY                                  ="RESULTS_FACTORY";
    /**
     * Results property: Default key specified in the properties file that
     * identifies implementation class for ResultsEdge interface.
     */
    public static final String RESULTS_EDGE                                     ="RESULTS_EDGE";
    /**
     * Results property: Default key specified in the properties file that
     * identifies implementation class for ResultsConstraint interface.
     */
    public static final String RESULTS_CONSTRAINT                               ="RESULTS_CONSTRAINT";
    /**
     * Results property: Default key specified in the properties file that
     * identifies implementation class for ResultsGenerator interface.
     */
    public static final String RESULTS_GENERATOR                                ="RESULTS_GENERATOR";
    /**
     * Results property: Default key specified in the properties file that
     * identifies implementation class for PropertiesInitializer interface.
     */
    public static final String RESULTS_PROPERTIES                               ="RESULTS_PROPERTIES";
    /**
     * Results property: Default key specified in the properties file that
     * identifies the unique names for results types.
     */
    public static final String RESULTS_UNIQUE_NAME                              ="RESULTS_UNIQUE_NAME";

////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    /* ************************** FACTORIES PROPERTIES ********************/
    
    /* REQUIRED ATTRIBUTES */
    /* Factories: */
    /**
     * Factories property: The NetworkFactory implementation to use in current simulation.
     */
    public static Class factoriesNetworkFactory                                 = null;
    /**
     * Factories property: The IdFactory implementation to use in current simulation.
     */
    public static Class factoriesIdFactory                                      = null;
    /**
     * Factories property: The NodeHandleFactory implementation to use in current simulation.
     */
    public static Class factoriesNodeHandleFactory                              = null;
    /**
     * Factories property: The NodeFactory implementation to use in current simulation.
     */
    public static Class factoriesNodeFactory                                    = null;
    /**
     * Factories property: The RouteMessagePool implementation to use in current simulation.
     */
    public static Class factoriesRouteMessagePool                               = null;

    /* Specific classes: */
    /**
     * Factories property: The Network implementation to use in current simulation.
     */
    public static Class factoriesNetwork                                        = null;
    /**
     * Factories property: The NodeHandle implementation to use in current simulation.
     */
    public static Class factoriesNodeHandle                                     = null;
    /**
     * Factories property: The RouteMessage implementation to use in current simulation.
     */
    public static Class factoriesRouteMessage                                   = null;
    /**
     * Factories property: The network topology for the current simulated overlay.
     */
    public static String factoriesNetworkTopology                               = null;
    /**
     * Factories property: The network size (number of nodes) that must appear
     * in the current simulation.
     */
    public static int factoriesNetworkSize                                      = 0;

    /* OPTIONAL ATTRIBUTES */
    /* Factories: */
    /**
     * Factories property: The ApplicationFactory implementation to use in current simulation.
     */
    public static Class factoriesApplicationFactory                             = null;
    /**
     * Factories property: The EndPointFactory implementation to use in current simulation.
     */
    public static Class factoriesEndPointFactory                                = null;

    /* Specific classes: */
    /**
     * Factories property: The Application implementation to use in current simulation.
     */
    public static Class factoriesApplication                                    = null;
    /**
     * Factories property: The EndPoint implementation to use in current simulation.
     */
    public static Class factoriesEndPoint                                       = null;

    
    /* *********************** SIMULATOR PROPERTIES NAMES ***********************/
    
    /* REQUIRED */
    /**
     * Simulator property: Stabilization steps to simulate for any node at join 
     * or leave.
     */
    public static int simulatorSimulationSteps                                  = 0;
    /**
     * Simulator property: Required log level in the current simulation.
     */
    public static int simulatorLogLevel                                         = 0;
    /**
     * Simulator property: Required print level for whole network.
     */
    public static int simulatorPrintLevel                                       = 0;
    /**
     * Simulator property: Specifies that the network will not be printed out.
     */
    public static final int SIMULATOR_NO_PRINT                                  = 0;
    /**
     * Simulator property: Specifies that permits the network.prettyPrintNodes() invokation.
     */
    public static final int SIMULATOR_PRETTY_PRINT                              = 1;
    /**
     * Simulator property: Specifies that permits the network.printNodes() invokation.
     */
    public static final int SIMULATOR_FULL_PRINT                                = 2;
    /**
     * Simulator property: Environment for the current simulation.
     */
    public static String simulatorEnvironment                                   = null;
    /**
     * Simulator property: Specifies the Simulation environment, based on steps.
     */
    public static final String SIMULATOR_SIMULATION_ENVIRONMENT                 = "SIMULATION";
    /**
     * Simulator property: Specifies the Experimental environment, based on time 
     * (not on steps).
     */
    public static final String SIMULATOR_EXPERIMENTAL_ENVIRONMENT               = "EXPERIMENTAL";
    /**
     * Simulator property: The maximum queue size for the nodes.
     */
    public static int simulatorQueueSize                                        = 0;
    /**
     * Simulator property: The maximum number of processed messages per step and
     * node.
     */
    public static int simulatorProcessedMessages                                = 0;

    /* OPTIONAL */
    /**
     * Simulator property: The events filename to be loaded.
     */
    public static String simulatorEventFile                                     = null;
    

    /* *********************** SERIALIZATION PROPERTIES NAMES ***********************/
    /* Test dependant: All these attributes are optionals */

    /**
     * Serialization property: Identifies the serialized file that contains the 
     * network to be loaded.
     */
    public static String serializedInputFile                                    = null;
    /**
     * Serialization property: Identifies filename to which serialize the final 
     * state.
     */
    public static String serializedOutputFile                                   = null;
    /**
     * Serialization property: Identifies if the output file must be replaced 
     * with new outputs.
     */
    public static boolean serializedOutputFileReplaced                          = true;
    
    
    /* *********************** BEHAVIOURS PROPERTIES NAMES ***********************/
    /* Overlay dependant: All these attributes are optionals */
    
    /**
     * Behaviours property: Implementation class for BehaviourFactory interface.
     */
    public static Class behavioursFactory                                       = null; 
    /**
     * Behaviours property: Implementation class for BehaviourPool interface.
     */
    public static Class behavioursPool                                          = null;
    /**
     * Behaviours property: Implementation class for BehaviourRoleSelector interface.
     */
    public static Class behavioursRoleSelector                                  = null;
    /**
     * Behaviours property: Implementation class for BehaviourInvoker interface.
     */
    public static Class behavioursInvoker                                       = null;
    /**
     * Behaviours property: Implementation class for BehaviourFilter interface.
     */
    public static Class behavioursFilter                                        = null;
    /**
     * Behaviours property: Implementation class for BehaviourPattern interface.
     */
    public static Class behavioursPattern                                       = null;
    /**
     * Behaviours property: Implementation class for the PropertiesInitializer interface.
     */
    public static Class behavioursProperties                                    = null;
    /**
     * Behaviours property: Instance of the implementation class for the 
     * PropertiesInitializer interface.
     */
    public static PropertiesInitializer behavioursPropertiesInstance            = null;
    /**
     * Behaviours property: Number of message types used in the current
     * overlay implementation.
     */
    public static int behavioursNumberOfTypes                                   = 0;
    /**
     * Behaviours property: Number of message modes used in the current
     * overlay implementation.
     */
    public static int behavioursNumberOfModes                                   = 0;

    /* *********************** OVERLAY PROPERTIES NAMES ***********************/
    /* Required: */
    
    /**
     * Overlay property: Implementation class for Id interface.
     */
    public static Class overlayId                                               = null;
    /**
     * Overlay property: Implementation class for Node interface.
     */
    public static Class overlayNode                                             = null;
    /**
     * Overlay property: Implementation class for PropertiesInitializer interface.
     */
    public static Class overlayProperties                                       = null;
    /**
     * Overlay property: Instance of the implementation class for 
     * PropertiesInitializer interface for the current overlay.
     */
    public static OverlayProperties overlayPropertiesInstance                   = null;
    /**
     * Overlay property: Identifies if this overlay implementation uses 
     * behaviours. If true, the behaviours properties must be loaded successfully
     * before the begining of the simulation.
     */
    public static boolean overlayWithBehaviours                                 = false;
    

    /* *********************** RESULTS PROPERTIES NAMES ***********************/
    /* Test dependant: All these attributes are optionals */
    
    /**
     * Results property: Contains all Class instances for each Factory implementation.
     * These instances appear in the same order than in file.
     */
    public static Vector resultsFactory                                         = null;
    /**
     * Results property: Contains all Class instances for each Edge implementation.
     * These instances appear in the same order than in file.
     */
    public static Vector resultsEdge                                            = null;
    /**
     * Results property: Contains all Class intances for each Constraint implementation.
     * These instances appear in the same order than in file.
     */
    public static Vector resultsConstraint                                      = null;
    /**
     * Results property: Contains all Class instances for each Generator implementation.
     * These instances appear in the same order than in file.
     */
    public static Vector resultsGenerator                                       = null;
    /**
     * Results property: Contains all Class instances for each PropertiesInitializer implementation.
     * These instances appear in the same order than in file.
     */
    public static Vector resultsProperties                                      = null;
    /**
     * Results property: Contains all instances for each PropertiesInitializer implementation,
     * once them have been built and initialized.
     * These instances appear in the same order than their specification in file.
     */
    public static Vector resultsPropertiesInstance                              = null;
    /**
     * Results property: Contains (String,Integer) pairs, where the String is
     * the unique name for a result type, and the Integer shows its position
     * into the list of possible unique names.
     */
    public static TreeMap resultsUniqueName                                     = null;

    /* ****************************** INTERNAL PROPERTIES *********************/
    /**
     * Internal property: To contain all attributes loaded from the external file.
     */
    private static PropertiesWrapper properties                                 = null;
    /**
     * Internal property: Shows when the applicaton level has been activated.
     */
    private static boolean activatedApplicationLevel                            = false;
    /**
     * Internal property: Shows when the netwokr building by events has been activated.
     */
    private static boolean activatedEvents                                      = false;
    /**
     * Internal property: Shows when the serialization has been activated.
     */
    private static boolean activatedSerialization                               = false;
    /**
     * Internal property: Shows when the results have been activated.
     */
    private static boolean activatedResults                                     = false;
    /**
     * Internal property: Class array for temporal uses in implementedInterface
     * method.
     */
    private static Class[] implementedInterfaceTemp                             = null;

////////////////////////////////////////////////////////////////////////////////////////////////////////////
    

    /**
     * Initialize all required attributes for the current simulation. If the 
     * overlay uses behaviours, these attributes also are loaded. If you want to
     * use other test dependant attributes, you have to activate them
     * individually. After this method invokation all optional fields must be
     * activated explicitly.
     * @param masterFilename Filename that contains the file specification
     * that have all required attributes for the current simulation.
     * @param mainPropertyName The key that appears into the <b>masterFilename</b>
     * with the final filename with required configuration.
     * @throws InitializationException if any error has ocurred during the
     * initialization.
     * @see Properties#activateApplicationLevelAttributes()
     * @see Properties#activateEventsAttributes()
     * @see Properties#activateSerializationAttributes()
     * @see Properties#activateResultsAttributes()
     */
    public static void init(String masterFilename, String mainPropertyName) throws InitializationException
    {
        resetAttributes();
        properties = loadProperties(masterFilename, mainPropertyName);
        loadFactoriesAttributes();
        loadSimulatorAttributes();
        loadOverlayAttributes();
        if (Properties.overlayWithBehaviours)
            loadBehavioursAttributes();
        
    }
    
    /**
     * Makes the postinitialization process for all requried PropertiesInitializers instances.
     * @throws InitializationException if an error occurs during
     * the initialization of the different properties.
     * @see planet.util.PropertiesInitializer#postinit(planet.util.PropertiesWrapper)
     */
    public static void postinit() throws InitializationException
    {
        Properties.overlayPropertiesInstance.postinit(properties);
        if (Properties.overlayWithBehaviours)
            Properties.behavioursPropertiesInstance.postinit(properties);
        if (Properties.activatedResults)
            postInitResults();
    }
    
    /**
     * Resets all attributes to its initial values or null by default.
     */
    private static void resetAttributes()
    {
        /* Factories attributes: */
        factoriesNetworkFactory        = null;
        factoriesIdFactory             = null;
        factoriesNodeHandleFactory     = null;
        factoriesNodeFactory           = null;
        factoriesRouteMessagePool      = null;
        
        factoriesNetwork               = null;
        factoriesNodeHandle            = null;
        factoriesRouteMessage          = null;
        factoriesNetworkTopology       = null;
        factoriesNetworkSize           = 0;
        
        factoriesApplicationFactory    = null;
        factoriesEndPointFactory       = null;

        factoriesApplication           = null;
        factoriesEndPoint              = null;

        /* Simulator attributes: */
        simulatorSimulationSteps       = 0;
        simulatorLogLevel              = 0;
        simulatorPrintLevel            = 0;
        simulatorEnvironment           = null;
        simulatorQueueSize             = 0;
        simulatorProcessedMessages     = 0;

        simulatorEventFile             = null;
        
        /* Serialization attributes: */
        serializedInputFile            = null;
        serializedOutputFile           = null;
        serializedOutputFileReplaced   = true;
        
        /* Behaviours attributes: */
        behavioursFactory              = null; 
        behavioursPool                 = null;
        behavioursRoleSelector         = null;
        behavioursInvoker              = null;
        behavioursFilter               = null;
        behavioursPattern              = null;
        behavioursProperties           = null;
        behavioursPropertiesInstance   = null;
        behavioursNumberOfTypes        = 0;
        behavioursNumberOfModes        = 0;

        /* Overlay attributes: */
        overlayId                      = null;
        overlayNode                    = null;
        overlayProperties              = null;
        overlayPropertiesInstance      = null;
        overlayWithBehaviours          = false;
        
        /* Results attributes: */
        resultsFactory                 = null;
        resultsEdge                    = null;
        resultsConstraint              = null;
        resultsGenerator               = null;
        resultsProperties              = null;
        resultsPropertiesInstance      = null;
        resultsUniqueName              = null;

        /* Internal attributes: */
        properties                     = null;
        activatedApplicationLevel      = false;
        activatedEvents                = false;
        activatedSerialization         = false;
        activatedResults               = false;
    }
    
    /**
     * Makes the postinitialization process for each result properties.
     * @throws InitializationException if an error occurs during
     * the initialization of the different properties.
     * @see planet.util.PropertiesInitializer#postinit(planet.util.PropertiesWrapper)
     */
    private static void postInitResults() throws InitializationException
    {
        for (int i=0; i < Properties.resultsPropertiesInstance.size(); i++)
        {
            ((PropertiesInitializer)Properties.resultsPropertiesInstance.get(i)).postinit(properties);
        }
    }
    
    /**
     * Loads the entire specific properties file for the current simulation.
     * The steps to do it are the followings:
     * <ol type="1">
     * <li> Loads the <b>masterFilename</b> with the filename of the specific properties file to use.</li>
     * <li> Loads the specific properties file with the attributes for the current simulation.</li>
     * </ol>
     * @param masterFilename Master filename where is specified the filename
     * with the specific properties file for the current simulation.
     * @param mainPropertyName The key that appears into the <b>masterFilename</b>
     * with the final filename with required configuration.
     * @return The java.util.Properties with all specific properties for the current simulation.
     * @throws InitializationException if occurs any error opening the files or loading the properties.
     */
    private static PropertiesWrapper loadProperties(String masterFilename, String mainPropertyName) throws InitializationException
    {
        FileInputStream fis = null;
        PropertiesWrapper prop = new PropertiesWrapper(); 
        
        //loads the master properties file, with the name of the specific properties file
        try {
            fis = new FileInputStream(masterFilename);
            prop.load(fis);
            fis.close();
        } catch (FileNotFoundException e) {
            throw new InitializationException("The file '"+masterFilename+"' not exists",e);
        } catch (IOException e) {
            throw new InitializationException("Cannot load the file '"+masterFilename+"'",e);
        }
        
        //loads the specified properties file for the current simulation
        String filename = prop.getProperty(mainPropertyName);
        if (filename == null)
            throw new InitializationException("The properties file '"+masterFilename+"' doesn't contain the main property '"+mainPropertyName+"'");
        prop.clear();
        try {
            fis = new FileInputStream(filename);
            prop.load(fis);
            fis.close();
        } catch (FileNotFoundException e) {
            throw new InitializationException("The properties file '"+filename+"' specified in the master file not exists",e);
        } catch (IOException e) {
            throw new InitializationException("Cannot load the properties file '"+filename+"' specified in the master file",e);
        }
        return prop;
    }

    /**
     * Builds an instance of the PropertiesInitializer implementation and loads
     * its specific attributes.
     * @param classReference Class reference to use to build a new instance.
     * @return A new built and initialized instance of the related class.
     * @throws InitializationException if any error occurs during the 
     * initialization.
     */
    private static PropertiesInitializer loadPropertiesInitializer(Class classReference) throws InitializationException
    {
        PropertiesInitializer props = null;
        try {
            props = (PropertiesInitializer)classReference.newInstance();
            props.init(properties); //loads its specific properties
            return props;
        } catch (Exception e)
        {
            throw new InitializationException("Cannot build an instance of PropertiesInitializer named '"+classReference.getName()+"'",e);
        }
    }

    /**
     * Load the base implementation classes to run the current simulation.
     * @throws InitializationException if any error has ocurred during the
     * loading process.
     */
    private static void loadFactoriesAttributes() throws InitializationException
    {
        /* Factories classes: */
        Properties.factoriesNetworkFactory    = properties.getPropertyAsClass(FACTORIES_NETWORKFACTORY);
        Properties.factoriesIdFactory         = properties.getPropertyAsClass (FACTORIES_IDFACTORY);
        Properties.factoriesNodeHandleFactory = properties.getPropertyAsClass (FACTORIES_NODEHANDLEFACTORY);
        Properties.factoriesNodeFactory       = properties.getPropertyAsClass (FACTORIES_NODEFACTORY);
        Properties.factoriesRouteMessagePool  = properties.getPropertyAsClass (FACTORIES_ROUTEMESSAGEPOOL);

        /* Specific classes: */
        Properties.factoriesNetwork           = properties.getPropertyAsClass (FACTORIES_NETWORK);
        Properties.factoriesNodeHandle        = properties.getPropertyAsClass (FACTORIES_NODEHANDLE);
        Properties.factoriesRouteMessage      = properties.getPropertyAsClass (FACTORIES_ROUTEMESSAGE);
        
        /* Other attributes: */
        Properties.factoriesNetworkTopology   = properties.getProperty(FACTORIES_NETWORKTOPOLOGY);
        Properties.factoriesNetworkSize       = properties.getPropertyAsInt(FACTORIES_NETWORKSIZE);
        if (Properties.factoriesNetworkSize < 0) {
            throw new InitializationException("The network size '"+Properties.factoriesNetworkSize+"' are invalid. Must be equal to or greater than zero.");
        }
        
        //testing interfaces implementation
        Interfaces.ensureImplementedInterfaceOrClass(Properties.factoriesNetworkFactory,   Interfaces.FACTORIES_NETWORKFACTORY,   FACTORIES_NETWORKFACTORY);
        Interfaces.ensureImplementedInterfaceOrClass(Properties.factoriesIdFactory,        Interfaces.FACTORIES_IDFACTORY,        FACTORIES_IDFACTORY);
        Interfaces.ensureImplementedInterfaceOrClass(Properties.factoriesNodeHandleFactory,Interfaces.FACTORIES_NODEHANDLEFACTORY,FACTORIES_NODEHANDLEFACTORY);
        Interfaces.ensureImplementedInterfaceOrClass(Properties.factoriesNodeFactory,      Interfaces.FACTORIES_NODEFACTORY,      FACTORIES_NODEFACTORY);
        Interfaces.ensureImplementedInterfaceOrClass(Properties.factoriesRouteMessagePool, Interfaces.FACTORIES_ROUTEMESSAGEPOOL, FACTORIES_ROUTEMESSAGEPOOL);
        
        Interfaces.ensureImplementedInterfaceOrClass(Properties.factoriesNetwork,          Interfaces.FACTORIES_NETWORK,          FACTORIES_NETWORK);
        Interfaces.ensureImplementedInterfaceOrClass(Properties.factoriesNodeHandle,       Interfaces.FACTORIES_NODEHANDLE,       FACTORIES_NODEHANDLE);
        Interfaces.ensureImplementedInterfaceOrClass(Properties.factoriesRouteMessage,     Interfaces.FACTORIES_ROUTEMESSAGE,     FACTORIES_ROUTEMESSAGE);
    }

    /**
     * Loads all specific simulator attributes from the current properties.
     * @throws InitializationException if occurs any error during the loading process
     * of the attributes.
     */
    private static void loadSimulatorAttributes() throws InitializationException
    {
        Properties.simulatorSimulationSteps   = properties.getPropertyAsInt(SIMULATOR_SIMULATION_STEPS);
        Properties.simulatorLogLevel          = properties.getPropertyAsInt(SIMULATOR_LOG_LEVEL);
        Properties.simulatorPrintLevel        = properties.getPropertyAsInt(SIMULATOR_PRINT_LEVEL);
        Properties.simulatorEnvironment       = properties.getProperty(SIMULATOR_ENVIRONMENT);
        Properties.simulatorQueueSize         = properties.getPropertyAsInt(SIMULATOR_QUEUE_SIZE);
        Properties.simulatorProcessedMessages = properties.getPropertyAsInt(SIMULATOR_PROCESSED_MESSAGES);
        
        //testing the correctness of the values
        ensureValidSimulatorEnvironment(Properties.simulatorEnvironment);
        ensureValidPrintLevel(Properties.simulatorPrintLevel);
    }
    
    /**
     * Test if the <b>environment</b> is a valid simulator environment.
     * @param environment The value to be tested.
     * @throws InitializationException if the <b>environment</b> has an 
     * incorrect value.
     */
    private static void ensureValidSimulatorEnvironment(String environment) throws InitializationException
    {
        if (environment!=null && 
            (!environment.equals(Properties.SIMULATOR_EXPERIMENTAL_ENVIRONMENT) &&
             !environment.equals(Properties.SIMULATOR_SIMULATION_ENVIRONMENT)))
            throw new InitializationException("The environment specified at property '"+
                    Properties.SIMULATOR_ENVIRONMENT+"' are unknown (found '"+environment+"'). The correct values are: "+
                    Properties.SIMULATOR_EXPERIMENTAL_ENVIRONMENT+", "+
                    Properties.SIMULATOR_SIMULATION_ENVIRONMENT);
    }
    
    /**
     * Test if the <b>printLevel</b> is a valid print level.
     * @param printLevel The value to be tested.
     * @throws InitializationException if the <b>printLevel</b> has an 
     * incorrect value.
     */
    private static void ensureValidPrintLevel(int printLevel) throws InitializationException
    {
        switch (printLevel)
        {
        case SIMULATOR_NO_PRINT:
        case SIMULATOR_PRETTY_PRINT:
        case SIMULATOR_FULL_PRINT: return;
        default: throw new InitializationException("The print level specified at property '"
                + Properties.SIMULATOR_PRINT_LEVEL+"' are unknown. The correct values are: 0, 1, 2");
        }
    }

    
    /**
     * Loads all specific overlay attributes from the current properties.
     * @throws InitializationException if occurs any error during the loading process
     * of the attributes.
     */
    private static void loadOverlayAttributes() throws InitializationException
    {
        Properties.overlayId                 = properties.getPropertyAsClass(OVERLAY_ID);
        Properties.overlayNode               = properties.getPropertyAsClass(OVERLAY_NODE);
        Properties.overlayProperties         = properties.getPropertyAsClass(OVERLAY_PROPERTIES);
        Properties.overlayWithBehaviours     = properties.getPropertyAsBoolean(OVERLAY_WITH_BEHAVIOURS);
        
        //testing the correctness of the values
        Interfaces.ensureImplementedInterfaceOrClass(Properties.overlayId,        Interfaces.OVERLAY_ID,        OVERLAY_ID);
        Interfaces.ensureImplementedInterfaceOrClass(Properties.overlayNode,      Interfaces.OVERLAY_NODE,      OVERLAY_NODE);
        Interfaces.ensureImplementedInterfaceOrClass(Properties.overlayProperties,Interfaces.OVERLAY_PROPERTIES,OVERLAY_PROPERTIES);
        
        //ending the loading process
        Properties.overlayPropertiesInstance = (OverlayProperties)loadPropertiesInitializer(Properties.overlayProperties);
    }

    /**
     * Loads the specific behaviours attributes for the current simulation.
     * Only should be loaded if the current overlay use them.
     * @throws InitializationException if any error has ocurred during the 
     * loading process.
     */
    private static void loadBehavioursAttributes() throws InitializationException
    {
        Properties.behavioursFactory            = properties.getPropertyAsClass(BEHAVIOURS_FACTORY);
        Properties.behavioursPool               = properties.getPropertyAsClass(BEHAVIOURS_POOL);
        Properties.behavioursRoleSelector       = properties.getPropertyAsClass(BEHAVIOURS_ROLESELECTOR);
        Properties.behavioursInvoker            = properties.getPropertyAsClass(BEHAVIOURS_INVOKER);
        Properties.behavioursFilter             = properties.getPropertyAsClass(BEHAVIOURS_FILTER);
        Properties.behavioursPattern            = properties.getPropertyAsClass(BEHAVIOURS_PATTERN);
        Properties.behavioursProperties         = properties.getPropertyAsClass(BEHAVIOURS_PROPERTIES);
        
        Properties.behavioursNumberOfTypes      = properties.getPropertyAsInt(BEHAVIOURS_NUMBEROFTYPES);
        Properties.behavioursNumberOfModes      = properties.getPropertyAsInt(BEHAVIOURS_NUMBEROFMODES);
        
        //testing the correctness of the values
        Interfaces.ensureImplementedInterfaceOrClass(Properties.behavioursFactory,     Interfaces.BEHAVIOURS_FACTORY,     BEHAVIOURS_FACTORY);
        Interfaces.ensureImplementedInterfaceOrClass(Properties.behavioursPool,        Interfaces.BEHAVIOURS_POOL,        BEHAVIOURS_POOL);
        Interfaces.ensureImplementedInterfaceOrClass(Properties.behavioursRoleSelector,Interfaces.BEHAVIOURS_ROLESELECTOR,BEHAVIOURS_ROLESELECTOR);
        Interfaces.ensureImplementedInterfaceOrClass(Properties.behavioursInvoker,     Interfaces.BEHAVIOURS_INVOKER,     BEHAVIOURS_INVOKER);
        Interfaces.ensureImplementedInterfaceOrClass(Properties.behavioursFilter,      Interfaces.BEHAVIOURS_FILTER,      BEHAVIOURS_FILTER);
        Interfaces.ensureImplementedInterfaceOrClass(Properties.behavioursPattern,     Interfaces.BEHAVIOURS_PATTERN,     BEHAVIOURS_PATTERN);
        Interfaces.ensureImplementedInterfaceOrClass(Properties.behavioursProperties,  Interfaces.BEHAVIOURS_PROPERTIES,  BEHAVIOURS_PROPERTIES);
        
        //ending the loading process
        Properties.behavioursPropertiesInstance = loadPropertiesInitializer(Properties.behavioursProperties);
    }
    
    /**
     * Loads all required attributes for be able to use the application level.
     * @throws InitializationException if cannot load any required attribute.
     */
    public static void activateApplicationLevelAttributes() throws InitializationException
    {
        if (Properties.activatedApplicationLevel) return;
        
        Properties.activatedApplicationLevel   = true;
        /* Factory classes: */
        Properties.factoriesApplicationFactory = properties.getPropertyAsClass(FACTORIES_APPLICATIONFACTORY);
        Properties.factoriesEndPointFactory    = properties.getPropertyAsClass(FACTORIES_ENDPOINTFACTORY);

        /* Specific classes: */
        Properties.factoriesApplication        = properties.getPropertyAsClass(FACTORIES_APPLICATION);
        Properties.factoriesEndPoint           = properties.getPropertyAsClass(FACTORIES_ENDPOINT);
        
        //testing the correctness of the values
        Interfaces.ensureImplementedInterfaceOrClass(Properties.factoriesApplicationFactory,Interfaces.FACTORIES_APPLICATIONFACTORY,FACTORIES_APPLICATIONFACTORY);
        Interfaces.ensureImplementedInterfaceOrClass(Properties.factoriesEndPointFactory,   Interfaces.FACTORIES_ENDPOINTFACTORY,   FACTORIES_ENDPOINTFACTORY);
        Interfaces.ensureImplementedInterfaceOrClass(Properties.factoriesApplication,       Interfaces.FACTORIES_APPLICATION,       FACTORIES_APPLICATION);
        Interfaces.ensureImplementedInterfaceOrClass(Properties.factoriesEndPoint,          Interfaces.FACTORIES_ENDPOINT,          FACTORIES_ENDPOINT);
    }

    /**
     * Permits the use of files with events to be loaded into the current
     * simulation.
     * @throws InitializationException if any error has ocurred during the
     * loading process.
     */
    public static void activateEventsAttributes() throws InitializationException
    {
        if (Properties.activatedEvents) return;
        
        Properties.activatedEvents    = true;
        
        Properties.simulatorEventFile = properties.getProperty(SIMULATOR_EVENT_FILE);
    }
    
    /**
     * Permits the loading, saving process of a serialized network. A loaded
     * serialized network can be used for the current simulation without any
     * other cost (without its creation and stabilization processes). You 
     * can save the current simulation state to use it in the future wihtout any
     * other cost.
     * @throws InitializationException if any error has ocurred during the
     * loading process.
     */
    public static void activateSerializationAttributes() throws InitializationException
    {
        if (Properties.activatedSerialization) return;
        
        Properties.activatedSerialization       = true;
        
        Properties.serializedInputFile          = properties.getProperty(SERIALIZATION_INPUT_FILE);
        Properties.serializedOutputFile         = properties.getProperty(SERIALIZATION_OUTPUT_FILE);
        Properties.serializedOutputFileReplaced = properties.getPropertyAsBoolean(SERIALIZATION_REPLACE_OUTPUT_FILE);
    }
    
    /**
     * Loads all results types specified in the properties file.
     * @throws InitializationException if any error occurs during the 
     * loading process.
     */
    public static void activateResultsAttributes() throws InitializationException
    {
        if ( Properties.activatedResults) return;
        
        Properties.activatedResults = true;
       
        StringTokenizer resultsFactory    = new StringTokenizer(properties.getProperty(RESULTS_FACTORY),    ",");
        StringTokenizer resultsEdge       = new StringTokenizer(properties.getProperty(RESULTS_EDGE),       ",");
        StringTokenizer resultsConstraint = new StringTokenizer(properties.getProperty(RESULTS_CONSTRAINT), ",");
        StringTokenizer resultsGenerator  = new StringTokenizer(properties.getProperty(RESULTS_GENERATOR),  ",");
        StringTokenizer resultsProperties = new StringTokenizer(properties.getProperty(RESULTS_PROPERTIES), ",");
        StringTokenizer resultsUniqueName = new StringTokenizer(properties.getProperty(RESULTS_UNIQUE_NAME),",");
        
        final int MAX_ELEMS = resultsUniqueName.countTokens();

        Properties.resultsFactory            = new Vector(MAX_ELEMS);
        Properties.resultsEdge               = new Vector(MAX_ELEMS);
        Properties.resultsConstraint         = new Vector(MAX_ELEMS);
        Properties.resultsGenerator          = new Vector(MAX_ELEMS);
        Properties.resultsProperties         = new Vector(MAX_ELEMS);
        Properties.resultsPropertiesInstance = new Vector(MAX_ELEMS);
        Properties.resultsUniqueName         = new TreeMap();
        
        //testing if there are the same number of items
        if (resultsFactory.countTokens()    != MAX_ELEMS ||
            resultsEdge.countTokens()       != MAX_ELEMS ||
            resultsConstraint.countTokens() != MAX_ELEMS ||
            resultsGenerator.countTokens()  != MAX_ELEMS ||
            resultsProperties.countTokens() != MAX_ELEMS)
            throw new InitializationException("There are not the same number of items in the results attributes");
        
        //loading the items
        String uniqueName = null;
        Class factory     = null;
        Class edge        = null;
        Class constraint  = null;
        Class generator   = null;
        Class prop        = null;
        for (int i =0; i < MAX_ELEMS; i++)
        {
            uniqueName = resultsUniqueName.nextToken().trim();
            if (uniqueName.equals("")) throw new InitializationException("An empty string is not a valid results type name");
            
            Properties.resultsUniqueName.put(uniqueName,new Integer(i));
            //loading process
            factory    = PropertiesWrapper.getValueAsClass(resultsFactory.nextToken().trim(),RESULTS_FACTORY);
            edge       = PropertiesWrapper.getValueAsClass(resultsEdge.nextToken().trim(),RESULTS_EDGE);
            constraint = PropertiesWrapper.getValueAsClass(resultsConstraint.nextToken().trim(),RESULTS_CONSTRAINT);
            generator  = PropertiesWrapper.getValueAsClass(resultsGenerator.nextToken().trim(),RESULTS_GENERATOR);
            prop       = PropertiesWrapper.getValueAsClass(resultsProperties.nextToken().trim(),RESULTS_PROPERTIES);
            
            //testing the correctness of the values
            Interfaces.ensureImplementedInterfaceOrClass(factory,   Interfaces.RESULTS_FACTORY,   RESULTS_FACTORY);
            Interfaces.ensureImplementedInterfaceOrClass(edge,      Interfaces.RESULTS_EDGE,      RESULTS_EDGE);
            Interfaces.ensureImplementedInterfaceOrClass(constraint,Interfaces.RESULTS_CONSTRAINT,RESULTS_CONSTRAINT);
            Interfaces.ensureImplementedInterfaceOrClass(generator, Interfaces.RESULTS_GENERATOR, RESULTS_GENERATOR);
            Interfaces.ensureImplementedInterfaceOrClass(prop,      Interfaces.RESULTS_PROPERTIES,RESULTS_PROPERTIES);
            
            //add references
            Properties.resultsFactory.add(factory);
            Properties.resultsEdge.add(edge);
            Properties.resultsConstraint.add(constraint);
            Properties.resultsGenerator.add(generator);
            
            Properties.resultsProperties.add(prop);
            Properties.resultsPropertiesInstance.add(loadPropertiesInitializer(prop));
        }
    }
    
    /**
     * Gets related index of the requested results type.
     * @param resultsName Results name.
     * @return The related index of the requested results type.
     * @throws InitializationException if some error occurs during
     * the process.
     */
    private static int getIndexOf(String resultsName) throws InitializationException
    {
        //testing if the results has been activated.
        if (!activatedResults)
            throw new InitializationException("The optional results part is not activated.");

        //testing if has a correct value
        if (resultsName == null)
            throw new InitializationException("The specified resultsName is null.");
        
        //testing if the requested results type exists
        Object value = Properties.resultsUniqueName.get(resultsName);
        if (value == null)
            throw new InitializationException("The results name '"+resultsName+"' don't appear into the loaded configuration.");

        //obtaining the requested index
        return ((Integer)value).intValue();
    }
    
    /**
     * Gets the ResultsFactory related to the <b>resultsname</b>.
     * @param resultsName Results name, appeared in the configuration file.
     * @return The requrested ResultsFactory.
     * @throws InitializationException if some error occurs during 
     * the obtaining process.
     */
    public static Class getResultsFactory(String resultsName) throws InitializationException
    {
        //obtaining the requested factory
        try {
            return (Class)Properties.resultsFactory.get(getIndexOf(resultsName));
        } catch (Exception e)
        {
            throw new InitializationException("Cannot obtain the requested ResultsFactory of '"+resultsName+"' results",e);
        }
    }
    
    /**
     * Gets the ResultsConstraint related to the <b>resultsname</b>.
     * @param resultsName Results name, appeared in the configuration file.
     * @return The requrested ResultsConstraint.
     * @throws InitializationException if some error occurs during 
     * the obtaining process.
     */
    public static Class getResultsConstraint(String resultsName) throws InitializationException
    {
        //obtaining the requested constraint
        try {
            return (Class)Properties.resultsConstraint.get(getIndexOf(resultsName));
        } catch (Exception e)
        {
            throw new InitializationException("Cannot obtain the requested ResultsConstraint of '"+resultsName+"' results",e);
        }
    }
    
    /**
     * Gets the ResultsEdge related to the <b>resultsname</b>.
     * @param resultsName Results name, appeared in the configuration file.
     * @return The requrested ResultsEdge.
     * @throws InitializationException if some error occurs during 
     * the obtaining process.
     */
    public static Class getResultsEdge(String resultsName) throws InitializationException
    {
        //obtaining the requested edge
        try {
            return (Class)Properties.resultsEdge.get(getIndexOf(resultsName));
        } catch (Exception e)
        {
            throw new InitializationException("Cannot obtain the requested ResultsEdge of '"+resultsName+"' results",e);
        }
    }

    /**
     * Gets the ResultsGenerator related to the <b>resultsname</b>.
     * @param resultsName Results name, appeared in the configuration file.
     * @return The requrested ResultsGenerator.
     * @throws InitializationException if some error occurs during 
     * the obtaining process.
     */
    public static Class getResultsGenerator(String resultsName) throws InitializationException
    {
        //obtaining the requested generator
        try {
            return (Class)Properties.resultsGenerator.get(getIndexOf(resultsName));
        } catch (Exception e)
        {
            throw new InitializationException("Cannot obtain the requested ResultsGenerator of '"+resultsName+"' results",e);
        }
    }
    
    /**
     * Gets the properties class related to the <b>resultsname</b>.
     * @param resultsName Results name, appeared in the configuration file.
     * @return The requested properties.
     * @throws InitializationException if some error occurs during 
     * the obtaining process.
     */
    public static Class getResultsProperties(String resultsName) throws InitializationException
    {
        //obtaining the requested properties class
        try {
            return (Class)Properties.resultsProperties.get(getIndexOf(resultsName));
        } catch (Exception e)
        {
            throw new InitializationException("Cannot obtain the requested properties class of '"+resultsName+"' results",e);
        }
    }
    
    /**
     * Gets the properties instance related to the <b>resultsname</b>.
     * @param resultsName Results name, appeared in the configuration file.
     * @return The requested properties instance.
     * @throws InitializationException if some error occurs during 
     * the obtaining process.
     */
    public static PropertiesInitializer getResultsPropertiesInstance(String resultsName) throws InitializationException
    {
        //obtaining the requested properties instance
        try {
            return (PropertiesInitializer)Properties.resultsPropertiesInstance.get(getIndexOf(resultsName));
        } catch (Exception e)
        {
            throw new InitializationException("Cannot obtain the requested properties class of '"+resultsName+"' results",e);
        }
    }

    /**
     * Shows when the optional application level has been activated.
     * @return true when it is activated. false in other case.
     */
    public static boolean isApplicationLevelActivated()
    { 
        return activatedApplicationLevel;
    }
    
    /**
     * Shows when the optional events part has been activated.
     * @return true when it is activated. false in other case.
     */
    public static boolean isEventsActivated()
    {
        return activatedEvents;
    }

    /**
     * Shows when the optional serialization has been activated.
     * @return true when it is activated. false in other case.
     */
    public static boolean isSerializationActivated()
    {
        return activatedSerialization;
    }
    
    /**
     * Shows when the optional results part has been activated.
     * @return true when it is activated. false in other case.
     */
    public static boolean isResultsActivated()
    {
        return activatedResults;
    }
}
