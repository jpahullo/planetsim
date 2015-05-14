package planet.test.broadcast;

import planet.commonapi.Network;
import planet.commonapi.exception.InitializationException;
import planet.generic.commonapi.GenericApp;
import planet.generic.commonapi.factory.*;
import planet.test.TestNames;
import planet.util.Properties;

/**
 * This test make a broadcast message over all network nodes.
 * @author Carles Pairot   <cpairot@etse.urv.es>
 * @author Jordi Pujol     <jordi.pujol@estudiants.urv.es>
 * @version 1.0
 */
public class BroadcastTest extends GenericApp {
  /**
   * Network to construct with all specified nodes at properties file.
   */	
  private static Network network = null;
  private static int networkSteps=0; 

  /**
   *  Initialize the broadcast test and builds the network.
   */
  public BroadcastTest() throws InitializationException {
    //arguments: properties file, application level, events, results, serialization
    super("../conf/master.properties",TestNames.BROADCAST_BROADCASTTEST,true,false,false,false);
    
    //verifying the input configuration
    if (!Properties.factoriesApplication.getName().equals("planet.test.broadcast.DHTApplication"))
        throw new Error("This test only can run with the application 'planet.test.broadcast.DHTApplication'");

    System.out.println("NOTE: This test only can be executed under overlays with implemented broadcast operation. By default, only can be Chord.");
    
  	try {
  		//builds a network with the properties specified at properties file
  		long t1 = System.currentTimeMillis();
  		network = GenericFactory.buildNetwork();
  		networkSteps = network.stabilize();
  		long t2 = System.currentTimeMillis();
  		System.out.println("Network created with ["+network.size()+"] nodes and ["+GenericApp.timeElapsedInSeconds(t1,t2)+"] seconds with ["+networkSteps+"] steps\n");
        System.out.println("RouteMessages: Created["+GenericFactory.getBuiltRouteMessages()+"], reused["+GenericFactory.getReusedRouteMessages()+"], free["+GenericFactory.getFreeRouteMessages()+"]");
        //prints out the current network
        printNetwork(network);
        
  		//register to all nodes the application specified at properties files
  		network.registerApplicationAll();
  		System.out.println("Application added\n");
  		
  		} catch (InitializationException e) {
  			System.out.println("Occur an exception in initialization of "+this.getClass().getName()+": "+e.getMessage());
  			System.exit(-1);
  		}
  }

  /**
   * Sends the first broadcast message.
   */
  public void sendMessage() {
    System.out.println ("Sending message...");
    String messageText = "Hello World!";
    DHTApplication app = (DHTApplication)network.getRandomApplication(DHTApplication.applicationId);
    DHTPeerTestMessage mesg = new DHTPeerTestMessage (null,messageText);
    long t1 = System.currentTimeMillis();
    app.send(messageText,mesg);
    int steps = network.stabilize();
    long t2 = System.currentTimeMillis();
    System.out.println("\n\nBroadcast message delivered with ["+GenericApp.timeElapsedInSeconds(t1,t2)+"] seconds with ["+(steps-networkSteps)+"] steps\n");
  }

  
  /**
   * Builds a new BroadcastTest and send the message.
   * @param args
   */
  public static void main(String args[]) {

    try {
      BroadcastTest c = new BroadcastTest();
      // sends the first broadcast test
      c.sendMessage();
    } catch (Exception e) {e.printStackTrace();}

    System.exit(0);
  }
}
