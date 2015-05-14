package planet.test.factory;

import planet.commonapi.Id;
import planet.commonapi.exception.InitializationException;
import planet.commonapi.factory.IdFactory;
import planet.generic.commonapi.GenericApp;
import planet.generic.commonapi.factory.GenericFactory;
import planet.test.TestNames;

/**
 * Simple test that builds an IdFactory and an Id using it.
 * @author Jordi Pujol
 */
public class TestIdFactory extends GenericApp {
	/**
	 * Buils an instance of IdFactory and a Id and
	 * prints its class.
	 */
	public TestIdFactory() throws InitializationException {
        //arguments: properties file, application level, events, results, serialization
        super("../conf/master.properties",TestNames.FACTORY_TESTIDFACTORY,true,false,false,false);

		IdFactory nf = null;
		try {
			nf = GenericFactory.buildIdFactory();
		} catch (Exception e) {
			System.out.println("Error creating factory... ");
			e.printStackTrace();
			return;
		}
		
		try {
			Id id = nf.buildId();
			System.out.println("I'm an instance of ["+
					id.getClass().getName() +
					"] with Id value = [" +id+"].");
		} catch (InitializationException e) {
			System.out.println("Error creating Id...");
			e.printStackTrace();
		}
	}
	
	/**
	 * Test the IdFactoryImpl.
	 * @param args Waiting nothing.
	 */
	public static void main(String[] args) {
		try {
			new TestIdFactory();
		} catch (InitializationException e) {
			System.out.println("TestIdFactory: Exception thrown....");
			e.printStackTrace();
		}
	}
}
