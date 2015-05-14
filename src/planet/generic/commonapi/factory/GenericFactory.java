package planet.generic.commonapi.factory;

import java.util.Iterator;
import java.util.TreeMap;

import planet.commonapi.Application;
import planet.commonapi.EndPoint;
import planet.commonapi.Id;
import planet.commonapi.Message;
import planet.commonapi.Network;
import planet.commonapi.Node;
import planet.commonapi.NodeHandle;
import planet.commonapi.RouteMessage;
import planet.commonapi.behaviours.BehavioursFactory;
import planet.commonapi.behaviours.BehavioursFilter;
import planet.commonapi.behaviours.BehavioursInvoker;
import planet.commonapi.behaviours.BehavioursPattern;
import planet.commonapi.behaviours.BehavioursPool;
import planet.commonapi.behaviours.BehavioursRoleSelector;
import planet.commonapi.behaviours.exception.NoBehaviourDispatchedException;
import planet.commonapi.behaviours.exception.NoSuchBehaviourException;
import planet.commonapi.exception.InitializationException;
import planet.commonapi.factory.ApplicationFactory;
import planet.commonapi.factory.EndPointFactory;
import planet.commonapi.factory.IdFactory;
import planet.commonapi.factory.NetworkFactory;
import planet.commonapi.factory.NodeFactory;
import planet.commonapi.factory.NodeHandleFactory;
import planet.commonapi.factory.RouteMessagePool;
import planet.commonapi.results.ResultsConstraint;
import planet.commonapi.results.ResultsEdge;
import planet.commonapi.results.ResultsFactory;
import planet.commonapi.results.ResultsGenerator;
import planet.simulate.Logger;
import planet.util.KeyGen;
import planet.util.Properties;

/**
 * It is an abstraction class that follows the Factory Method pattern. It
 * offers different static methods to obtain differents instances of factories
 * and their related objects. 
 * <br><br>
 * From anywhere of this simulator one can use these methods to obtain the
 * required instance, using the current configuration attributes (using
 * loaded properties from planet.util.Properties).
 * 
 * @author <a href="mailto: jordi.pujol@estudiants.urv.es">Jordi Pujol</a>
 * 07-jul-2005
 */
public class GenericFactory {
    
    /**
     * The NetworkFactory that is used to invoke the 
     * NetworkFactory methods.
     */
    private static NetworkFactory factoryNetworkFactory = null;
    /**
     * The IdFactory that is used to invoke the 
     * IdFactory methods.
     */
    private static IdFactory factoryIdFactory = null;
    /**
     * The NodeHandleFactory that is used to invoke the nodehandle factory
     * methods.
     */
    private static NodeHandleFactory factoryNodeHandleFactory = null;
    /**
     * The NodeFactory that is used to invoke the node factory methods.
     */
    private static NodeFactory factoryNodeFactory = null;   
    /**
     * The ApplicationFactory that is used to invoke the application factory
     * methods.
     */
    private static ApplicationFactory factoryApplicationFactory = null;
    /**
     * The EndPointFactory that is used to invoke the endpoint factory methods.
     */
    private static EndPointFactory factoryEndPointFactory = null;
    /**
     * The BehavioursFactory that is used to invoke the behaviours factory methods.
     */
    private static BehavioursFactory behavioursFactory = null;
    /**
     * The BehavioursPool that is used to invoke the behaviours pool methods.
     */
    private static BehavioursPool behavioursPool = null;
    /**
     * The RouteMessagePool that is used to invoke the pool methods.
     */
    private static RouteMessagePool routeMessagePool = null;
    /**
     * The ResultsFactory instances for any results type loaded.
     */
    private static TreeMap resultsFactory = null;
    /**
     * The ResultsGenerator instances for any results type loaded.
     */
    private static TreeMap resultsGenerator = null;
    /**
     * Permits to generate an unique String key for RouteMessages that can send
     * any Node.
     */
    private static KeyGen keyGen = null;

    
    /**
     * Builds an instance of the specified class. Its internal behaviour
     * is the following:
     * <pre>
     *    return classReference.newInstance();
     * </pre>
     * @param classReference The class reference to use to build the new instance.
     * @return A new instance of the specified class.
     * @throws InitializationException if any error occurs during the 
     * initialization process, or when the <b>classReference</b> has a null value.
     */
    public static Object newInstance(Class classReference) throws InitializationException
    {
        if (classReference == null)
            throw new InitializationException("Using null class reference to build a new instance.");
        try {
            return classReference.newInstance();
        } catch (Exception e)
        {
            throw new InitializationException("Cannot build a new instance of '"+
                    classReference.getName()+"'.",e);
        }
    }
    
