package planet.test.dht2;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import planet.commonapi.Network;
import planet.commonapi.exception.InitializationException;
import planet.generic.commonapi.GenericApp;
import planet.generic.commonapi.factory.GenericFactory;
import planet.simulate.Results;
import planet.test.TestNames;
import planet.util.Properties;

/**
 * Main application that tests to inserts a wide number of key/value
 * pairs at the ring and lookups a concrete existing key.
 * @author Carles Pairot   <cpairot@etse.urv.es>
 * @author Jordi Pujol     <jordi.pujol@estudiants.urv.es>
 * @author Marc Sanchez	   <marc.sanchez@estudiants.urv.es>
 * @version 1.0
 */
public class DHTTest extends GenericApp {

	/**
   * Constructor that initialize a network with MAX_NODES, and register over each node
   * an instance of SymphonyDHTApplication.
   */
  public DHTTest () throws InitializationException {
      //arguments: properties file, application level, events, results, serialization
      super("../conf/master.properties",TestNames.DHT2_DHTTEST,true,false,false,true);

  	try {
  		
  		System.out.println ("Starting creation of " + Properties.factoriesNetworkSize + " nodes...");
    
  		long t1 = System.currentTimeMillis();
  		//build network but not is stabilize
  		Network network = GenericFactory.buildNetwork();
  		//stabilize network
  		int steps = network.stabilize();
  		//register application
  		network.registerApplicationAll();
  		long t2 = System.currentTimeMillis();
        System.out.println (Properties.factoriesNetworkSize + " nodes created OK with [" + steps + "] steps and [" + GenericApp.timeElapsedInSeconds(t1,t2) + "] seconds.\n");
        System.out.println("RouteMessages: Created["+GenericFactory.getBuiltRouteMessages()+"], reused["+GenericFactory.getReusedRouteMessages()+"], free["+GenericFactory.getFreeRouteMessages()+"]");
        network.printNodes();
        Results.resetInserts();
        
        
        //INSERTION PROCESS
        System.out.println("\nStarting key insertion...");
        
        t1 = System.currentTimeMillis();
        DHTApplication app = (DHTApplication)network.getRandomApplication(DHTApplication.APPLICATION_ID);
        
        // Insert From 'BluesMan.txt'
        try {
        	insertAll("data/BluesMan.txt", app, network);
        } catch(FileNotFoundException insertF) {
        	throw new InitializationException("!! Exception when INSERT on: "+ this.getClass().getName() + " - " + insertF.getMessage());
        }
        
        t2 = System.currentTimeMillis();
        steps = network.stabilize();
        System.out.println ("Keys inserted with [" + steps + "] steps and [" + GenericApp.timeElapsedInSeconds(t1,t2) + "] seconds.\n");
        
        //LOOKUP PROCESS
        System.out.println("\nStarting lookups...");
        t1 = System.currentTimeMillis();
        try {
        	lookupAll("data/BluesMan.txt", 10, app, network);
        } catch(FileNotFoundException lookupF) {
        	throw new InitializationException("!! Exception when LOOKUP on: "+ this.getClass().getName() + " - " + lookupF.getMessage());
        }
        steps = network.stabilize();
        t2 = System.currentTimeMillis();
        System.out.println ("Key lookup finished with ["+steps+"] steps and ["+GenericApp.timeElapsedInSeconds(t1,t2)+"] seconds.\n");

        // Print 
//        planet.results.LinkStateResults.printByType();
//        planet.results.LinkStateResults.printByNode();
        
        // network.printNodes();
  	} catch (InitializationException e) {
  		System.out.println("Occur an exception in initialization of " + this.getClass().getName()+ ": " + e.getMessage());
  		System.exit(-1);
  	}
  }
  /**
   * This methods insert all keys from a file called <b> insertSet </b> to DHT.
   * @param insertSet The filename with all data to insert. 
   * @param app The instance class object of the DHT application.
   * @param network The handler from the overlay network.
   * @throws FileNotFoundException If file called <b> insertSet </b> is unavailable.
   */
  public void insertAll(String insertSet, DHTApplication app, Network network) throws FileNotFoundException {
  	FileInputStream F = new FileInputStream(insertSet);
  	BufferedReader BF = new BufferedReader(new InputStreamReader(F));
  	String Q = null, k = null;
  	try {
  		do {
  			Q = BF.readLine();
  			if (Q != null) {
  				app.insert(Q, Q);
  				network.run(Properties.simulatorSimulationSteps);
  			}
  		} while (Q != null);
  		F.close();
	} catch(IOException e) {
	}
  }
  /**
   * This method launches several lookup operation into Symphony Ring. 
   * @param lookupSet The lookup set with all keys to retrieve.
   * @param UpperBound The maximum number of lookups launched by this method.
   * @param app The instance class object of the DHT application.
   * @param network The handler from the overlay network.
   * @throws FileNotFoundException If file called <b> lookupSet </b> is unavailable.
   */
  public void lookupAll(String lookupSet, int UpperBound, DHTApplication app, Network network) throws FileNotFoundException {
  	FileInputStream F = new FileInputStream(lookupSet);
  	BufferedReader BF = new BufferedReader(new InputStreamReader(F));
  	String Q = null;
  	int LowBound = 0;
  	try {
  		do {
  			Q = BF.readLine();
  			if (Q != null && LowBound++ < UpperBound) 
            {
                app.lookup(Q);
                network.run(Properties.simulatorSimulationSteps);
            }
  		} while (Q != null && LowBound < UpperBound);
  		F.close();
	} catch(IOException e) {
	}
  }
    
  /**
   * Initialize a network with the number of nodes specified by command line argument,
   * and inserts all keys/values existing at file claves.txt.
   * @param args
   */
  public static void main (String args[]) {

    try {
		DHTTest c = new DHTTest();
	} catch (InitializationException e) {
		System.out.println("Error during initialization of SymphonyDHT");
		e.printStackTrace();
	}
  }
}
