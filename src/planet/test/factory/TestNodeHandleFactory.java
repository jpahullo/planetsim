package planet.test.factory;

import planet.commonapi.NodeHandle;
import planet.commonapi.exception.InitializationException;
import planet.commonapi.factory.NodeHandleFactory;
import planet.generic.commonapi.GenericApp;
import planet.generic.commonapi.factory.GenericFactory;
import planet.test.TestNames;

/**
 * Simple test that builds a NodeHandleFactory and a NodeHandle using it.
 * @author Jordi Pujol
 */
public class TestNodeHandleFactory extends GenericApp {
	/**
	 * Buils an instance of NodeHandleFactory and a NodeHandle and
	 * prints its class.
	 */
	public TestNodeHandleFactory() throws InitializationException {
        //arguments: properties file, application level, events, results, serialization
        super("../conf/master.properties",TestNames.FACTORY_TESTNODEHANDLEFACTORY,true,false,false,false);

		NodeHandleFactory nf = null;
		try {
			nf = GenericFactory.buildNodeHandleFactory();
		} catch (Exception e) {
			System.out.println("Error creating factory... ");
			e.printStackTrace();
			return;
		}
		
		try {
			NodeHandle nh = nf.buildNodeHandle(GenericFactory.buildId(),true); 
			System.out.println("I'm an instance of ["+
					nh.getClass().getName() +
					"] with NodeHandle = [" +nh+ "].");
		} catch (InitializationException e) {
			System.out.println("Error creating NodeHandle...");
			e.printStackTrace();
		}
	}
	
	/**
	 * Test the NodeHandleFactoryImpl.
	 * @param args Waiting nothing.
	 */
	public static void main(String[] args) {
		try {
			new TestNodeHandleFactory();
		} catch (InitializationException e) {
			System.out.println("TestNodeHandleFactory: Exception thrown....");
			e.printStackTrace();
		}
	}
}
