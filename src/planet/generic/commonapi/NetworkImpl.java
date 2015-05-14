package planet.generic.commonapi;

import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectStreamException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;
import java.util.Stack;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Vector;

import planet.commonapi.Application;
import planet.commonapi.Network;
import planet.commonapi.Node;
import planet.commonapi.NodeHandle;
import planet.commonapi.RouteMessage;
import planet.commonapi.behaviours.BehavioursRoleSelector;
import planet.commonapi.exception.InitializationException;
import planet.commonapi.factory.NodeFactory;
import planet.generic.commonapi.behaviours.BehavioursPropertiesImpl;
import planet.generic.commonapi.factory.GenericFactory;
import planet.simulate.Globals;
import planet.simulate.Logger;
import planet.simulate.Results;
import planet.util.Properties;
import planet.util.Queue;
import planet.util.QueueFull;
 
/**
 * This implementation of Network interface pretends to abstract
 * any network, based with the actual commonapi, of course. The
 * time is abstracted to discrete simulation steps.
 * <br><br>
 * Operationals modes for this network implementation:
 * 
 * <ol>
 * <li>Accessing directly to an instance of this class, and adding
 * nodes, leaving them (or part of them), etc. without time certainty.
 * In some cases it is usefull. In this case, events cannot be treated.</li>
 * <li>Generating a new instance of NetworkSimulator and invoking ONLY AND
 * ONLY IF the NetworkSimulator methods, with time certainty and treatment
 * of events.</li>
 * </ol> 
 * 
 * @author <a href="mailto: jordi.pujol@estudiants.urv.es">Jordi Pujol</a>
 * @author <a href="mailto: marc.sanchez@urv.net">Marc Sanchez</a>
 * 07-jul-2005
 */
public class NetworkImpl implements Network {
	/**
	 * To contains (NodeHandle,Node) pairs for all nodes in the network.
	 */
	private TreeMap nodes;
	/**
	 * Containts all nodes to remove from the network. 
	 */
	private Stack toRemove;
	/**
	 * NodeFactory implementation which permits build new nodes.
	 */
	private NodeFactory nodeFactory;
	/**
	 * Defines the topology of the actual network.
	 */
	private String topology;
	/**
	 * Random generator to use to generate random indexes.
	 */
	private Random randomGenerator;
	/**
	 * The actual size of the network. The number of contained nodes.
	 * It is required for when there are leaving nodes, for showing
	 * the real size on any time.
	 */
	private int size;
	/**
	 * Shows the level of stabilization of the network.
	 */
	private int stabLevel;
	/**
	 * Shows the number of actually simulated steps.
	 */
	private int totalSteps;
	
	/**
	 * Initialize the network with no nodes and wihtout simulation steps.
	 */
	public NetworkImpl() {
		nodes = new TreeMap();    //to contain all nodes
		toRemove = new Stack();
		randomGenerator = new Random(System.currentTimeMillis());
		size = 0;
		stabLevel = 0;
		totalSteps = 0;
	}

    /**
     * Sets the initial values for the new Network instance.
     * @param topology Desired network topology.
     * @param nodeFactory NodeFactory implementation to be used to build
     * the network.
     * @return The same instance once it has been updated.
     * @throws InitializationException if any error occurs during
     * the initialization process.
     * @see planet.commonapi.Network#setValues(java.lang.String, planet.commonapi.factory.NodeFactory)
     */
    public Network setValues(String topology, NodeFactory nodeFactory) throws InitializationException
    {
        this.topology    = topology;
        this.nodeFactory = nodeFactory;
        if (nodeFactory == null)
            throw new InitializationException("The nodeFactory parameter is null");
        return this;
    }

	
	/**
	 * Add the <b>node</b> to the actual network. Its bootstrap can be:
	 * <ol>
	 * <li><b>node</b>: If there aren't nodes to the network.</li>
	 * <li><b>Any node</b>: Any node in the network if just exists nodes
	 * to the network.</li>
	 * </ol>
	 * Runs the stabilization process after joins the node.
	 * @see planet.commonapi.Network#joinNode(planet.commonapi.Node)
	 * @param node Node to add to the network.
	 */
	public void joinNode(Node node) throws InitializationException {
		if (nodes.size()==0) {
			nodes.put(node.getLocalHandle(),node); //it is its own bootstrap
			node.join(node.getLocalHandle());
		} else {
			Node boot = (Node)nodes.values().iterator().next(); //constant view of Id of nodes
			node.join(boot.getLocalHandle());
		}
		size++;
	}
	
