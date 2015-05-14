package planet.simulate;

import java.io.ObjectOutputStream;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.Vector;

import planet.commonapi.Id;
import planet.commonapi.Network;
import planet.commonapi.NodeHandle;
import planet.commonapi.exception.InitializationException;
import planet.generic.commonapi.factory.GenericFactory;
import planet.io.NoReplacingOutputStream;
import planet.util.Properties;

/**
 * The simulator is the main class.  It controls the events to execute them 
 * at the opportune moment and controls the network. 
 * <br><br>
 * The operational model consists of building a network with the actual
 * specified parametres at properties files under root directory 'conf'.
 * <br><br>
 * Then, this simulator modifies the network by invoking different methods.
 * <br><br>
 * The information that is contained by this simulator is the time 
 * (number of steps) of the simulation and the IScheduler specified by constructor.
 * 
 * @author Pedro García
 * @author Carles Pairot
 * @author Ruben Mondejar 
 * @author Jordi Pujol
 */
public class NetworkSimulator implements ISimulator, java.io.Serializable {
  private int time = 0;
  private IScheduler timer;
  private Network network = null;
  private TreeMap nodeHandles = null;
  private int eventsProcessed = 0;
  
  /**
   * Constructor, create a new simulation with a determinate event scheduler
   * and a new network.
   * @param timer Scheduler with the all events to simulate
   * @see planet.simulate.IScheduler
   */       
  public NetworkSimulator (IScheduler timer) throws InitializationException {
    this.timer = timer;    
	Results.clearStabRate(); 
    this.network = GenericFactory.buildNetwork();
    nodeHandles = new TreeMap();
    eventsProcessed = 0;
  }
  
  /**
   * Constructor, create a new simulation with a determinate event scheduler
   * @param timer Scheduler with the all events to simulate
   * @see planet.simulate.IScheduler
   */       
  public NetworkSimulator (IScheduler timer, Network net) throws InitializationException {
    this.timer = timer;    
	Results.clearStabRate(); 
    this.network = net;
    nodeHandles = new TreeMap();
    eventsProcessed = 0;
  }
  
  /**
   * Constructor that generates a new instance of Network and prepares its 
   * simulation using the parameters specified in the properties file.
   * @param timer IScheduler with events.
   * @param factoriesProperties Property file with all parameters for factories.
   * @param simProperties Property file with all parameters for simulator.
   * @param chordProperties Property file with all parameters for Chord.
   * @throws InitializationException if occur any problem during initialization.
   */
  public NetworkSimulator (IScheduler timer, Network net, String factoriesProperties,String simProperties,String chordProperties) 
	throws InitializationException {
  	this.network = net;
    this.timer = timer;    
	Results.clearStabRate(); 
    nodeHandles = new TreeMap();
    eventsProcessed = 0;
  }
  
  /**
   * Initialize this Simulator of Networks.
   * @param timer IScheduler with events.
   * @param factoriesProperties File of properties for factories.
   * @param simProperties File of properties for simulator.
   * @param chordProperties File of properties for Chord.
   * @throws InitializationException if occur any error.
   */
  private void init(IScheduler timer) 
  		throws InitializationException {
  	network = GenericFactory.buildNetwork();
    this.timer = timer;    
	Results.clearStabRate();  	
    nodeHandles = new TreeMap();
    eventsProcessed = 0;
  }
  
  /**
   *  Executes the events at the opportune moment, controls the nodes, 
   *  while they join, go away, they fail,and handles to the messages 
   *  and the message queues
   *  @param steps number of steps that the simulation lasts 
   */  
  public void run (int steps) throws InitializationException {
  	for (int i=0; i< steps; i++)
  		simulate();
  	
  }
  
  /**
   * Serialize the actual state.
   *
   */
  public void saveState()
  {
      saveState(network);
  }
  
  /**
   * Serialize the specified network.
   */
  public static void saveState(Network network) {
	try {
     NoReplacingOutputStream out = new NoReplacingOutputStream(
             Properties.serializedOutputFile,
             Properties.serializedOutputFileReplaced);
	 ObjectOutputStream os = new ObjectOutputStream(out);
     System.out.println("NOTICE: saving actual state to file ["+out.getFilename()+"]");
     
	 os.writeObject (network);
	 os.close();
	}
	catch (Exception ex) {
	  ex.printStackTrace();
	} 
  }
  
