package planet.test.scribe;

import java.util.Iterator;

import planet.commonapi.Application;
import planet.commonapi.Network;
import planet.commonapi.Node;
import planet.commonapi.exception.InitializationException;
import planet.generic.commonapi.GenericApp;
import planet.generic.commonapi.factory.GenericFactory;
import planet.scribe.Scribe;
import planet.scribe.ScribeContent;
import planet.scribe.ScribeImpl;
import planet.scribe.Topic;
import planet.simulate.NetworkSimulator;
import planet.test.TestNames;
import planet.util.Properties;
/**
 * @author Ruben Mondejar  <Ruben.Mondejar@estudiants.urv.es>
 */

public class ScribePeerTest extends GenericApp {

  // Simulator
  private static NetworkSimulator simulator = null;
  private static Network network = null;

  // DHT applications reference 
  private Scribe[] myScribes = null;
  
  private Topic topic = null;

  /**
   *  Constructor
   *
   */
  public ScribePeerTest() throws InitializationException {
    //arguments: properties file, application level, events, results, serialization
    super("../conf/master.properties",TestNames.SCRIBE_SCRIBEPEERTEST,true,false,false,false);

    if (!Properties.factoriesApplication.getName().equals("planet.scribe.ScribeImpl"))
        throw new Error("This test only can be run with the application 'planet.scribe.ScribeImpl'");

  	try {
  		//builds a network with the properties specified at properties file
  		network = GenericFactory.buildNetwork();
  		System.out.println("Network created\n");
  		network.stabilize();
  		network.prettyPrintNodes();
  		//register to all nodes the application specified at properties files
  		//network.registerApplicationAll();
  		
  		topic = new Topic(GenericFactory.buildKey("This is the large topic for this test"));
  		System.out.println(topic);
  		network.stabilize();
  		myScribes = new Scribe[network.size()];
  		Iterator it = network.iterator();  		
  		int i=network.size()-1;
  		while (i>=0 && it.hasNext()) {
  		  Node node = (Node)it.next();
  		  //Application[] apps = n.getRegisteredApplications();
  		  //myScribes[i] = (Scribe) apps[0];
  		  myScribes[i] = new ScribeImpl("Scribe");  		 
  		  node.registerApplication((Application) myScribes[i],"Scribe");
  		  System.out.println("Application "+i+" to node "+node.getId());
  		  if (myScribes[i].isRoot(topic)) System.out.println("Scribe "+i+" is root of this topic");
  		  i--;
  		}
  		
  		for (int j=0;j<network.size();j++) {
  		  myScribes[j].subscribe(topic,new ScribeApplication("Client "+j));  		
  		  network.simulate();
  		}
  		network.stabilize();   
  		
  		System.out.println("Application added in "+network.size()+" nodes\n");
  		
  		} catch (InitializationException e) {
  			System.out.println("Occur an exception in initialization of "+this.getClass().getName()+": "+e.getMessage());
  			System.exit(-1);
  		}
  }

  /**
   * Per enviar un event Scribe
   */
  public void sendEvent(int num_node) {  	
    ScribeTestMessage textMissatge = new ScribeTestMessage("Hello World!");
    myScribes[num_node].publish(topic,textMissatge);
    network.stabilize();    
  }
  
  /**
   * Metode principal: rep els arguments i inicialitza el peer servidor
   * @param args
   */
  public static void main(String args[]) {

    try {
      ScribePeerTest c = new ScribePeerTest();
      // Enviem un event a tot el grup      
      c.sendEvent(9);
    } catch (Exception e) {e.printStackTrace();}

    System.exit(0);
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