	/**
	 * Adds <b>node</b> to the network, using the node with
	 * Id <b>bootstrap</b> to enter it. Runs the stabilization process after joins
	 * this node.
	 * @see planet.commonapi.Network#joinNode(planet.commonapi.Node, planet.commonapi.NodeHandle)
	 * @param node Node to add to the network.
	 * @param bootstrap Id of the node to use as bootstrap for <b>node</b>.
	 * @throws InitializationException if the <b>bootstrap</b> not exists in the network.
	 */
	public void joinNode(Node node, NodeHandle bootstrap) throws InitializationException {
		//test if bootstrap exists
		if ((nodes.size()==0 && !node.getId().equals(bootstrap.getId())) || (nodes.size()>0 && !nodes.containsKey(bootstrap))) 
        {
			throw new InitializationException("The bootstrap Id ["+ bootstrap +"] not exists within the network.");
        }
		//adds the node to Hash
		nodes.put(node.getLocalHandle(),node);
		//joins node with this bootstrap
		node.join(bootstrap);
		size++;
	}
	/**
	 * Add <b>size</b> nodes to the actual network. Their class and their Ids
	 * depends of the actual configuration of different factories. As bootstrap
	 * for those nodes are used any of the existing nodes or the firt of them 
	 * if exists no nodes. Runs the stabilization process for each node.
	 * @see planet.commonapi.Network#joinNodes(int)
	 * @param size Number of nodes to add to the network.
	 * @throws InitializationException if occur some problem with building nodes.
	 * @return A number of actual simulated steps after join <b>size</b> nodes.
	 */
	public int joinNodes(int size) throws InitializationException {
        if (Properties.overlayWithBehaviours)
			return joinNodesWithBehaviours(size);
		else
            return joinNodesNormally(size);
	}
	
    /**
     * Add <b>size</b> nodes to the network.
     * @param size Number of nodes to be added.
     * @return The number of simulated steps.
     * @throws InitializationException if any error has ocurred.
     */
    private int joinNodesWithBehaviours(int size) throws InitializationException {
        if (size <= 0) return 0;
        
        TreeSet nodeSet = new TreeSet();
        Vector  network = new Vector(); 
        for (int whichNode = 0; whichNode < size; whichNode++) {
            Node toJoin = nodeFactory.buildNode(); 
            nodeSet.add(toJoin.getLocalHandle());
            network.add(toJoin);
        }

        // BehaviourRoleSelector
        BehavioursRoleSelector behSelector = GenericFactory.buildBehavioursRoleSelector();
        
        Set badSet =  behSelector.select(
                        nodeSet.iterator(),                         // Iterator over the whole Network 
                        ((BehavioursPropertiesImpl)Properties.behavioursPropertiesInstance).faultyNodes/100, // Percentage of Bad nodes.
                        ((BehavioursPropertiesImpl)Properties.behavioursPropertiesInstance).maliciousDistributionAsInt  // Distrbution of Bad nodes.
                      );
        
        
        Iterator it = network.iterator();
        Node toJoin = (Node) it.next();  // First Node to be inserted
        NodeHandle bootstrap = null;
        if (this.size==0)
        {
            bootstrap = toJoin.getLocalHandle(); // itself
        } else {
            bootstrap = (NodeHandle)nodes.keySet().iterator().next(); //another node already existing
        }
        toJoin.setGoodRole(badSet==null || !badSet.contains(toJoin.getId()));
        toJoin.join(bootstrap);
        nodes.put(toJoin.getLocalHandle(),toJoin);
        Vector boots = new Vector();    //the set of possible bootstrap nodes
        boots.add(bootstrap);
        this.size++;
        // The Remaining Nodes Ready For Bootstrapping
        while (it.hasNext())
        {
            toJoin = (Node) it.next();
            toJoin.setGoodRole(badSet==null || !badSet.contains(toJoin.getId()));
            bootstrap = (NodeHandle)boots.get(randomGenerator.nextInt(boots.size()));
            toJoin.join(bootstrap);
            nodes.put(toJoin.getLocalHandle(),toJoin);
            boots.add(toJoin.getLocalHandle());
            run(Properties.simulatorSimulationSteps);
            this.size++;
        }
        return totalSteps;
    }
    
