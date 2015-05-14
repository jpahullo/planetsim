package planet.test;

import java.util.Vector;

import planet.commonapi.Network;
import planet.generic.commonapi.GenericApp;
import planet.generic.commonapi.factory.GenericFactory;
import planet.generic.commonapi.factory.Topology;
import planet.simulate.EventParser;
import planet.simulate.NetworkSimulator;
import planet.simulate.Scheduler;
import planet.util.Properties;

/**
 * This test use an event file in the "bin/data" directory, specified into
 * the current configuration. Shows the time elapsed for generating the network 
 * and its stabilization. 
 * @author <a href="mailto:Ruben.Mondejar@estudiants.urv.es">Ruben Mondejar</a>
 * @author <a href="mailto:jordi.pujol@estudiants.urv.es">Jordi Pujol</a>
 */ 
public class SimTest {

	/**
	 * Initialize the simulator context and run the test.
	 * @param args Nothing.
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		//init context
	    //arguments: properties file, application level, events, results, serialization
        GenericApp.start("../conf/master.properties",TestNames.SIMTEST,false,true,false,false);
        
        //ensure network size to 0 to be able to add events
        Properties.factoriesNetworkSize = 0;
        Properties.factoriesNetworkTopology = Topology.RANDOM;
        GenericApp.restart(false,true,false,false);
		
		//load events
		Vector events = EventParser.parseEvents(Properties.simulatorEventFile);
		Scheduler timer = new Scheduler();
		timer.addEvents(events);
			
		long t1,t2,t3,t4;
		
		//actual millis
		t1 = System.currentTimeMillis();
		NetworkSimulator sim = new NetworkSimulator(timer);
		t2 = System.currentTimeMillis();
		Network net = sim.getInternalNetwork();
		int steps = net.getSimulatedSteps();
		System.out.println("Network creation time ["+GenericApp.timeElapsedInSeconds(t1,t2)+"] seconds for ["+net.size()+"] nodes with ["+steps+"] steps.");
		
		//stabilization
		t3 = System.currentTimeMillis();
		sim.stabilize();
		t4 = System.currentTimeMillis();
		int steps2 = net.getSimulatedSteps();
		System.out.println("Simulation time ["+GenericApp.timeElapsedInSeconds(t3,t4)+"] seconds for ["+net.size()+"] nodes with ["+(steps2-steps)+"] steps.");
		System.out.println("Total time ["+GenericApp.timeElapsedInSeconds(t1,t4)+"] seconds for ["+net.size()+"] nodes with ["+steps2+"] steps.");
        System.out.println("RouteMessages: Created["+GenericFactory.getBuiltRouteMessages()+"], reused["+GenericFactory.getReusedRouteMessages()+"], free["+GenericFactory.getFreeRouteMessages()+"]");
        GenericApp.printNetwork(net);
	}
}
