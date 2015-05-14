package planet.test.serialize;


import planet.commonapi.Network;
import planet.generic.commonapi.GenericApp;
import planet.generic.commonapi.factory.GenericFactory;
import planet.generic.commonapi.factory.Topology;
import planet.test.TestNames;
import planet.util.Properties;
/**
 * Builds a network based with a serialized network specified in the
 * properties file. 
 * @author Ruben Mondejar
 * @author Jordi Pujol
 */
 
public class LoadSerializedFile {

	/**
	 * Loads a serialized network from the specified serialized file
	 * at properties file.
	 * @param args Waiting nothing.
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		//init context
        //arguments: properties file, application level, events, results, serialization
        GenericApp.start("../conf/master.properties",TestNames.SERIALIZE_LOADSERIALIZEDFILE,true,false,false,true);

        if (!Properties.factoriesNetworkTopology.equalsIgnoreCase(Topology.SERIALIZED))
            throw new Error ("This test must only run under a Serialized network.");
        
		System.out.println("Properties.serializedFile = "+Properties.serializedInputFile);

		//loads network.
		long t1,t2;
		t1 = System.currentTimeMillis();
		Network net = GenericFactory.buildNetwork();
		t2 = System.currentTimeMillis(); 
		int steps = net.getSimulatedSteps();
		System.out.println("Load time ["+((t2-t1)/1000)+"] seconds for ["+net.size()+"] nodes with ["+steps+"] steps.");
	}
}