    /**
     * Adds the <b>size</b> nodes to the current network.
     * @param size Total number of nodes to add.
     * @return The last simulated step number.
     * @throws InitializationException if any error occurs during the
     * joining process.
     */
	private int joinNodesNormally(int size) throws InitializationException {
		if (size>0) {
			Node toJoin = null;
			NodeHandle bootstrap = null;
			Vector boots = new Vector();
			//if exists no nodes, add one
			if (this.size==0) {
				//create new node
				toJoin = nodeFactory.buildNode();
				nodes.put(toJoin.getLocalHandle(),toJoin);
				//updates number of nodes to join
				size--;
				this.size++;
				bootstrap = toJoin.getLocalHandle();
				toJoin.join(bootstrap);
			} else {
				bootstrap = (NodeHandle)nodes.keySet().iterator().next();
			}
            
			boots.add(bootstrap);
			//add the rest of nodes
			for (int i = 0; i<size; i++) {
				//create new node
				toJoin = nodeFactory.buildNode();
				//put it into hashmap
				nodes.put(toJoin.getLocalHandle(),toJoin);
				toJoin.join(bootstrap);
				boots.add(bootstrap);
				bootstrap = (NodeHandle)boots.get(randomGenerator.nextInt(boots.size()));
				run(Properties.simulatorSimulationSteps);
				this.size++;
			}
		}
		return totalSteps;
	}
	
	/**
	 * Add <b>size</b> nodes to the actual network. Their class and their Ids
	 * depends of the actual configuration of different factories. As bootstrap
	 * for those nodes are used the Ids that appears at <b>bootstrap</b>. If
	 * <b>size</b> > <b>bootstrap.length</b>, new nodes are distributed
	 * radomly for them. 
	 * @see planet.commonapi.Network#joinNodes(int, planet.commonapi.NodeHandle[])
	 * @param size Number of nodes to add to the network.
	 * @param bootstrap NodeHandles of nodes to use as bootstrap for new nodes.
	 * @throws InitializationException if occur some problem with building nodes.
	 * @return A number of actual simulated steps after join <b>size</b> nodes.
	 */
	public int joinNodes(int size, NodeHandle[] bootstrap) throws InitializationException {
		Node toJoin = null;
		NodeHandle boot = null;
		int index = 0;
		for (int i = 0; i<size; i++) {
			//create new node
			toJoin = nodeFactory.buildNode();
			//put it to hashmap
			nodes.put(toJoin.getLocalHandle(),toJoin);
			//reinitialize the iterator if it has no more elements
			if (index==bootstrap.length)
				index = 0;
			boot = bootstrap[index];
			toJoin.join(boot);
			run(Properties.simulatorSimulationSteps);
			this.size++;
			index++;
		}
		return totalSteps;
	}
	
