package planet.test.serialize;

import java.util.Vector;

import planet.commonapi.exception.InitializationException;
import planet.generic.commonapi.GenericApp;
import planet.generic.commonapi.factory.GenericFactory;
import planet.generic.commonapi.factory.Topology;
import planet.simulate.EventParser;
import planet.simulate.NetworkSimulator;
import planet.simulate.Scheduler;
import planet.test.TestNames;
import planet.util.Properties;
/**
 * Builds a network based with events file "test_join1000d.txt" and serialize 
 * obtained network.
 * @author Ruben Mondejar
 * @author Jordi Pujol
 */
 
public class GenSerializedFile extends GenericApp {

    /**
	 * Builds a network based with events file "test_join1000d.txt" and
	 * serialize it to file "test_1000.dat".
     * @throws InitializationException if the events file is not found.
     */
    public GenSerializedFile() throws InitializationException
    {
        //arguments: properties file, application level, events, results, serialization
        super("../conf/master.properties",TestNames.SERIALIZE_GENSERIALIZEDFILE,true,true,false,true);

        //ensure a network size to zero to adding events
        Properties.factoriesNetworkSize = 0;
        Properties.factoriesNetworkTopology = Topology.RANDOM;
        restart(true,true,false,true);
        
		//load events
        Vector events = null;
        try {
            events = EventParser.parseEvents(Properties.simulatorEventFile);
        } catch (java.io.IOException e)
        {
            throw new InitializationException("Cannot load the events file called ["+Properties.simulatorEventFile+"]",e);
        }
		Scheduler timer = new Scheduler();
		timer.addEvents(events);
			
		//build network and add all events
		long t1,t2;
		t1 = System.currentTimeMillis();
		NetworkSimulator sim = new NetworkSimulator(timer);
		sim.stabilize();
		t2 = System.currentTimeMillis();
		int steps = sim.getInternalNetwork().getSimulatedSteps();
		System.out.println("Simulation time ["+GenericApp.timeElapsedInSeconds(t1,t2)+"] seconds for 1000 nodes with ["+steps+"] steps.");
        System.out.println("RouteMessages: Created["+GenericFactory.getBuiltRouteMessages()+"], reused["+GenericFactory.getReusedRouteMessages()+"], free["+GenericFactory.getFreeRouteMessages()+"]");
		sim.saveState();
    }
    
	/**
	 * @param args Nothing waiting.
	 */
	public static void main(String[] args) {
	    try {
	        new GenSerializedFile();
	    } catch (InitializationException e)
	    {
	        System.out.println("Initialization error in GenSerializedFile...");
	        e.printStackTrace();
	    }
	}
}
