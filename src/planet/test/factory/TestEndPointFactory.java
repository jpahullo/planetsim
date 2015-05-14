package planet.test.factory;

import planet.commonapi.EndPoint;
import planet.commonapi.exception.InitializationException;
import planet.commonapi.factory.ApplicationFactory;
import planet.commonapi.factory.EndPointFactory;
import planet.commonapi.factory.NodeFactory;
import planet.generic.commonapi.GenericApp;
import planet.generic.commonapi.factory.GenericFactory;
import planet.test.TestNames;

/**
 * Simple test that builds an EndPointFactory and an EndPoint using it.
 * @author Jordi Pujol
 */
public class TestEndPointFactory extends GenericApp {
	/**
	 * Buils an instance of EndPointFactory and a EndPoint and
	 * prints its class.
	 */
	public TestEndPointFactory() throws InitializationException {
        //arguments: properties file, application level, events, results, serialization
        super("../conf/master.properties",TestNames.FACTORY_TESTENDPOINTFACTORY,true,false,false,false);

		NodeFactory nf = null;
		ApplicationFactory af = null;
		EndPointFactory epf = null;
		try {
			nf = GenericFactory.buildNodeFactory();
			af = GenericFactory.buildApplicationFactory();
			epf = GenericFactory.buildEndPointFactory();
		} catch (Exception e) {
			System.out.println("Error creating factory... ");
			e.printStackTrace();
			return;
		}
		
		try {
			EndPoint ep = epf.buildEndPoint(af.buildApplication(),nf.buildNode());
			System.out.println("I'm an instance of ["+
					ep.getClass().getName() +
					"] with EndPoint value = [" +ep+"].");
		} catch (InitializationException e) {
			System.out.println("Error creating EndPoint...");
			e.printStackTrace();
		}
	}
	
	/**
	 * Test the IdFactoryImpl.
	 * @param args Waiting nothing.
	 */
	public static void main(String[] args) {
		try {
			new TestEndPointFactory();
		} catch (InitializationException e) {
			System.out.println("TestEndPointFactory: Exception thrown....");
			e.printStackTrace();
		}
	}
}