	/**
	 * Leave theese <b>nodes</b> for the network. If an exception is thrown,
	 * it method ensures that the existing Ids has been leaved. Runs its
	 * stabilization process at the end of all leaves. 
	 * @see planet.commonapi.Network#leaveNodes(planet.commonapi.NodeHandle[])
	 * @param nodes Ids for nodes to leave.
	 * @throws InitializationException if exists some Id in the array 
	 * as parameter that not exists in the network. The message includes
	 * which are of them. 
	 */
	public void leaveNodes(NodeHandle[] nodes) throws InitializationException {
		Vector unknownNodes = new Vector(0,1);
		Set nodesId = this.nodes.keySet(); //constant view of Id of nodes
		for (int i=0; i< nodes.length; i++) {
			if (nodesId.contains(nodes[i])) {
				//update its predecessor and successor
				((Node)this.nodes.get(nodes[i])).leave();
			} else
				unknownNodes.add(nodes[i]);
		}
		if (unknownNodes.size()>0)
			throw new InitializationException ("Not exist some Ids in Id[]. They are followings: "+unknownNodes.toArray());
	}
	
	/**
	 * Runs the stabilization process after each fail node.
	 * @see planet.commonapi.Network#failNodes(planet.commonapi.NodeHandle[])
	 */
	public void failNodes(NodeHandle[] nodes) throws InitializationException {
		for (int i=0; i < nodes.length; i++) {
			((Node)this.nodes.get(nodes[i])).fail();
		}
	}
	
	/**
	 * Register the Application <b>app</b> to all nodes existing
	 * at the underlying network.
	 * @see planet.commonapi.Network#registerApplicationAll()
	 */
	public void registerApplicationAll() throws InitializationException {
		Iterator it = nodes.values().iterator();
		Node node = null;
		Application app = null;
		while (it.hasNext()) {
			node = (Node)it.next();
			app = GenericFactory.buildApplication();
			node.registerApplication(app,app.getId());
		}
	}
	
	/**
	 * Register the Application specified at properties file
	 * to the specified <b>nodes</b> by theirs Ids.
	 * @see planet.commonapi.Network#registerApplication(planet.commonapi.NodeHandle[])
	 * @param nodes NodeHandles of those nodes to register Application <b>app</b>.
	 * @return The number of Nodes with the Application registered. If all that's ok,
	 * the value returned would have be the same that nodes.length.
	 */
	public int registerApplication(NodeHandle[] nodes) throws InitializationException {
		Node node = null;
		Application app = null;
		int registered = 0;
		for (int i=0; i<nodes.length; i++) {
			node = (Node)this.nodes.get(nodes[i]);
			if (node!=null) {
				app = GenericFactory.buildApplication();
				node.registerApplication(app,app.getId());
				registered++;
			}
		}
		return registered;
	}
	
	/**
	 * Register the Application <b>app</b> to radomly at the number of Nodes
	 * <b>nodes</b>. If <b>nodes</b> is equals or greater than number of nodes
	 * in the network, his effects are the same of <b>registerApplicationAll(Aplication)</b>.
	 * @see planet.commonapi.Network#registerApplicationRandom(int)
	 * @param nodes Number of nodes to radomly select to register the 
	 * Application <b>app</b>.
	 */
	public void registerApplicationRandom(int nodes) throws InitializationException {
		Set nodesId = this.nodes.keySet(); //constant view of Id of nodes
		NodeHandle[] ids = (NodeHandle[])nodesId.toArray();
		Node node = null;
		if (nodes >= this.nodes.size() ) {
			registerApplicationAll();
		} else {
			int[] selectedIds = makeRandomIndexes(nodes);
			Application app   = null;
			for (int i=0; i < nodes; i++) {
				node = (Node)this.nodes.get(ids[selectedIds[i]]);
				app = GenericFactory.buildApplication();
				node.registerApplication(app,app.getId());
			}
		}
	}
	