    /**
     * Initialize the GenericFactory to load the default properties file. This
     * way permits return the different factories with the same base properties:
     * the factories obtained with the methods withot parameters returns the
     * same class of instances.
     * 
     * @throws InitializationException
     *             if occurs any problem during initialization.
     */
    public static void init() throws InitializationException {
        //Initialization process
        
        //---> Pre-initialization (don't comment!!)
        preinit();
        
        //---> Updating data into factories
        update();
        
        if (Properties.isApplicationLevelActivated())
        {
            factoryApplicationFactory = buildApplicationFactory();
            factoryEndPointFactory    = buildEndPointFactory();
        }
        
        //---> Behaviours
        if (Properties.overlayWithBehaviours)
        {
            behavioursFactory         = buildBehavioursFactory();
        }

        //---> Results
        if (Properties.isResultsActivated())
        {
            resultsFactory            = new TreeMap();
            resultsGenerator          = new TreeMap();
            Iterator it = Properties.resultsUniqueName.keySet().iterator();
            String resultsName = null;
            ResultsFactory factory = null;
            while (it.hasNext())
            {
                resultsName = (String)it.next();
                factory = buildResultsFactory(resultsName);
                resultsFactory.put(resultsName,factory);
                resultsGenerator.put(resultsName,factory.buildGenerator());
            }
        }
        
        //---> others
        keyGen                    = new KeyGen(0, Integer.MAX_VALUE);
        Logger.setLevel(Properties.simulatorLogLevel);
    }

    /**
     * Updates all main factories with the required but uninitialized instance.
     * @throws InitializationException if occurs some error during the
     * initialization.
     */
    private static void preinit() throws InitializationException 
    {
        factoryNetworkFactory     = (NetworkFactory)newInstance(Properties.factoriesNetworkFactory);
        factoryIdFactory          = (IdFactory)newInstance(Properties.factoriesIdFactory);
        factoryNodeHandleFactory  = (NodeHandleFactory)newInstance(Properties.factoriesNodeHandleFactory);
        factoryNodeFactory        = (NodeFactory)newInstance(Properties.factoriesNodeFactory);
        routeMessagePool          = (RouteMessagePool)newInstance(Properties.factoriesRouteMessagePool);
    }

    /**
     * Updates the internal attributes with the required parameters for
     * fully functional factories.
     * @throws InitializationException if some error occurs during the
     * initialization.
     */
    private static void update() throws InitializationException
    {
        factoryNetworkFactory.setValues(Properties.factoriesNetwork,Properties.factoriesNetworkSize, 
                factoryNodeFactory,Properties.factoriesNetworkTopology);
        factoryIdFactory.setValues(Properties.overlayId,Properties.factoriesNetworkTopology,
                Properties.factoriesNetworkSize);
        factoryNodeHandleFactory.setValues(Properties.factoriesNodeHandle);
        factoryNodeFactory.setValues(factoryIdFactory,Properties.overlayNode);
    }

    /**
     * Make the post initializations to leave the GenericFactory complete.
     * @throws InitializationException if some error occurs during the
     * initialization.
     */
    public static void postinit() throws InitializationException
    {
        //---> Behaviours
        if (Properties.overlayWithBehaviours)
        {
            behavioursPool            = buildBehavioursPool();
        }

    }
    
    
    /* ************* NETWORK RELATED METHODS **********************************/
    
