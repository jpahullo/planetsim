package planet.test.serialize;

import planet.commonapi.Network;
import planet.commonapi.exception.InitializationException;
import planet.generic.commonapi.GenericApp;
import planet.generic.commonapi.factory.GenericFactory;
import planet.generic.commonapi.factory.Topology;
import planet.simulate.NetworkSimulator;
import planet.test.TestNames;
import planet.util.Properties;
/**
 * Shows the way to serialize the whole network. 
 * @author Ruben Mondejar
 * @author Jordi Pujol
 */
 
public class SerializeNetwork extends GenericApp {

    /**
	 * Builds a network based with events file "test_join1000d.txt" and
	 * serialize it to file "test_1000.dat".
     * @throws InitializationException if the events file is not found.
     */
    public SerializeNetwork() throws InitializationException
    {
        //arguments: properties file, application level, events, results, serialization
        super("../conf/master.properties",TestNames.SERIALIZE_SERIALIZENETWORK,false,false,false,true);

        Properties.serializedOutputFileReplaced = false;
        if (!Topology.isValidForNew(Properties.factoriesNetworkTopology))
            throw new Error ("The network topology cannot be 'Serialized'. Replace it, please");
        
        
		//build network
		long t1,t2;
        int steps;
        Network net = null;
        final int[] sizes = new int[]{10,100,1000};
        for (int i =0; i < sizes.length; i++)
        {
            //ensure the required network size
            Properties.factoriesNetworkSize = sizes[i];
            //updating simulator context
            restart(false,false,false,true);
            
            //building network
    		t1 = System.currentTimeMillis();
    		net = GenericFactory.buildNetwork();
    		steps = net.stabilize();
    		t2 = System.currentTimeMillis();
    		steps = net.getSimulatedSteps();
    		System.out.println("Simulation time ["+GenericApp.timeElapsedInSeconds(t1,t2)+"] seconds for ["+sizes[i]+"] nodes with ["+steps+"] steps.");
            System.out.println("RouteMessages: Created["+GenericFactory.getBuiltRouteMessages()+"], reused["+GenericFactory.getReusedRouteMessages()+"], free["+GenericFactory.getFreeRouteMessages()+"]");
            
            //network serialization
            NetworkSimulator.saveState(net);
        }
    }
    
	/**
	 * @param args Nothing waiting.
	 */
	public static void main(String[] args) {
	    try {
	        new SerializeNetwork();
	    } catch (InitializationException e)
	    {
	        System.out.println("Initialization error in GenSerializedFile...");
	        e.printStackTrace();
	    }
	}
}
