package planet.generic.commonapi;

import planet.commonapi.Network;
import planet.commonapi.exception.InitializationException;
import planet.generic.commonapi.factory.GenericFactory;
import planet.util.Properties;

/**
 * This class only attemps to help to programmers make more simple the initialization of the 
 * simulator context. There are two ways to initialize the context:
 * <br><br><b>One</b><br>
 * The new main application has to extend this GenericApp and includes in theirs constructors the
 * sentence <b>super(...)</b> with the desired arguments.
 * For example:
 * <b><pre>
 * ....
 * 
 * public NewTest() throws InitializationException {
 *     super("../conf/master2.properties","TEST_ONE",false,false,false,false);
 * 
 * ....
 * </pre></b>
 * See the constructor description for more details on the arguments.
 * <br><br><b>Two</b><br>
 * To include at first these lines (before any other simulator related statement):
 * <b><pre>
 *   //FIRST: complete Properties initialization
 *   Properties.init(masterPropertiesFile, masterPropertyName);
 *   if (activateApplicationLevel)
 *       Properties.activateApplicationLevelAttributes();
 *   if (activateEvents)
 *       Properties.activateEventsAttributes();
 *   if (activateResults)
 *       Properties.activateResultsAttributes();
 *   if (activateSerialization)
 *       Properties.activateSerializationAttributes();
 *
 *   //SECOND: GenericFactory initialization
 *   GenericFactory.init();
 * 
 *   //THIRD: making postinitialization
 *   Properties.postinit();
 *   GenericFactory.postinit();
 * </pre></b>
 * where the <b>masterPropertiesFile</b> is the main cofiguration file,
 * with the desired properties filename with the current configuration, and
 * <b>masterPropertyName</b> is the key to load the configuration and
 * runs the current test.
 * 
 * @author <a href="mailto: jordi.pujol@estudiants.urv.es">Jordi Pujol</a>
 * 08-jul-2005
 */
public class GenericApp {
	
	/**
	 * Make the complete initialization of the simulator context, according
     * to the arguments. In concrete:
	 * <b><pre>
     *   //FIRST: complete Properties initialization
     *   Properties.init(masterPropertiesFile);
     *   if (activateApplicationLevel)
     *       Properties.activateApplicationLevelAttributes();
     *   if (activateEvents)
     *       Properties.activateEventsAttributes();
     *   if (activateResults)
     *       Properties.activateResultsAttributes();
     *   if (activateSerialization)
     *       Properties.activateSerializationAttributes();
     *
     *   //SECOND: GenericFactory initialization
     *   GenericFactory.init();
     * 
     *   //THIRD: making postinitialization
     *   Properties.postinit();
     *   GenericFactory.postinit();
  	 * </pre></b>
  	 * Depending on the <b>masterPropertiesFile</b> specification, you must
     * select the correct directory to run the main application.
	 * 
     * @param masterPropertiesFile The intermediate file necessary to load 
     * the required configuration attributes to run the current test.
     * @param masterPropertyName The key that have to appear in the
     * <b>masterPropertiesFile</b> which identifies the last file with
     * current configuration.
     * @param activateApplicationLevel Flag that shows when to load
     * specific application level attributes for the current test.
     * @param activateEvents Flag that shows when to load 
     * specific events attributes for the current test.
     * @param activateResults Flag that shows when to load
     * specific results attributes for the current test.
     * @param activateSerialization Flag that shows when to load
     * specific serialization attributes for the current test.
	 * @throws InitializationException if any error has occurred during 
     * initialization of the simulator context.
	 */
	public GenericApp(String masterPropertiesFile, String masterPropertyName, 
            boolean activateApplicationLevel, boolean activateEvents, 
            boolean activateResults, boolean activateSerialization) throws InitializationException {

        start(masterPropertiesFile,masterPropertyName,
                activateApplicationLevel,activateEvents,
                activateResults,activateSerialization);
	}
    
    /**
     * Make the complete initialization of the simulator context, according
     * to the arguments. In concrete:
     * <b><pre>
     *   //FIRST: complete Properties initialization
     *   Properties.init("../conf/master.properties");
     *   if (activateApplicationLevel)
     *       Properties.activateApplicationLevelAttributes();
     *   if (activateEvents)
     *       Properties.activateEventsAttributes();
     *   if (activateResults)
     *       Properties.activateResultsAttributes();
     *   if (activateSerialization)
     *       Properties.activateSerializationAttributes();
     *
     *   //SECOND: GenericFactory initialization
     *   GenericFactory.init();
     * 
     *   //THIRD: making postinitialization
     *   Properties.postinit();
     *   GenericFactory.postinit();
     * </pre></b>
     * According to the master properties file path, you must
     * run the main application into the <b>bin</b> directory of this distribution.
     * 
     * @param masterPropertyName The key that have to appear in the
     * <b>masterPropertiesFile</b> which identifies the last file with
     * current configuration.
     * @param activateApplicationLevel Flag that shows when to load
     * specific application level attributes for the current test.
     * @param activateEvents Flag that shows when to load 
     * specific events attributes for the current test.
     * @param activateResults Flag that shows when to load
     * specific results attributes for the current test.
     * @param activateSerialization Flag that shows when to load
     * specific serialization attributes for the current test.
     * @throws InitializationException if any error has occurred during 
     * initialization of the simulator context.
     */
    public GenericApp(String masterPropertyName, 
            boolean activateApplicationLevel, boolean activateEvents, 
            boolean activateResults, boolean activateSerialization) throws InitializationException
    {
        start("../conf/master.properties",masterPropertyName,
                activateApplicationLevel, activateEvents,
                activateResults, activateSerialization);
    }
	