    /**
     * Builds a new instance of NetworkFactory.
     * 
     * @return A new instance of the NetworkFactory default implementation.
     * @throws InitializationException
     *             if occurs some problem with the factory initialization.
     */
    public static NetworkFactory buildNetworkFactory()
            throws InitializationException {
        return ((NetworkFactory)newInstance(Properties.factoriesNetworkFactory))
            .setValues(Properties.factoriesNetwork,Properties.factoriesNetworkSize, 
                    factoryNodeFactory,Properties.factoriesNetworkTopology);
    }
    
    /**
     * @see planet.commonapi.factory.NetworkFactory#buildNetwork()
     */
    public static Network buildNetwork() throws InitializationException {
        return factoryNetworkFactory.buildNetwork();
    }

    /**
     * @see planet.commonapi.factory.NetworkFactory#buildNetwork(int)
     */
    public static Network buildNetwork(int size) throws InitializationException {
        return factoryNetworkFactory.buildNetwork(size);
    }

    /**
     * @see planet.commonapi.factory.NetworkFactory#buildNetwork(int,
     *      planet.commonapi.factory.NodeFactory)
     */
    public static Network buildNetwork(int size, NodeFactory nodeFactory)
            throws InitializationException {
        return factoryNetworkFactory.buildNetwork(size, nodeFactory);
    }

    /**
     * @see planet.commonapi.factory.NetworkFactory#buildNetwork(int,
     *      java.lang.String)
     */
    public static Network buildNetwork(int size, String topology)
            throws InitializationException {
        return factoryNetworkFactory.buildNetwork(size, topology);
    }

    /**
     * @see planet.commonapi.factory.NetworkFactory#buildNetwork(int,
     *      planet.commonapi.factory.NodeFactory, java.lang.String)
     */
    public static Network buildNetwork(int size, NodeFactory nodeFactory,
            String topology) throws InitializationException {
        return factoryNetworkFactory.buildNetwork(size, nodeFactory, topology);
    }
    
    /* ************************* ID RELATED METHODS ***************************/
    
    /**
     * Builds a new instance of IdFactory.
     * @return A new instance of the default IdFactory implementation.
     * @throws InitializationException if occurs some problem with the 
     * factory initialization.
     */
    public static IdFactory buildIdFactory()
            throws InitializationException {
        return ((IdFactory)newInstance(Properties.factoriesIdFactory))
            .setValues(Properties.overlayId,Properties.factoriesNetworkTopology,
                    Properties.factoriesNetworkSize);
    }
    
    /**
     * @see planet.commonapi.factory.IdFactory#buildId()
     */
    public static Id buildId() throws InitializationException {
        return factoryIdFactory.buildId();
    }

    /**
     * @see planet.commonapi.factory.IdFactory#buildId(int)
     */
    public static Id buildId(int material) throws InitializationException {
        return factoryIdFactory.buildId(material);
    }

    /**
     * @see planet.commonapi.factory.IdFactory#buildId(double)
     */
    public static Id buildId(double material) throws InitializationException {
        return factoryIdFactory.buildId(material);
    }

    /**
     * @see planet.commonapi.factory.IdFactory#buildId(byte[])
     */
    public static Id buildId(byte[] material) throws InitializationException {
        return factoryIdFactory.buildId(material);
    }

    /**
     * @see planet.commonapi.factory.IdFactory#buildId(int[])
     */
    public static Id buildId(int[] material) throws InitializationException {
        return factoryIdFactory.buildId(material);
    }

    /**
     * @see planet.commonapi.factory.IdFactory#buildKey(java.lang.String)
     */
    public static Id buildKey(String string) throws InitializationException {
        return factoryIdFactory.buildKey(string);
    }

