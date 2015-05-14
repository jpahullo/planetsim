package planet.test.factory;

import planet.commonapi.Node;
import planet.commonapi.exception.InitializationException;
import planet.commonapi.factory.NodeFactory;
import planet.generic.commonapi.GenericApp;
import planet.generic.commonapi.factory.GenericFactory;
import planet.test.TestNames;

/**
 * Simple test that builds a NodeFactory and a Node using it.
 * @author Jordi Pujol
 */
public class TestNodeFactory extends GenericApp {
	/**
	 * Buils an instance of NodeFactory and a Node and
	 * prints its class.
	 */
	public TestNodeFactory() throws InitializationException {
        //arguments: properties file, application level, events, results, serialization
        super("../conf/master.properties",TestNames.FACTORY_TESTNODEFACTORY,false,false,false,false);

		NodeFactory nf = null;
		try {
			nf = GenericFactory.buildNodeFactory();
		} catch (Exception e) {
			System.out.println("Error creating factory... ");
			e.printStackTrace();
			return;
		}
		
		try {
			Node n = nf.buildNode();
			System.out.println("I'm an instance of ["+
					n.getClass().getName() +
					"] with Id = [" +n.getId()+"].");
		} catch (InitializationException e) {
			System.out.println("Error creating Node...");
			e.printStackTrace();
		}
	}
	
	/**
	 * Test the NodeFactoryImpl.
	 * @param args Waiting nothing.
	 */
	public static void main(String[] args) {
		try {
			new TestNodeFactory();
		} catch (InitializationException e) {
			System.out.println("TestNodeFactory: Exception thrown....");
			e.printStackTrace();
		}
	}
}