	/**
	 * Returns an array of indexes in range (0..number of nodes) to use
	 * to identify which nodes to select.
	 * @param max Number of indexes generate.
	 * @return An array of indexes with values at range (0..number of nodes) with
	 * a size of <b>max</b> elements.
	 */
	protected int[] makeRandomIndexes (int max) {
		HashSet indexes = new HashSet(max);
		int maxValue = nodes.size();
		while (indexes.size() < max) {
			indexes.add(new Integer(randomGenerator.nextInt(maxValue)));
		}
		Integer[] integerIndexes = (Integer[])indexes.toArray();
		int[] intIndexes = new int[max];
		for (int i=0; i<max; i++) {
			intIndexes[i] = integerIndexes[i].intValue();
		}
		return intIndexes;
	}
	
	/**
	 * Gets the actual topology of the underlying network.
	 * @see planet.commonapi.Network#getTopology()
	 * @return A String defining the actual topology of the network.
	 * @see planet.generic.commonapi.factory.Topology
	 */
	public String getTopology() {
		return topology;
	}
	
	/* ***************** DE/SERIALIZE NETWORKIMPL ****************/
	/**
	 * Serialize this instance through the <b>stream</b>. It is 
	 * responsible to generate a correct serialized state.
	 * @param stream ObjectOutputStream to save the actual state.
	 * @throws IOException if occurs any problem during serialization.
	 */
	private void writeObject(java.io.ObjectOutputStream stream)
    throws IOException {
		//serialized necessary attributes
	 	stream.writeObject(this.nodeFactory);
	 	stream.writeObject(this.nodes);
	 	stream.writeObject(this.toRemove);
	 	stream.writeObject(this.randomGenerator);
	 	stream.writeObject(new Integer(totalSteps));
	}
	
	/**
	 * Reads a serialized state of 
	 * @param stream
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	private void readObject(java.io.ObjectInputStream stream)
    throws IOException, ClassNotFoundException {
		//load serialized attributes
	 	this.nodeFactory = (NodeFactory) stream.readObject();
	 	this.nodes = (TreeMap) stream.readObject();
	 	this.toRemove = (Stack) stream.readObject();
		this.randomGenerator = (Random) stream.readObject();
		this.totalSteps = ((Integer)stream.readObject()).intValue();

	 	//generate rest of attributes
	 	this.topology = Properties.factoriesNetworkTopology;
	 	this.size = nodes.size();
	 	this.stabLevel = 0;
	}

	/**
	 * This method is invoked when cannot read the version of serialized objects 
	 * from the instance of network. In this case, an InvalidObjectException is thrown.
     */
	private void readObjectNoData() 
    throws ObjectStreamException {
	 	throw new InvalidObjectException("Cannot deserialize the network state.");
	}
	
	/* *************************** PRINTING BLOCK *******************************/
	
	/**
	 * Returns the actual number of nodes to the network.
	 * @see planet.commonapi.Network#size()
	 * @return The actual number of nodes to the network.
	 */
	public int size() {
		return size;
	}
	
	/**
	 * Shows for each Node its printNode(). 
	 */
	public void printNodes() {
		Node aNode = null;
	  	Collection c = nodes.values();
	  	Iterator it3 = c.iterator();
	  	while (it3.hasNext()){
	  		aNode = (Node)it3.next();
	    	aNode.printNode();
	  	}
	}
	
	/**
	 * Shows only the owner Id, the successor and predecessor
	 * for each Node.
	 */
	public void prettyPrintNodes() {
		Node aNode = null;
		Collection c = nodes.values();
		Iterator it3 = c.iterator();
		while (it3.hasNext()){
			aNode = (NodeImpl)it3.next();
			aNode.prettyPrintNode();
		}
	}
	
	/* ********************************  SIMULATION BLOCK **************************/
	
	/**
	 * Simulate a total number <b>steps</b> steps.
	 * @see planet.commonapi.Network#run(int)
	 * @param steps Number of steps to simulate.
	 * @return Number of actual stabilization steps after run <b>steps</b> steps.
	 */
	public int run(int steps) {
		for (int i=0; i< steps; i++) {
			simulate();
		}
		return totalSteps;
	}
	