    /**
     * @see planet.commonapi.factory.IdFactory#buildId(java.lang.String)
     */
    public static Id buildId(String string) throws InitializationException {
        return factoryIdFactory.buildId(string);
    }

    /**
     * @see planet.commonapi.factory.IdFactory#buildId(java.math.BigInteger)
     */
    public static Id buildId(java.math.BigInteger bigNumber)
            throws InitializationException {
        return factoryIdFactory.buildId(bigNumber);
    }

    /**
     * @see planet.commonapi.factory.IdFactory#buildId(java.lang.String,java.lang.String)
     * @see <a href="http://java.sun.com/j2se/1.4.2/docs/guide/security/CryptoSpec.html#AppA">
     *      Java Cryptography Architecture API Specification & Reference </a>
     */
    public static Id buildId(String material, String algorithm)
            throws InitializationException {
        return factoryIdFactory.buildId(material, algorithm);
    }

    /**
     * @see planet.commonapi.factory.IdFactory#buildRandomId()
     */
    public static Id buildRandomId() throws InitializationException {
        return factoryIdFactory.buildRandomId();
    }

    /**
     * @see planet.commonapi.factory.IdFactory#buildDistributedIds(int)
     */
    public static java.util.Iterator buildDistributedIds(int desiredNetworkSize)
            throws InitializationException {
        return factoryIdFactory.buildDistributedIds(desiredNetworkSize);
    }
    
    /* ************* NODEHANDLE RELATED METHODS ******************************/
    
    /**
     * Builds a new instance of NodeHandleFactory following the current
     * configuration.
     * @return A new instance of NodeHandleFactory
     * @throws InitializationException if some error occurs during
     * the initialization process.
     */
    public static NodeHandleFactory buildNodeHandleFactory() throws InitializationException
    {
        return ((NodeHandleFactory)newInstance(Properties.factoriesNodeHandleFactory)).
                setValues(Properties.factoriesNodeHandle);
    }
    
    /**
     * @see planet.commonapi.factory.NodeHandleFactory#buildNodeHandle(planet.commonapi.Id,boolean)
     */
    public static NodeHandle buildNodeHandle(Id nodeId, boolean alive)
            throws InitializationException {
        return factoryNodeHandleFactory.buildNodeHandle(nodeId, alive);
    }
    
    /* *********************** NODE RELATED METHODS **************************/
    
    /**
     * Builds a new instance of NodeFactory following the current
     * configuration.
     * @return A new instance of NodeFactory.
     * @throws InitializationException if some error occurs during
     * the initialization process.
     */
    public static NodeFactory buildNodeFactory() throws InitializationException
    {
        return ((NodeFactory)newInstance(Properties.factoriesNodeFactory)).
            setValues(factoryIdFactory,Properties.overlayNode);
    }
    
    /**
     * @see planet.commonapi.factory.NodeFactory#buildNode()
     */
    public static Node buildNode() throws InitializationException {
        return factoryNodeFactory.buildNode();
    }

    /**
     * @see planet.commonapi.factory.NodeFactory#buildNode(planet.commonapi.Id)
     */
    public static Node buildNode(Id id) throws InitializationException {
        return factoryNodeFactory.buildNode(id);
    }

    /* ************** ROUTEMESSAGE RELATED METHODS ****************************/
    
    /**
     * Builds an instance of the current RouteMessagePool implementation.
     * @return An instance of the current RouteMessagePool implementation.
     * @throws InitializationException if any errors occurs during the
     * initialization process.
     */
    public static RouteMessagePool buildRouteMessagePool() throws InitializationException
    {
        return (RouteMessagePool)newInstance(Properties.factoriesRouteMessagePool);
    }
    
