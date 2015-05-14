package planet.test.trivialp2ptest;

import java.util.Iterator;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

import planet.commonapi.Id;
import planet.commonapi.Network;
import planet.commonapi.Node;
import planet.commonapi.exception.InitializationException;
import planet.generic.commonapi.GenericApp;
import planet.generic.commonapi.factory.GenericFactory;
import planet.test.TestNames;
import planet.trivialp2p.TrivialNode;
import planet.trivialp2p.TrivialProperties;
import planet.util.Properties;

/**
 * Builds a network with the specified size, and sets the nodes routing table
 * directly. Then, make lookup messages and sends them to the destination nodes.
 * @author <a href="mailto: jordi.pujol@estudiants.urv.es">Jordi Pujol</a>
 * 03-jun-2005
 */
public class TrivialTest {

    public TrivialTest() throws InitializationException
    {
        //arguments: properties file, application level, events, results, serialization
        GenericApp.start("../conf/master.properties",TestNames.TRIVIALP2PTEST_TRIVIALTEST,true,false,false,true);

        //init context
/*        Properties.init("../conf/trivial_factories.properties","../conf/trivial_sim.properties","../conf/trivial.properties",
                "../conf/trivial_behaviours.properties");
        GenericFactory.init();*/
        
        boolean makeDebug = ((TrivialProperties)Properties.overlayPropertiesInstance).debug;
        
        //FIRST STEP: network building
        //Postconditions: all nodes are builded
        long t1 = System.currentTimeMillis();
        Network net = GenericFactory.buildNetwork();
        long t2 = System.currentTimeMillis();
        int steps1 = net.getSimulatedSteps();
        System.out.println("Network creation time ["+GenericApp.timeElapsedInSeconds(t1,t2)+"] seconds for ["+net.size()+"] nodes with ["+steps1+"] steps.");
        
        //SECOND STEP: direct stabilization algorithm
        //Postconditions: each node has its routing table correctly updated.
        directStabilization(net);
        if (makeDebug)
            System.out.println("Stabilization algorithm applied...");
        
        //THIRD STEP: register the default application to all nodes
        //Postconditions: each node has a registered instance of the default application
        net.registerApplicationAll();
        if (makeDebug)
            System.out.println("Applications have been registered...");
        
        //FOURTH STEP: making exhaustive lookups
        //Postconditions: each node has send a lookup message to each other
        long t3 = System.currentTimeMillis();
        makeLookups(net,makeDebug);
        long t4 = System.currentTimeMillis();
        int steps2 = net.getSimulatedSteps();
        System.out.println("Lookup time ["+GenericApp.timeElapsedInSeconds(t3,t4)+"] seconds for ["+net.size()+"] nodes with ["+(steps2-steps1)+"] steps.");
        
        //FIFTH STEP: printing simulation statistics
        System.out.println("Total time ["+GenericApp.timeElapsedInSeconds(t1,t4)+"] seconds for ["+net.size()+"] nodes with ["+steps2+"] steps.");
        System.out.println("RouteMessages: Created["+GenericFactory.getBuiltRouteMessages()+"], reused["+GenericFactory.getReusedRouteMessages()+"], free["+GenericFactory.getFreeRouteMessages()+"]");
        GenericApp.printNetwork(net);
    }
    
    /**
     * Sets the routing table to each node into the network.
     * @param network The built network.
     */
    private void directStabilization(Network network)
    {
        Iterator it = network.iterator();
        if (!it.hasNext())
            throw new Error("Cannot make a simulation without nodes.");
        
        //updating all links on the ring
        TrivialNode last = (TrivialNode)it.next();
        TrivialNode temp = null;
        TrivialNode first = last;
        while (it.hasNext())
        {
            temp = (TrivialNode)it.next();
            temp.setPredecessor(last.getLocalHandle());
            last.setSuccessor(temp.getLocalHandle());
            last = temp;
        }
        
        //closing the ring
        first.setPredecessor(last.getLocalHandle());
        last.setSuccessor(first.getLocalHandle());
    }
    
    /**
     * Makes an exhaustive lookups.
     * @param network Network wiht all builded nodes.
     * @param makeDebug true for showing information for debug purposes.
     */
    private void makeLookups(Network network, boolean makeDebug)
    {
        //saving the keys to be found
        TreeSet idSet = new TreeSet();
        Iterator it = network.iterator();
        while (it.hasNext())
        {
            idSet.add(((Node)it.next()).getId());
        }
        if (makeDebug)
            System.out.println("Keys to be found:\n"+idSet+"\n");
        
        //making lookups with the keys
        Random random = new Random();
        for (int i=0; i< 10; i++)
        {
            makeLookupsPerNode((Node)network.getRandomNode(random),idSet,network,makeDebug);
        }
        //Uncomment for a complete exhaustive lookups
        /*it = network.iterator();
        while (it.hasNext())
        {
            makeLookupsPerNode((Node)it.next(),idSet,network, makeDebug);
        }*/
    }

    /**
     * Build all lookups for the specified <b>node</b>. The keys to be found
     * are in the <b>ids</b>.
     * @param node Node which have to send the lookups.
     * @param ids The set with all the keys to found.
     * @param net The actual Network.
     * @param makeDebug true for showing information for debug purposes.
     */
    private void makeLookupsPerNode(Node node,Set ids, Network net, boolean makeDebug)
    {
        if (makeDebug)
        {
            System.out.println("\nStarting lookups for node ["+node.getId()+"]:");
            System.out.println("--------------------------------------------------------------");
        }
        
        Iterator it = ids.iterator();
        TrivialApplication app = (TrivialApplication)node.getRegisteredApplications()[0];
        while (it.hasNext())
        {
            app.find((Id)it.next());
            net.run(Properties.simulatorSimulationSteps); //makes few steps to send the message
            if (makeDebug)
                System.out.print(".");
        }
        net.stabilize();
        if (makeDebug)
            System.out.println();
    }
    
    /**
     * Builds an instance of TrivialTest.
     * @param args Nothing is required.
     */
    public static void main(String[] args) {
        try {
            new TrivialTest();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