	/**
	 * Simulate one step. 
	 * @see planet.commonapi.Network#simulate()
	 * @return true if the simulation process must to continue for
	 * to finish the stabilization.
	 */
	public boolean simulate() {
	  	//process all nodes
		boolean toContinue = process();
	  	//send all messages
		sendMessages();
	  	//remove all pending nodes to be deleted
		removeNodes();
		totalSteps++;
		//to update actual step in Logger utility
	  	Logger.setStep(totalSteps);
		return (toContinue                     //overlay condition 
				|| Results.getTraffic() > 0);  //exist application messages??

	}
	/**
	 * Process all nodes one step at this network.
	 * @return true if continue the simulation. false in other case.
	 */
	protected boolean process() {
		Iterator it = iterator();
		Node aNode = null;
		//informs that any node requires continue the stabilization process.
		boolean toContinue = false; 
		//Each node process incoming messages and handles additional 
		//stabilization operations
		while (it.hasNext()){
		  aNode = (Node)it.next();
		  // always process the node
		  toContinue = toContinue | aNode.process(this.totalSteps);
		  if (!aNode.isAlive()) { //the node has failed or leaved??
		  	toRemove.add(aNode.getLocalHandle());
		  }
		}
		return toContinue;
	}

	/**
	 * Sends all message pending to be delivered by all nodes.
	 */
	protected boolean sendMessages() {
		Iterator it = iterator();
		Node aNode = null;
		Queue messages = null;
		boolean toContinue = false;
		// The simulator moves outgoing messages to target's incoming node queues
		while (it.hasNext()){
		  aNode = (Node)it.next();
		  messages = aNode.outMessages();
		  toContinue = toContinue | (messages==null || messages.size() > 0);
		  send(messages);
		}
		return toContinue;
	}
	
	/**
	 * Takes all messages of queue and send to all destination nodes. If any
	 * destination of any message is not found, it is returned with mode sets
	 * to Globals.ERROR.
	 * 
	 * @param messages Queue with messages to send
	 * @see planet.util.Queue
	 * @see planet.simulate.Globals Globals
	 */
	protected void send (Queue messages) {
		//There are messages to process??
		if (messages==null) return;
		
		RouteMessage aMessage;
		NodeHandle from;
		NodeHandle target;
		Node aNode;
		int processed = 0;
		while (!messages.isEmpty() && processed < Properties.simulatorProcessedMessages){
			aMessage = (RouteMessage) messages.remove();

			target = aMessage.getNextHopHandle();
			if (target!=null && nodes.containsKey(target)) {
				aNode = (Node)nodes.get(target);
				
				try {
					aNode.receive(aMessage);
				} catch (QueueFull ex){
					Logger.log("Incoming queue of Node "+target+" is full",Logger.EVENT_LOG);
					GenericFactory.freeMessage(aMessage);
				}
			} else {
				Logger.log("Target of message ["+aMessage+"] is not found.",Logger.MSG_LOG);
				from = aMessage.getSource();
				if (nodes.containsKey(from)){
					aNode = (Node)nodes.get(from);
					aMessage.setMode(Globals.ERROR);
					try {
						NodeHandle source = aMessage.getSource();
						aMessage.setSource(aMessage.getDestination());
						aMessage.setDestination(source);
						aMessage.setNextHopHandle(source);
						aNode.receive(aMessage);
					} catch (QueueFull ex) {
						Logger.log("Incoming queue of Node "+from+" is full",Logger.EVENT_LOG);
						GenericFactory.freeMessage(aMessage);
					}
				}
			}
			processed ++;
		}
	}
	
