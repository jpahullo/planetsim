package planet.test;


import planet.commonapi.Network;
import planet.generic.commonapi.GenericApp;
import planet.generic.commonapi.factory.GenericFactory;
import planet.symphony.SymphonyProperties;
import planet.symphony.messages.NeighbourMessagePool;
import planet.util.Properties; 
/**
 * This example shows how to build a simple Network, without registered Application's 
 * on to the nodes. Can be used the Chord or Symphony overlays implementation.
 *
 * @author <a href="mailto: ruben.mondejar@estudiants.urv.es">Ruben Mondejar</a>
 * @author <a href="mailto: jordi.pujol@estudiants.urv.es">Jordi Pujol</a>
 * 14-jul-2005
 */ 
public class SimNetTest {

	/**
	 * This example shows how to build a simple Network, without registered Application's 
	 * on to the nodes.
	 * @param args Nothing.
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		//init context
        //arguments: properties file, application level, events, results, serialization
        GenericApp.start("../conf/master.properties",TestNames.SIMNETTEST,false,false,false,false);
		
		long t1,t2,t3,t4;  
		
		//actual millis
		t1 = System.currentTimeMillis();
		Network net = GenericFactory.buildNetwork();
		t2 = System.currentTimeMillis();
		int steps = net.getSimulatedSteps();
		System.out.println("Network creation time ["+GenericApp.timeElapsedInSeconds(t1,t2)+"] seconds for ["+net.size()+"] nodes with ["+steps+"] steps.");
		
		//stabilization
		t3 = System.currentTimeMillis();
		int steps2 = net.stabilize();
		t4 = System.currentTimeMillis();
        
		System.out.println("Simulation time ["+GenericApp.timeElapsedInSeconds(t3,t4)+"] seconds for ["+net.size()+"] nodes with ["+(steps2-steps)+"] steps.");
		System.out.println("Total time ["+GenericApp.timeElapsedInSeconds(t1,t4)+"] seconds for ["+net.size()+"] nodes with ["+steps2+"] steps.");
        System.out.println("RouteMessages: Created["+GenericFactory.getBuiltRouteMessages()+"], reused["+GenericFactory.getReusedRouteMessages()+"], free["+GenericFactory.getFreeRouteMessages()+"]");

        //only print out under Symphony overlay
        if (Properties.overlayPropertiesInstance instanceof SymphonyProperties)
            System.out.println("NeighbourMessages: Created["+NeighbourMessagePool.createdMessages+"], reused["+NeighbourMessagePool.reusedMessages+"], free["+NeighbourMessagePool.freeMessages+"]");
        
        //prints or not whole network, depending on current configuration
        GenericApp.printNetwork(net);
	}
}
