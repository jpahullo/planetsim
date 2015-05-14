package planet.test.factory;

import planet.commonapi.Application;
import planet.commonapi.exception.InitializationException;
import planet.commonapi.factory.ApplicationFactory;
import planet.generic.commonapi.GenericApp;
import planet.generic.commonapi.factory.GenericFactory;
import planet.test.TestNames;

/**
 * Simple test that builds an ApplicationFactory and an Application using it.
 * @author Jordi Pujol
 */
public class TestAppFactory extends GenericApp {
	/**
	 * Buils an instance of ApplicationFactory and a Application and
	 * prints its class.
	 */
	public TestAppFactory() throws InitializationException {
        //arguments: properties file, application level, events, results, serialization
        super("../conf/master.properties",TestNames.FACTORY_TESTAPPFACTORY,true,false,false,false);
		ApplicationFactory nf = null;
		try {
			nf = GenericFactory.buildApplicationFactory();
		} catch (Exception e) {
			System.out.println("Error creating factory... ");
			e.printStackTrace();
			return;
		}
		
		try {
			Application app = nf.buildApplication();
			System.out.println("I'm an instance of ["+
					app.getClass().getName() +
					"] with default Id = [" +app.getId()+"].");
			app = nf.buildApplicationWithName("TestApp");
			System.out.println("I'm an instance of ["+
					app.getClass().getName() +
					"] with Id = [" +app.getId()+"].");
		} catch (InitializationException e) {
			System.out.println("Error creating Application...");
			e.printStackTrace();
		}
	}
	
	/**
	 * Test the ApplicationFactoryImpl.
	 * @param args Waiting nothing.
	 */
	public static void main(String[] args) {
		try {
			new TestAppFactory();
		} catch (InitializationException e) {
			System.out.println("TestAppFactory: Exception thrown....");
			e.printStackTrace();
		}
	}
}