    /**
     * @see planet.commonapi.factory.RouteMessagePool#freeMessage(planet.commonapi.RouteMessage)
     */
    public static void freeMessage(RouteMessage msg) {
        routeMessagePool.freeMessage(msg);
    }
    /**
     * @see planet.commonapi.factory.RouteMessagePool#getMessage(java.lang.String, planet.commonapi.NodeHandle, planet.commonapi.NodeHandle, int, int)
     */
    public static RouteMessage getMessage(String key, NodeHandle from, NodeHandle to,
            int type, int mode) throws InitializationException {
        return routeMessagePool.getMessage(key,from,to,type,mode);
    }
    /**
     * @see planet.commonapi.factory.RouteMessagePool#getMessage(String, NodeHandle, NodeHandle, NodeHandle, int, int, Message, String)
     */
    public static RouteMessage getMessage(String key, NodeHandle from, NodeHandle to,
            NodeHandle nextHop, Message msg, int type, int mode, String appId)
            throws InitializationException {
        return routeMessagePool.getMessage(key,from,to,nextHop,type,mode,msg,appId);
    }
    
    /**
     * @see planet.commonapi.factory.RouteMessagePool#getBuiltRouteMessages()
     */
    public static int getBuiltRouteMessages() {
        return routeMessagePool.getBuiltRouteMessages();
    }
    /**
     * @see planet.commonapi.factory.RouteMessagePool#getFreeRouteMessages()
     */
    public static int getFreeRouteMessages() {
        return routeMessagePool.getFreeRouteMessages();
    }
    /**
     * @see planet.commonapi.factory.RouteMessagePool#getReusedRouteMessages()
     */
    public static int getReusedRouteMessages() {
        return routeMessagePool.getReusedRouteMessages();
    }
    
    /* ************* APPLICATION RELATED METHODS ******************************/

    /**
     * Test if the application level part is activated.
     * @throws InitializationException only if the application level part is 
     * not activated.
     */
    private static void ensureActivatedApplicationLevel() throws InitializationException
    {
        if (!Properties.isApplicationLevelActivated())
            throw new InitializationException("The current configuration has not the application level activated.");
    }

    
    /**
     * Builds a new instance of ApplicationFactory following the current
     * configuration.
     * @return A new instance of ApplicationFactory.
     * @throws InitializationException if some error occurs during
     * the initialization process.
     */
    public static ApplicationFactory buildApplicationFactory() throws InitializationException
    {
        ensureActivatedApplicationLevel();
        return ((ApplicationFactory)GenericFactory.newInstance(Properties.factoriesApplicationFactory)).
            setValues(Properties.factoriesApplication);
    }
    
    /**
     * @see planet.commonapi.factory.ApplicationFactory#buildApplication()
     */
    public static Application buildApplication() throws InitializationException {
        ensureActivatedApplicationLevel();
        return factoryApplicationFactory.buildApplication();
    }

    /**
     * @see planet.commonapi.factory.ApplicationFactory#buildApplication(java.lang.String)
     */
    public static Application buildApplication(String app) throws InitializationException {
        ensureActivatedApplicationLevel();
        return factoryApplicationFactory.buildApplication(app);
    }

    /**
     * @see planet.commonapi.factory.ApplicationFactory#buildApplicationWithName(java.lang.String)
     */
    public static Application buildApplicationWithName(String name)
            throws InitializationException {
        ensureActivatedApplicationLevel();
        return factoryApplicationFactory.buildApplicationWithName(name);
    }

    /**
     * @see planet.commonapi.factory.ApplicationFactory#buildApplicationWithName(java.lang.String,
     *      java.lang.String)
     */
    public static Application buildApplicationWithName(String app, String name)
            throws InitializationException {
        ensureActivatedApplicationLevel();
        return factoryApplicationFactory.buildApplicationWithName(app, name);
    }
    
    /* ********************** ENDPOINT RELATED METHODS ************************/
    
    /**
     * Builds a new instance of EndPointFactory following the current
     * configuration.
     * @return A new instance of EndPointFactory.
     * @throws InitializationException if some error occurs during
     * the initialization process.
     */
    public static EndPointFactory buildEndPointFactory() throws InitializationException
    {
        ensureActivatedApplicationLevel();
        return ((EndPointFactory)GenericFactory.newInstance(Properties.factoriesEndPointFactory)).
            setValues(Properties.factoriesEndPoint);
    }