  /**
   * Executes only one step of simulation.
   * @return return true if the simulation is not been finished. 
   */  
  public boolean simulate() throws InitializationException {
  	//parse the events at actual time
  	Vector events = timer.getEvents(time);
  	if (!events.isEmpty())
  		parseEvents(events);
  	
  	//simulate one time step
  	boolean moreSteps = network.simulate();
  	//updates the actual number of time step
  	time++; 	
  	//make some logs
  	if (time%1000==0) {
  		Logger.log("NetworkSimulator: Simulation time "+time+" and num nodes "+network.size(),Logger.EVENT_LOG);	          
  		Logger.log("NetworkSimulator: Ratio estabilitzacio "+Results.getStabRate(),Logger.EVENT_LOG);
  	}     

  	//return true only if there are steps to simulate
  	return (timer.hasNext() || 
  			moreSteps);
  }
  
  /**
   * Make a simple loop that make the stabilization of the actual
   * network. That is:
   * <pre>
   *     while (simulate());
   * </pre>
   * @throws InitializationException if occur any problem during simulation.
   */
  public void stabilize() throws InitializationException {
  	while(simulate());
  }

  /**
   * Joins a new node to the simulated network.
   *
   * @param node New node to add to the network.
   * @param bootstrap NodeHandle of the any node in the network
   */ 
  public void addNode(planet.commonapi.Node node, NodeHandle bootstrap) throws InitializationException {
  	network.joinNode(node,bootstrap);
  }

  public void printNodes() {
  	network.printNodes();
  }
  
  private NodeHandle getNodeHandle(Id id)
  {
      NodeHandle nh = (NodeHandle)nodeHandles.get(id);
      if (nh == null)
      {
          try{
              nh = GenericFactory.buildNodeHandle(id,true);
              nodeHandles.put(id,nh);
          } catch (Exception e)
          {
              Logger.log("Cannot build a NodeHandle for id =" + id,Logger.ERROR_LOG);
              System.exit(-1);
          }
      }
      return nh;
  }
  
  /**
   * Create and joins a new node to the simulated network.
   *
   * @param id Id of the new node
   * @param bootstrap Id of the any node in the network
   */ 
  public void addNode(Id id, Id bootstrap) throws InitializationException {
  	network.joinNode(GenericFactory.buildNode(id),getNodeHandle(bootstrap));
  }

  
  /**
   * Prepares all events to simulate from a events vector
   * @param events Vector of events   
   */ 
  public void parseEvents(Vector events) throws InitializationException {
    IEvent aEvent;

    Iterator it = events.iterator();
    while (it.hasNext()){
      aEvent = (IEvent) it.next();
      switch (aEvent.getType()){
        case Globals.JOIN:		parseJoin(aEvent);
          break;
        case Globals.LEAVE:		parseLeave(aEvent);
          break;
        case Globals.FAIL:     	parseFail(aEvent);
          break;

      }
      eventsProcessed ++;
    }
  }

  /**
   * Prepares the join event to simulate from a event interface
   * @param aEvent IEvent
   * @see planet.simulate.IEvent  
   */ 
  public void parseJoin (IEvent aEvent) throws InitializationException {
    Id id, target;

    id = aEvent.getFrom();
    target = aEvent.getTo();

    if (id == null || target == null)
    {
        throw new InitializationException("Join event loaded not contains the node Id or bootstrap Id.");
    }

    if (network.existNode(getNodeHandle(id))) {
      Logger.log("ERROR: Node Id to join by event already exits ["+id+"] and not loaded.",Logger.ERROR_LOG);
    } 
    else {
      try {
          addNode(id,target);   
      } catch (InitializationException e)
      {
          if (eventsProcessed == 0)
              throw new InitializationException("You have to ensure a network size equals to zero for a correct work with events",e);
          else
              throw e;
      }
    }
  }  
  
  /**
   * Prepares the leave event to simulate from a event interface
   * @param aEvent IEvent
   * @see planet.simulate.IEvent  
   */
  public void parseLeave(IEvent aEvent) throws InitializationException {
    Id id = aEvent.getFrom();
    if (id == null)
    {
        throw new InitializationException("Leave event loaded not contains the node Id.");
    }
    
    network.leaveNodes(new NodeHandle[]{getNodeHandle(id)});
  }

  /**
   * Prepares the fail event to simulate from a event interface
   * @param aEvent IEvent
   * @see planet.simulate.IEvent  
   */
  public void parseFail(IEvent aEvent) throws InitializationException {
    Id id = aEvent.getFrom();
    if (id == null)
    {
        throw new InitializationException("Fail event loaded not contains the node Id.");
    }

    network.failNodes(new NodeHandle[]{getNodeHandle(id)});
  }

  /**
   * Do nothing.
   * @see planet.simulate.ISimulator#stop()
   */
  public void stop() {
  }
  
  /**
   * Return the size of the network
   * @return size int of the number of nodes
   */
  public int getSizeNetwork() {    
	return network.size();
  }
  
  /**
   * Returns a reference of the internal network.
   * @return A reference of the internal network.
   */
  public Network getInternalNetwork() {
  	return network;
  }
}
