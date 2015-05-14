package planet.test.dht;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Iterator;
import java.util.StringTokenizer;

import planet.commonapi.Application;
import planet.commonapi.Network;
import planet.commonapi.Node;
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
 * @version 1.0
 */
public class DHTTest extends GenericApp {

	/**
   * Constructor that initialize a network with MAX_NODES, and register over each node
   * an instance of DHTApplication.
   */
  public DHTTest () throws InitializationException {
    //arguments: properties file, application level, events, results, serialization
    super("../conf/master.properties",TestNames.DHT_DHTTEST,true,false,false,false);
    
    //verifying the input configuration
    if (!Properties.factoriesApplication.getName().equals("planet.test.dht.DHTApplication"))
        throw new Error("This test only can run with the application 'planet.test.dht.DHTApplication'");
    
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
        System.out.println (Properties.factoriesNetworkSize + " nodes created OK with ["+steps+"] steps and ["+GenericApp.timeElapsedInSeconds(t1,t2)+"] seconds.\n");
        System.out.println("RouteMessages: Created["+GenericFactory.getBuiltRouteMessages()+"], reused["+GenericFactory.getReusedRouteMessages()+"], free["+GenericFactory.getFreeRouteMessages()+"]");
        Results.resetInserts();
        
        
        //INSERTION PROCESS
        System.out.println("\nStarting key insertion...");
        t1 = System.currentTimeMillis();
        DHTApplication app = (DHTApplication)network.getRandomApplication(DHTApplication.APPLICATION_ID);
        try {
        	String aux             = null;
        	String key             = null;
        	String value           = null;
        	StringTokenizer tokens = null;
            FileInputStream fis = new FileInputStream ("data/dht.DHTTest.keys");
            BufferedReader dis = new BufferedReader (new InputStreamReader (fis));
            try {
              aux = dis.readLine();
            }
            catch (IOException ex1) {
            }
            //while (aux != null) {
            int i=0;
            while (aux != null && i<100) {
            	i++;
              tokens = new StringTokenizer (aux, ";");
              value = tokens.nextToken();
              key = tokens.nextToken();
              System.out.println ("Inserting key = " + key + " value = " + value);
              
              //sends a DHTMessage with key/value pair to correct responsible node.
              app.send(key, value); 
              network.run(Properties.simulatorSimulationSteps);

              try {
                aux = dis.readLine();
              }
              catch (IOException ex3) {
              }
            }

            try {
              fis.close();
            }
            catch (IOException ex2) {
            }
          }
          catch (FileNotFoundException ex) {
          	System.out.println("File with keys not found.");
          }
          t2 = System.currentTimeMillis();
          steps = network.stabilize();
          System.out.println ("Keys inserted with ["+steps+"] steps and ["+GenericApp.timeElapsedInSeconds(t1,t2)+"] seconds.\n");
          
          //LOOKUP PROCESS
          t1 = System.currentTimeMillis();
          for (int i=0; i< 15; i++)
          {
              app = (DHTApplication)network.getRandomApplication(DHTApplication.APPLICATION_ID);
              System.out.println ("\nLooking up key f9a9f7cfae30f9443e913ce72f4c767e2c44d85f...");
              app.send("f9a9f7cfae30f9443e913ce72f4c767e2c44d85f");
              steps = network.stabilize();
              System.out.println ("///////////////////////////////////////////////////////////////");
          }
          
          System.out.println ("\nLooking up unexisting key f9a9f7cfae30f9443e913ce72f4c767e2c44d85...");
          app.send("f9a9f7cfae30f9443e913ce72f4c767e2c44d85");
          steps = network.stabilize();
          t2 = System.currentTimeMillis();
          System.out.println ("Key lookup finished with ["+steps+"] steps and ["+GenericApp.timeElapsedInSeconds(t1,t2)+"] seconds.\n");
          
          //PRINT APPLICATON INFORMATION
          Iterator nodes = network.iterator();
          FileOutputStream fos = null;
          try {
            fos = new FileOutputStream("dht.DHTTest.statistics");
          }
          catch (FileNotFoundException ex4) {
          }
          BufferedWriter dos = new BufferedWriter (new OutputStreamWriter(fos));
          while (nodes.hasNext()) {
          	//get node
          	Node node = (Node)nodes.next();
          	//get its applications ==> ONLY EXISTS ONE FOR EACH NODE
            Application[] apps = node.getRegisteredApplications();
            try {
              dos.write(((DHTApplication)apps[0]).gatherStatistics());
            }
            catch (IOException ex6) {
            	System.out.println(ex6.getMessage());
            }
          }
          try {
			dos.flush();
          } catch (IOException e1) {
			e1.printStackTrace();
          }
          
  	} catch (InitializationException e) {
  		System.out.println("Occur an exception in initialization of "+this.getClass().getName()+": "+e.getMessage());
  		System.exit(-1);
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
		System.out.println("Error during initialization of DHTTest");
		e.printStackTrace();
	}
  }
}