    /**
     * @see planet.commonapi.factory.EndPointFactory#buildEndPoint(planet.commonapi.Application,planet.commonapi.Node)
     */
    public static EndPoint buildEndPoint(Application app, Node node)
            throws InitializationException {
        ensureActivatedApplicationLevel();
        return factoryEndPointFactory.buildEndPoint(app, node);
    }
    
    /* ************************** BEHAVIOURS RELATED METHODS *****************/
    
    /**
     * Test if the behaviours part is activated (if and only if the overlay
     * use behaviours).
     * @throws InitializationException only if the behaviours part is not activated.
     */
    private static void ensureActivatedBehaviours() throws InitializationException
    {
        if (!Properties.overlayWithBehaviours)
            throw new InitializationException("The current overlay doesn't use behaviours.");
    }
    
    /**
     * Builds an instance of the current BehavioursFactory implementation.
     * @return An instance of the current BehavioursFactory implementation.
     * @throws InitializationException when an error occurs during the 
     * initialization or when this factory method is nonapplicable, because
     * the current overlay doesn't use behaviours.
     */
    public static BehavioursFactory buildBehavioursFactory()
            throws InitializationException {
        ensureActivatedBehaviours();
        return ((BehavioursFactory)GenericFactory.newInstance(Properties.behavioursFactory)).
            setValues(Properties.behavioursPool,Properties.behavioursFilter, 
                    Properties.behavioursInvoker, Properties.behavioursPattern, 
                    Properties.behavioursRoleSelector);
    }
    
    /**
     * @see planet.commonapi.behaviours.BehavioursFactory#buildBehavioursFilter()
     */
    public static BehavioursFilter buildBehavioursFilter()
            throws InitializationException {
        ensureActivatedBehaviours();
        return behavioursFactory.buildBehavioursFilter();
    }
    /**
     * @see planet.commonapi.behaviours.BehavioursFactory#buildBehavioursInvoker()
     */
    public static BehavioursInvoker buildBehavioursInvoker()
            throws InitializationException {
        ensureActivatedBehaviours();
        return behavioursFactory.buildBehavioursInvoker();
    }
    /**
     * @see planet.commonapi.behaviours.BehavioursFactory#buildBehavioursPattern()
     */
    public static BehavioursPattern buildBehavioursPattern()
            throws InitializationException {
        ensureActivatedBehaviours();
        return behavioursFactory.buildBehavioursPattern();
    }
    /**
     * @see planet.commonapi.behaviours.BehavioursFactory#buildBehavioursPool()
     */
    public static BehavioursPool buildBehavioursPool() throws InitializationException {
        ensureActivatedBehaviours();
        return behavioursFactory.buildBehavioursPool();
    }
    
    /**
     * @see planet.commonapi.behaviours.BehavioursFactory#buildBehavioursRoleSelector()
     */
    public static BehavioursRoleSelector buildBehavioursRoleSelector()
            throws InitializationException {
        ensureActivatedBehaviours();
        return behavioursFactory.buildBehavioursRoleSelector();
    }
    
    /**
     * @see planet.commonapi.behaviours.BehavioursPool#onMessage(planet.commonapi.RouteMessage, planet.commonapi.Node)()
     */
    public void onMessage(RouteMessage msg, Node node) throws NoSuchBehaviourException,NoBehaviourDispatchedException, InitializationException
    {
        ensureActivatedBehaviours();
        behavioursPool.onMessage(msg,node);
    }
    
    /* *************** RESULTS RELATED METHODS ********************************/
    
    /**
     * Test if the results part is activated.
     * @throws InitializationException only if the results part is not activated.
     */
    private static void ensureActivatedResults() throws InitializationException
    {
        if (!Properties.isResultsActivated())
            throw new InitializationException("The current configuration has not the activated results.");
    }
    
