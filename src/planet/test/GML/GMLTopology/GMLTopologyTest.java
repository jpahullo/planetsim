package planet.test.GML.GMLTopology;

import planet.commonapi.Network;
import planet.commonapi.exception.InitializationException;
import planet.generic.commonapi.GenericApp;
import planet.generic.commonapi.factory.GenericFactory;
import planet.generic.commonapi.results.ResultsNames;
import planet.test.TestNames;
import planet.util.Properties;



/**
 * <b> GMLTopologyTest </b> is an object class aimed at providing a 
 * description of a Chord Topology using The Graphic File Format GML
 * for its representations an according to the specific GMLConstraint 
 * used for edge selection.
 * @see planet.commonapi.results.ResultsConstraint for more details.  
 * @author  Marc Sanchez <marc.sanchez@estudiants.urv.es> 
 */
public class GMLTopologyTest extends GenericApp {
	  
	  // Constructor
	  public GMLTopologyTest () throws InitializationException {
        //arguments: properties file, application level, events, results, serialization
        super("../conf/master.properties",TestNames.GML_GMLTOPOLOGY_GMLTOPOLOGYTEST,false,false,true,false);

        try {
	  		System.out.println ("\n [Creating Network Of " + Properties.factoriesNetworkSize + " Nodes ]");
	  	    
	  	  	long start = System.currentTimeMillis();
	  	  	// Build network but not is stable
	  	  	Network network = GenericFactory.buildNetwork();
	  	  	// Wait until network is stable 
	  	  	int steps = network.stabilize();
	  	  	long stop = System.currentTimeMillis();
	  	    
	  	  	System.out.println (" [" + Properties.factoriesNetworkSize + " Nodes With [" + steps + "] Steps " +
	  	  						"	And [" + GenericApp.timeElapsedInSeconds(start,stop) + "] Seconds.\n");
            System.out.println("RouteMessages: Created["+GenericFactory.getBuiltRouteMessages()+"], reused["+GenericFactory.getReusedRouteMessages()+"], free["+GenericFactory.getFreeRouteMessages()+"]");
	
            // Obtain Topology Information in GML format
            System.out.print("Getting GML output....");
	  	  	GenericFactory.generateResults(ResultsNames.GML,network, "network.gml", GenericFactory.buildConstraint(ResultsNames.GML), true);
            System.out.println(" done!!");
            // Obtain Topology Information in Pajek format
            System.out.print("Getting Pajek output....");
            GenericFactory.generateResults(ResultsNames.PAJEK,network, "network.net", GenericFactory.buildConstraint(ResultsNames.PAJEK), true);
            System.out.println(" done!!");
	        
	  	} catch (InitializationException e) {
	  		System.out.println("Occur an exception in initialization of " + this.getClass().getName()+ ": " + e.getMessage());
	  		System.exit(-1);
	  	}
	  }

	  /**
	   * Launches GMLTopologyTest app.
	   * @param args Ignore command line args.
	   */
	  public static void main (String args[]) {

	    try {
			GMLTopologyTest c = new GMLTopologyTest();
		} catch (InitializationException e) {
			System.out.println("Error during initialization of GMLTopologyTest");
			e.printStackTrace();
		}
	    System.exit(0);
	  }
}