    /**
     * Make the complete initialization of the simulator context, according
     * to the arguments. In concrete:
     * <b><pre>
     *   //FIRST: complete Properties initialization
     *   Properties.init(masterPropertiesFile,masterPropertyName);
     *   if (activateApplicationLevel)
     *       Properties.activateApplicationLevelAttributes();
     *   if (activateEvents)
     *       Properties.activateEventsAttributes();
     *   if (activateResults)
     *       Properties.activateResultsAttributes();
     *   if (activateSerialization)
     *       Properties.activateSerializationAttributes();
     *
     *   //SECOND: GenericFactory initialization
     *   GenericFactory.init();
     *   
     *   //THIRD: making postinitialization
     *   Properties.postinit();
     *   GenericFactory.postinit();
     * </pre></b>
     * Depending on the <b>masterPropertiesFile</b> specification, you must
     * select the correct directory to run the main application.
     * 
     * @param masterPropertiesFile The intermediate file necessary to load 
     * the required configuration attributes to run the current test.
     * @param masterPropertyName The key that have to appear in the
     * <b>masterPropertiesFile</b> which identifies the last file with
     * current configuration.
     * @param activateApplicationLevel Flag that shows when to load
     * specific application level attributes for the current test.
     * @param activateEvents Flag that shows when to load 
     * specific events attributes for the current test.
     * @param activateResults Flag that shows when to load
     * specific results attributes for the current test.
     * @param activateSerialization Flag that shows when to load
     * specific serialization attributes for the current test.
     * @throws InitializationException if any error has occurred during 
     * initialization of the simulator context.
     */
    public static void start(String masterPropertiesFile, String masterPropertyName, 
            boolean activateApplicationLevel, boolean activateEvents, 
            boolean activateResults, boolean activateSerialization) throws InitializationException {
        //FIRST: complete Properties initialization
        Properties.init(masterPropertiesFile, masterPropertyName);
        if (activateApplicationLevel)
            Properties.activateApplicationLevelAttributes();
        if (activateEvents)
            Properties.activateEventsAttributes();
        if (activateResults)
            Properties.activateResultsAttributes();
        if (activateSerialization)
            Properties.activateSerializationAttributes();

        //SECOND: GenericFactory initialization
        GenericFactory.init();
        
        //THIRD: making postinitialization
        Properties.postinit();
        GenericFactory.postinit();
    }
    
    /**
     * Make an update of the current simulator context using the actual
     * configuration into the <b>planet.util.Properties</b>. In concrete:
     * <b><pre>
     *   if (activateApplicationLevel)
     *       Properties.activateApplicationLevelAttributes();
     *   if (activateEvents)
     *       Properties.activateEventsAttributes();
     *   if (activateResults)
     *       Properties.activateResultsAttributes();
     *   if (activateSerialization)
     *       Properties.activateSerializationAttributes();
     *   GenericFactory.init();
     *   Properties.postinit();
     *   GenericFactory.postinit();
     * </pre></b>
     * 
     * @param activateApplicationLevel Flag that shows when to load
     * specific application level attributes for the current test.
     * @param activateEvents Flag that shows when to load 
     * specific events attributes for the current test.
     * @param activateResults Flag that shows when to load
     * specific results attributes for the current test.
     * @param activateSerialization Flag that shows when to load
     * specific serialization attributes for the current test.
     * @throws InitializationException if any error has occurred during 
     * initialization of the simulator context.
     */
    public static void restart(boolean activateApplicationLevel, boolean activateEvents, 
            boolean activateResults, boolean activateSerialization) throws InitializationException {
        if (activateApplicationLevel)
            Properties.activateApplicationLevelAttributes();
        if (activateEvents)
            Properties.activateEventsAttributes();
        if (activateResults)
            Properties.activateResultsAttributes();
        if (activateSerialization)
            Properties.activateSerializationAttributes();
        GenericFactory.init();
        Properties.postinit();
        GenericFactory.postinit();
    }    
    
    /**
     * Using the attribute <b>planet.util.Properties.simulatorPrintLevel</b>
     * prints out (or not) the whole network.
     * @param network The network to print out.
     */
    public static void printNetwork(Network network)
    {
        switch (Properties.simulatorPrintLevel)
        {
        case Properties.SIMULATOR_NO_PRINT: break;
        case Properties.SIMULATOR_PRETTY_PRINT: network.prettyPrintNodes(); break;
        default: network.printNodes(); break; //Properties.SIMULATOR_FULL_PRINT
        }
    }
    
	/**
	 * Shows in Y.XXX string format, the seconds elapsed between <b>firstTime</b> and </b>lastTime</b>,
	 * where 'Y' are the seconds, and 'XXX' the milliseconds ones.
	 * The time elapsed is obtained as <b>lastTime-firstTime</b>.
	 * @param firstTime First logged time.
	 * @param lastTime Second logged time.
	 * @return The seconds elapsed between the two logged times, in Y.XXX format.
	 */
	public static String timeElapsedInSeconds(long firstTime, long lastTime)
	{
	    long timeElapsed = lastTime-firstTime;
	    long modulus     = timeElapsed%1000;
	    return 
	    		(timeElapsed/1000)+"."+  //the seconds
	    			((modulus<10)?"00"+modulus:  //up to 9 milliseconds
	        		 (modulus<100)?"0"+modulus:  //up to 99 milliseconds
	        		 ""+modulus);                //up to 999 millisecons
	}
}