    /**
     * Builds an instance of the current BehavioursFactory implementation.
     * @return An instance of the current BehavioursFactory implementation.
     * @throws InitializationException when an error occurs during the 
     * initialization or when this factory method is nonapplicable, because
     * the no results are activated, or when the <b>resultsName</b> doesn't
     * appears in the current configuration.
     */
    public static ResultsFactory buildResultsFactory(String resultName)
            throws InitializationException {
        ensureActivatedResults();
        return ((ResultsFactory)GenericFactory.newInstance(Properties.getResultsFactory(resultName))).
            setValues(  Properties.getResultsEdge(resultName),
                        Properties.getResultsConstraint(resultName),
                        Properties.getResultsGenerator(resultName));
    }

    /**
     * @param resultsName Results type name to use.
     * @throws InitializationException when an error occurs during the 
     * initialization or when this factory method is nonapplicable, because
     * the no results are activated, or when the <b>resultsName</b> doesn't
     * appears in the current configuration.
     * @see planet.commonapi.results.ResultsGenerator#generateResults(planet.commonapi.Network, java.lang.String, planet.commonapi.results.ResultsConstraint, boolean)
     */
    public static void generateResults(String resultsName, Network network, String out, ResultsConstraint constraint, boolean wholeNetworkLayout) throws InitializationException {
        ensureActivatedResults();
        ResultsGenerator gen = (ResultsGenerator)resultsGenerator.get(resultsName);
        if (gen == null)
            throw new InitializationException("The results type '"+resultsName+"' doesn't appears in the current configuration.");
        gen.generateResults(network,out,constraint,wholeNetworkLayout);
    }
    
    /**
     * @param resultsName Results type name to use.
     * @throws InitializationException when an error occurs during the 
     * initialization or when this factory method is nonapplicable, because
     * the no results are activated, or when the <b>resultsName</b> doesn't
     * appears in the current configuration.
     * @see planet.commonapi.results.ResultsFactory#buildEdge(planet.commonapi.Id, planet.commonapi.Id, boolean, java.lang.String)
     */
    public static ResultsEdge buildEdge(String resultsName, Id source, Id target, boolean directed, String fill) throws InitializationException {
        ensureActivatedResults();
        ResultsFactory fact = (ResultsFactory)resultsFactory.get(resultsName);
        if (fact == null)
            throw new InitializationException("The results type '"+resultsName+"' doesn't appears in the current configuration.");
        return fact.buildEdge(source,target,directed,fill);
    }

    /**
     * @param resultsName Results type name to use.
     * @throws InitializationException when an error occurs during the 
     * initialization or when this factory method is nonapplicable, because
     * the no results are activated, or when the <b>resultsName</b> doesn't
     * appears in the current configuration.
     * @see planet.commonapi.results.ResultsFactory#buildGenerator()
     */
    public static ResultsGenerator buildGenerator(String resultsName) throws InitializationException {
        ensureActivatedResults();
        ResultsFactory fact = (ResultsFactory)resultsFactory.get(resultsName);
        if (fact == null)
            throw new InitializationException("The results type '"+resultsName+"' doesn't appears in the current configuration.");
        return fact.buildGenerator();
    }

    /**
     * @param resultsName Results type name to use.
     * @throws InitializationException when an error occurs during the 
     * initialization or when this factory method is nonapplicable, because
     * the no results are activated, or when the <b>resultsName</b> doesn't
     * appears in the current configuration.
     * @see planet.commonapi.results.ResultsFactory#buildConstraint()
     */
    public static ResultsConstraint buildConstraint(String resultsName) throws InitializationException {
        ensureActivatedResults();
        ResultsFactory fact = (ResultsFactory)resultsFactory.get(resultsName);
        if (fact == null)
            throw new InitializationException("The results type '"+resultsName+"' doesn't appears in the current configuration.");
        return fact.buildConstraint();
    }    