	/**
	 * Removes ready nodes to be deleted.
	 */ 
	private void removeNodes() {
		NodeHandle node = null;
		while (!toRemove.isEmpty()){
			node = (NodeHandle)toRemove.pop();
			nodes.remove(node);
			size--;
		}
	}
	
	
	/**
	 * Returns a randomly selected node of actual network.
	 * @param r Generator of random numbers.
	 * @return A node radomly selected.
	 * @see planet.commonapi.Network#getRandomNode(java.util.Random)
	 */
	public Node getRandomNode(Random r) {
		Object[] values = nodes.values().toArray();
		int x = r.nextInt(values.length);
		return (Node)values[x];
	}
	
	
	/**
	 * Inform if exist on the network one node with NodeHandle <b>node</b>.
	 * @param node NodeHandle of the node to be search.
	 * @return true if and only if exist one node with the same <b>node</b> NodeHandle.
	 * @see planet.commonapi.Network#existNode(planet.commonapi.NodeHandle)
	 */
	public boolean existNode(NodeHandle node) {
		return nodes.containsKey(node);
	}
	
	/**
	 * Stabilize the network.
	 * @see planet.commonapi.Network#stabilize()
	 */
	public int stabilize() {
		this.stabLevel = 0;
		while (simulate());
		return totalSteps;
	}
	
	
	/**
	 * Gets a reference of some application on any node of the network.
	 * @param appId Application identification for searching it.
	 * @return A reference of an existing application on any node or null
	 * if there is not an Application with the <b>appId</b>.
	 * @see planet.commonapi.Network#getRandomApplication(java.lang.String)
	 */
	public Application getRandomApplication(String appId) {
		Node randomNode = this.getRandomNode(randomGenerator);
		Application[] apps = randomNode.getRegisteredApplications();
		boolean found = false;
		int i=0;
		for (; i<apps.length && !found;i++) {
			found = apps[i].getId().equals(appId);
		}
		//update to correct position
		i--;
		if (!found) 
			return null;
		return apps[i];
	}
	
	/**
	 * Builds an Iterator to iterate over all nodes of the network.
	 * @return An Iterator for all nodes on the network.
	 */
	public Iterator iterator() {
		return new NetworkIterator(nodes);
	}
	
	/**
	 * Gets the actual number of simulated steps.
	 * @see planet.commonapi.Network#getSimulatedSteps()
	 * @return actual number of simulated steps
	 */
	public int getSimulatedSteps() {
		return totalSteps;
	}
	
	/**
	 * This class implements the java.util.Iterator interface and is backed
	 * up by TreeMap that contains all existing nodes on the network.
	 * <br><br>
	 * It use the iterator of the TreeMap key set to iterate over all instances
	 * of the nodes, and returns the corresponding Node. It also implements
	 * the remove() method, that uses the same method of the key set iterator.
	 * This last method ensures to remove the required entry on to the TreeMap. 
	 * 
	 * @author Jordi Pujol
	 */
	public class NetworkIterator implements java.util.Iterator {
		/**
		 * HashMap that contains all Nodes to iterate.
		 */
		private TreeMap nodes = null;
		/**
		 * Iterator of keys existing over the HashMap.
		 */
		private Iterator keys = null;
		
		/**
		 * Initialize the key set iterator over the HashMap.
		 * @param nodes
		 */
		public NetworkIterator (TreeMap nodes) {
			this.nodes = nodes;
			this.keys = this.nodes.keySet().iterator();
		}

		/**
		 * Evaluate if there are more nodes.
		 * @see java.util.Iterator#hasNext()
		 */
		public boolean hasNext() {
			return keys.hasNext();
		}

		/**
		 * Returns the next Node.
		 * @see java.util.Iterator#next()
		 */
		public Object next() {
			return nodes.get(keys.next());
		}

		/**
		 * Remove the actual element of the HashMap.
		 * @see java.util.Iterator#remove()
		 */
		public void remove() {
			keys.remove();
		}
		
	}

	/**
	 * Always returns 1 (one).
	 * @see planet.commonapi.Network#getProximity(planet.commonapi.NodeHandle, planet.commonapi.NodeHandle)
	 * @param nodeA First node to evaluate.
	 * @param nodeB Second node to evaluate.
	 * @return The distance between theese two nodes (in this implementation, always one).
	 */
	public int getProximity(NodeHandle nodeA, NodeHandle nodeB) {
		return 1;
	}
}
