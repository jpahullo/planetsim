package planet.test;

import planet.commonapi.NodeHandle;
import planet.commonapi.RouteMessage;
import planet.commonapi.exception.InitializationException;
import planet.generic.commonapi.GenericApp;
import planet.generic.commonapi.factory.GenericFactory;
import planet.simulate.Globals;

/**
 * Test the MessagePool, building 10.000 messages, two times in this
 * sequence:
 * <br><br>
 * 1. Build 10.000 nodes<br>
 * 2. Free theese 10.000 nodes<br>
 * 3. Build 10.000 nodes<br>
 * <br>
 * In the last case, don't build really 10.000, only returns a reference of
 * one RouteMessage, created in the first point. 
 * 
 * @author Pedro García
 * @author Carles Pairot
 * @author Ruben Mondejar
 * @author Jordi Pujol
 */
public class TestPool extends GenericApp {
	/**
	 * Numer of messages (default 100.000).
	 */
	public static int MAX = 100000;
	/**
	 * Make the goal test of this class:
	 * <br><br>
	 * 1. Build MAX nodes<br>
	 * 2. Free theese MAX nodes<br>
	 * 3. Build MAX nodes<br>
	 * <br>
	 * @throws InitializationException during context initialization
	 */
	public TestPool() throws InitializationException {
		//context initialization
        //arguments: properties file, application level, events, results, serialization
        super("../conf/master.properties",TestNames.TESTPOOL,false,false,false,false);
		
		RouteMessage list[] = new RouteMessage[MAX];
		
		//prepare all the NodeHandle
		System.out.print("Building ["+MAX*2+"] NodeHandles to be used in RouteMessages...");
		NodeHandle[][] nh = new NodeHandle[2][MAX]; 
		try {
	        for (int i=0;i<MAX;i++){
		      // !!!
		      int[] array = {i, 0, 0, 0, 0};
		      int[] array1 = {i + 1, 0, 0, 0, 0};
		      nh[0][i]= GenericFactory.buildNodeHandle(GenericFactory.buildId(array),true);
		      nh[1][i]= GenericFactory.buildNodeHandle(GenericFactory.buildId(array1),true);
		    }
	    } catch (InitializationException e) {
	    	e.printStackTrace();
	    }
	    System.out.println(" done!!");
	    
	    long t0 = System.currentTimeMillis();
	    
	    //building the RouteMessages
	    try {
		    for (int i=0;i<MAX;i++){
		      list[i] = GenericFactory.getMessage(null,nh[0][i],nh[1][i],Globals.JOIN,1);
		    }
	    } catch (InitializationException e) {
	    	e.printStackTrace();
	    }
	    long t1 = System.currentTimeMillis();
	    
	    //freeing the 'unused' RouteMessages
	    for (int i=0;i<MAX;i++){
	      GenericFactory.freeMessage(list[i]);
	    }
	    long t2 = System.currentTimeMillis();
	    
	    //getting existing RouteMessages
	    try {
		    for (int j=0;j<MAX;j++){
		      // !!!
		      RouteMessage x2 = GenericFactory.getMessage(null,nh[0][j],nh[1][j],Globals.JOIN,1);
		    }
	    } catch (InitializationException e) {
	    	e.printStackTrace();
	    }
	    long t3 = System.currentTimeMillis();
	    
	    System.out.println("1. Time elapsed building "+MAX+" RouteMessages["+GenericApp.timeElapsedInSeconds(t0,t1)+"] milliseconds.");
	    System.out.println("2. Time elapsed freeing "+MAX+" RouteMessages["+GenericApp.timeElapsedInSeconds(t1,t2)+"] milliseconds. Speedup ["+((double)(t1-t0)/(double)(t2-t1))+"]");
	    System.out.println("3. Time elapsed restoring "+MAX+" RouteMessages["+GenericApp.timeElapsedInSeconds(t2,t3)+"] milliseconds. Speedup ["+((double)(t1-t0)/(double)(t3-t2))+"]");
	}

	/**
	 * Make a new reference of this class, that runs the test.
	 * @param args Nothing.
	 */
	public static void main(String[] args) {
		try {
			new TestPool();
		} catch (InitializationException e) {
			System.out.println("TestPool: Exception");
			e.printStackTrace();
		}
	}
}
