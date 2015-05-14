package planet.test.helloworld;
import planet.commonapi.Network;
import planet.commonapi.exception.InitializationException;
import planet.generic.commonapi.GenericApp;
import planet.generic.commonapi.factory.GenericFactory;
import planet.test.TestNames;
import planet.util.Properties;
/**
 * Makes a simple Hello World test.
 * 
 * @author Carles Pairot <cpairot@etse.urv.es>
 * @author Jordi Pujol <jordi.pujol@estudiants.urv.es>
 * @version 1.0
 */
public class DHTPeerTest extends GenericApp {
	/**
	 * Network to contain all nodes.
	 */
	private static Network network = null;
	
	/**
	 * Builds a network, following the values of the properties files.
	 *  
	 */
	public DHTPeerTest() throws InitializationException {
        //arguments: properties file, application level, events, results, serialization
        super("../conf/master.properties",TestNames.HELLOWORLD_DHTPEERTEST,true,false,false,false);

        if (!Properties.factoriesApplication.getName().equals("planet.test.helloworld.DHTApplication"))
            throw new Error("This test only can be run with the application 'planet.test.helloworld.DHTApplication'");
        
		try {
			//builds a network with the properties specified at properties
		    long t1 = System.currentTimeMillis();
			network = GenericFactory.buildNetwork();
			int steps1 = network.stabilize();
			long t2 = System.currentTimeMillis();
			System.out.println("Network with ["+network.size()+"] created in ["+GenericApp.timeElapsedInSeconds(t1,t2)+"] seconds and ["+steps1+"] steps\n");
			network.prettyPrintNodes();
			//register to all nodes the application specified at properties
			network.registerApplicationAll();
			System.out.println("Application added. Making 15 'Hello World!' from random nodes....\n");
			int steps2 =0;
			for (int i=0; i< 15; i++)
			    steps2=sendMessage();
			long t3 = System.currentTimeMillis();
			System.out.println("'Hello World' message send in a network of ["+network.size()+"] nodes in ["+GenericApp.timeElapsedInSeconds(t2,t3)+"] seconds and ["+(steps2-steps1)+"] steps\n");
			System.out.println("Total time with ["+network.size()+"] nodes is ["+GenericApp.timeElapsedInSeconds(t1,t3)+"] seconds and ["+steps2+"] steps\n");
            System.out.println("RouteMessages: Created["+GenericFactory.getBuiltRouteMessages()+"], reused["+GenericFactory.getReusedRouteMessages()+"], free["+GenericFactory.getFreeRouteMessages()+"]");
		} catch (InitializationException e) {
			System.out.println("Occur an exception in initialization of "
					+ this.getClass().getName() + ": " + e.getMessage());
			System.exit(-1);
		}
	}
	
	/**
	 * Send a Hello World message to the network, getting a radomly existing 
	 * DHTApplication of the network.
	 */
	public int sendMessage() {
		System.out.println("Sending message...");
		String messageText = "Hello World! ";
		DHTPeerTestMessage mesg = new DHTPeerTestMessage(messageText);
		DHTApplication app = (DHTApplication) network
				.getRandomApplication(DHTApplication.applicationId);
		app.send(messageText, mesg);
		return network.stabilize();
	}

	/**
	 * Starts the test, building a network following the values of the
	 * properties files and send a simple Hello World message.
	 * @param args Nothing waiting.
	 */
	public static void main(String args[]) {
		try {
			DHTPeerTest c = new DHTPeerTest();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.exit(0);
	}
}
