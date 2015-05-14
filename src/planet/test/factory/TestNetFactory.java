package planet.test.factory;

import planet.commonapi.Network;
import planet.commonapi.exception.InitializationException;
import planet.commonapi.factory.NetworkFactory;
import planet.generic.commonapi.GenericApp;
import planet.generic.commonapi.factory.GenericFactory;
import planet.test.TestNames;
import planet.util.Properties;

/**
 * Simple test that builds a NetworkFactory and a Network using it.
 * @author Jordi Pujol
 */
public class TestNetFactory extends GenericApp {
	/**
	 * Buils an instance of NetworkFactory and a Network and
	 * prints its class.
	 */
	public TestNetFactory() throws InitializationException {
        //arguments: properties file, application level, events, results, serialization
        super("../conf/master.properties",TestNames.FACTORY_TESTNETFACTORY,false,false,false,false);

		NetworkFactory nf = null;
		try {
			nf = GenericFactory.buildNetworkFactory();
		} catch (Exception e) {
			System.out.println("Error creating factory... ");
			e.printStackTrace();
			return;
		} 
		
		try {
			Network net = nf.buildNetwork();
			System.out.println("Network created... stabilizing!!");
			net.stabilize();
			System.out.println("Network stabilized... basic printing nodes!!");
			net.prettyPrintNodes();
			System.out.println("I'm an instance of ["+
					net.getClass().getName() +
					"] with this number of nodes = [" +net.size()+"]; preview ["+Properties.factoriesNetworkSize+"].");
		} catch (InitializationException e) {
			System.out.println("Error creating Network...");
			e.printStackTrace();
		}
	}
	
	/**
	 * Test the NetworkFactoryImpl.
	 * @param args Waiting nothing.
	 */
	public static void main(String[] args) {
		try {
			new TestNetFactory();
		} catch (InitializationException e) {
			System.out.println("TestNetFactory: Exception thrown....");
			e.printStackTrace();
		}
	}
}
