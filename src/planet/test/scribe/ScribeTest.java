package planet.test.scribe;

import java.util.Iterator;

import junit.framework.TestCase;
import junit.framework.TestSuite;
import planet.commonapi.Application;
import planet.commonapi.Network;
import planet.commonapi.exception.InitializationException;
import planet.generic.commonapi.GenericApp;
import planet.generic.commonapi.factory.GenericFactory;
import planet.scribe.Scribe;
import planet.scribe.ScribeContent;
import planet.scribe.ScribeImpl;
import planet.scribe.Topic;
import planet.test.TestNames;

/**
 * @author <a href="mailto:Ruben.Mondejar@estudiants.urv.es">Ruben Mondejar</a>
 * @author <a href="mailto:jordi.pujol@estudiants.urv.es">Jordi Pujol</a>
 */
 
public class ScribeTest extends TestCase {
    		
	private Network network;
	
	public ScribeTest(String name) throws InitializationException {
		super(name);
	}

  protected void setUp() {
		
    try {
        //arguments: properties file, master property, application level, events, results, serialization
        GenericApp.start("../conf/master.properties",TestNames.SCRIBE_SCRIBETEST,true,false,false,false);
/*        if (!(Properties.overlayPropertiesInstance instanceof ChordProperties))
        {
            System.out.println("Only the Chord overlay can be configured for this test.");
            System.exit(-1);
        }

        Properties.simulatorQueueSize=250;
        ((ChordProperties)Properties.overlayPropertiesInstance).stabilizeSteps=2;
        ((ChordProperties)Properties.overlayPropertiesInstance).fixFingerSteps=2;  
        
        GenericApp.restart(true,false,false,false);*/
	  
    	} catch (Exception e1) {
    	  e1.printStackTrace();	
    	}	
  }


  public static junit.framework.Test suite() {
    return new TestSuite(ScribeTest.class);
  }
  
  
  
  public void testSend() throws Exception {
  	System.out.println("//// TEST SEND ////");	
	
  	int root_num = 0;  	
  	network = GenericFactory.buildNetwork();
  	network.stabilize();
	
	Topic topic = new Topic(GenericFactory.buildKey("Junit SEND TEST"));
    System.out.println(topic);
	network.stabilize();
	Scribe[] myScribes = new Scribe[network.size()];
	Iterator it = network.iterator();  		
	int i=network.size()-1;
	while (i>=0 && it.hasNext()) {
	  planet.commonapi.Node node = (planet.commonapi.Node)it.next();
	  myScribes[i] = new ScribeImpl("ScribeTest");  		 
	  node.registerApplication((Application) myScribes[i],"ScribeTest");
	  System.out.println("Application "+i+" to node "+node.getId());
	  if (myScribes[i].isRoot(topic)) {
	    System.out.println("Scribe "+i+" is root of this topic");
		root_num=i;
      }
	  i--;
	}
	
	  System.out.println("Application has been register in "+network.size()+" nodes ");
	  
	  ScribeClientTest[] Clients = new ScribeClientTest[network.size()];
	  for (int j=0;j<Clients.length;j++) {
	  	Clients[j] = new ScribeClientTest("ClientTest "+j);
        myScribes[j].subscribe(topic,Clients[j]);  		
		network.simulate();
	  }
	  network.stabilize();   

	  //send message
	  ScribeTestMessage textMissatge = new ScribeTestMessage("Activate!");
	  int random = (int) (Math.random()*((double)(network.size()-1)));
	  System.out.println("Send message from "+random+" node");
	  myScribes[random].publish(topic,textMissatge);
	  network.stabilize();  
	  
	  i=0;
	  for (int j=0;j<Clients.length;j++) 
	  	if (Clients[j].isActivated()) i++;
	  	  
	  assertEquals(Clients.length,i);		
	
  } 
  
  class ScribeTestMessage implements ScribeContent {
    String message;
    
    public ScribeTestMessage(String message) {
    	this.message = message;
    }   
    
    public String toString() {
      	return message;
    }
    
 }

}