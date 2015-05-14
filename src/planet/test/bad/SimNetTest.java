package planet.test.bad;


import planet.chord.ChordProperties;
import planet.commonapi.Network;
import planet.commonapi.factory.NodeFactory;
import planet.generic.commonapi.GenericApp;
import planet.generic.commonapi.factory.GenericFactory;
import planet.generic.commonapi.factory.Topology;
import planet.test.TestNames;
import planet.util.Properties;
/**
 * This test puts into the same network Chord nodes, but there are a 2% of bad nodes.
 * Show how to put into the same network "two" types of Nodes.
 * @author Ruben Mondejar
 * @author Jordi Pujol 
 */ 
 
public class SimNetTest {

	/**
	 * This test puts into the same network Chord nodes, but there are a 2% of bad nodes.
	 * Show how to put into the same network "two" types of Nodes.
	 * @param args Nothing.
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		//init context
		int max = 98;
        //arguments: properties file, application level, events, results, serialization
        GenericApp.start("../conf/master.properties",TestNames.BAD_SIMNETTEST,false,false,false,false);
        if (!(Properties.overlayPropertiesInstance instanceof ChordProperties))
        {
            throw new Error("ERROR: Only the Chord overlay can be used in this test.");
        }
        
        //ensure network size to zero to work fine
        Properties.factoriesNetworkSize = 0;
        Properties.factoriesNetworkTopology = Topology.RANDOM;
        GenericApp.restart(false,false,false,false);
        
		NodeFactory nf = GenericFactory.buildNodeFactory(); 
		nf.setValues(GenericFactory.getDefaultIdFactory(),Class.forName("planet.badchord.BadChordNode"));
		
		//load events
		long t1,t2,t3,t4;
		
		//actual millis
		t1 = System.currentTimeMillis();
		Network net = GenericFactory.buildNetwork();
		for (int i=0; i<10;i++) {
			net.joinNodes(max); //add 98 ChordNode
			net.joinNode(nf.buildNode()); //add 2 BadChordNode
			net.joinNode(nf.buildNode());
		}
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
        GenericApp.printNetwork(net);
	}
}