    /* ****************** MESSAGE KEY RELATED METHODS   ********************* */
    /**
     * Generate a unique key, based on a simple int. Based on the context of the
     * simulator, its implementation ensures that the returned key is unique at
     * the time that it is generated. This key is used to identify a unique
     * RouteMessage.
     * @return An unique key for a RouteMessage to be send.
     */
    public static String generateKey() {
        return keyGen.generateKey();
    }
    
    /* ********************* RETRIEVING DEFAULT INSTANCES *********************/
    
    /**
     * Returns the NetworkFactory that is used internally to invoke the 
     * NetworkFactory methods.
     * @return The NetworkFactory that is used internally to invoke 
     * the current NetworkFactory methods.
     * @throws InitializationException if some error occurs during the retrieving.
     */
    public static NetworkFactory getDefaultNetworkFactory() throws InitializationException {
        return factoryNetworkFactory;
    }
    /**
     * Returns the IdFactory that is used internally to invoke the 
     * IdFactory methods.
     * @return The IdFactory that is used internally to invoke the 
     * IdFactory methods.
     * @throws InitializationException if some error occurs during the retrieving.
     */
    public static IdFactory getDefaultIdFactory() throws InitializationException {
        return factoryIdFactory;
    }

    /**
     * Returns the NodeHandleFactory that is used internally to invoke the 
     * nodehandle factory methods.
     * @return The NodeHandleFactory that is used internally to invoke the 
     * nodehandle factory methods.
     * @throws InitializationException if some error occurs during the retrieving.
     */
    public static NodeHandleFactory getDefaultNodeHandleFactory() throws InitializationException
    {
        return factoryNodeHandleFactory;
    }

    /**
     * Returns the NodeFactory that is used internally to invoke the node 
     * factory methods.
     * @return The NodeFactory that is used internally to invoke the node 
     * factory methods.
     * @throws InitializationException if some error occurs during the retrieving.
     */
    public static NodeFactory getDefaultNodeFactory() throws InitializationException
    {
        return factoryNodeFactory;
    }
    /**
     * Returns the ApplicationFactory that is used internally to invoke the 
     * application factory methods.
     * @return The ApplicationFactory that is used internally to invoke the 
     * application factory methods. 
     * @throws InitializationException if some error occurs during the retrieving.
     */
    public static ApplicationFactory getDefaultApplicationFactory() throws InitializationException
    {
        return factoryApplicationFactory;
    }

    /**
     * Return the EndPointFactory that is used internally to invoke the 
     * endpoint factory methods.
     * @return The EndPointFactory that is used internally to invoke the 
     * endpoint factory methods.
     * @throws InitializationException if some error occurs during the retrieving.
     */
    public static EndPointFactory getDefaultEndPointFactory() throws InitializationException
    {
        return factoryEndPointFactory;
    }
    /**
     * Return the BehavioursFactory that is used internally to invoke the 
     * behaviours factory methods.
     * @return The BehavioursFactory that is used internally to invoke the 
     * behaviours factory methods.
     * @throws InitializationException if some error occurs during the retrieving.
     */
    public static BehavioursFactory getDefaultBehavioursFactory() throws InitializationException
    {
        return behavioursFactory;
    }
    /**
     * Return the BehavioursPool that is used internally to invoke the 
     * behaviours pool methods.
     * @return The BehavioursPool that is used internally to invoke the 
     * behaviours pool methods.
     * @throws InitializationException if some error occurs during the retrieving.
     */
    public static BehavioursPool getDefaultBehavioursPool() throws InitializationException
    {
        return behavioursPool;
    }
    /**
     * Returns the RouteMessagePool that is used internally to invoke the pool 
     * methods.
     * @return The RouteMessagePool that is used internally to invoke the pool 
     * methods.
     * @throws InitializationException if some error occurs during the retrieving.
     */
    public static RouteMessagePool getDefaultRouteMessagePool() throws InitializationException
    {
        return routeMessagePool;
    }
}