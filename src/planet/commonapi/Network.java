package planet.commonapi;

import java.util.Random;

import planet.commonapi.exception.InitializationException;
import planet.commonapi.factory.NodeFactory;

/**
 * This interface abstacts any ring or network of Nodes. It pretends
 * contain all Nodes in the Network and allow to operate with the Nodes
 * with transparent manner.
 * <p>
 * Any future implementation must contain the no argument constructor.
 * </p>
 * @author <a href="mailto: jordi.pujol@estudiants.urv.es">Jordi Pujol</a>
 * 07-jul-2005
 */
public interface Network extends java.io.Serializable {
    
    /**
     * Sets the initial values for the new Network instance.
     * @param topology Desired network topology.
     * @param nodeFactory NodeFactory implementation to be used to build
     * the network.
     * @return The same instance once it has been updated.
     * @throws InitializationException if any error occurs during
     * the initialization process.
     */
    public Network setValues(String topology, NodeFactory nodeFactory) throws InitializationException;
    
	/**
	 * Joins the <b>node</b> to the ring.
	 * @param node Node to join to the ring.
	 */
	public void joinNode(Node node) throws InitializationException;
	
	/**
	 * Network joins a <b>node</b> with specified <b>bootstrap</b>.
	 * @param node Node to join to the network.
	 * @param bootstrap Node that joins the <b>node</b>.
	 * @throws InitializationException if occurs any problem during the join.
	 */
	public void joinNode(Node node, NodeHandle bootstrap) throws InitializationException;
	
	/**
	 * Generates <b>size</b> Nodes and joins them to the ring.
	 * @param size Number of Nodes to join to the ring.
	 * @return A number of actual simulated steps after join <b>size</b> nodes.
	 */
	public int joinNodes(int size) throws InitializationException;

	/**
	 * Generates <b>size</b> new Nodes joins them by any of the <b>bootstrap</b>s.
	 * @param size Number of nodes to joins to the network.
	 * @param bootstrap Array of NodeHandles to use as bootstrap. The implementation only 
	 * must ensure the use of them, or the maximum of them if the number of nodes
	 * to join is smaller than bootstrap.length.
	 * @throws InitializationException if any error has ocurred during the operation.
	 * @return A number of actual simulated steps after join <b>size</b> nodes.
	 */
	public int joinNodes(int size, NodeHandle bootstrap[]) throws InitializationException;
	
	/**
	 * Leave the ring the nodes which his NodeHandle appears in the array
	 * <b>nodes</b>.
	 * @param nodes Node's NodeHandles that must leave the network.
	 */
	public void leaveNodes(NodeHandle[] nodes) throws InitializationException;
	
	/**
	 * <b>nodes</b> shows all nodes NodeHandle that have failed.
	 * @param nodes Node's NodeHandles that must fail.
	 */
	public void failNodes(NodeHandle[] nodes) throws InitializationException;

	
	/**
	 * Register to all Nodes in this network the Applicaton specified at
	 * properties file.
	 * @see planet.commonapi.Application
	 * @see planet.commonapi.Node
	 */
	public void registerApplicationAll() throws InitializationException;
	
	/**
	 * Register to all Nodes whose NodeHandle appears in array Ids of <b>nodes</b>
	 * the Application specified at properties file.
	 * @param nodes NodeHandles of Nodes that must be registered the Application.
	 * @see planet.commonapi.Node
	 * @see planet.commonapi.Id
	 * @return The number of Nodes with the Application registered.
	 */
	public int registerApplication(NodeHandle[] nodes) throws InitializationException;
	
	/**
	 * Register the Application specified at properties file
	 * radomly to a maximum number of Nodes specified by <b>nodes</b>.
	 * @param nodes Number of Nodes to be register the Application.
	 * @see planet.commonapi.Node
	 * @see planet.commonapi.Id
	 */
	public void registerApplicationRandom(int nodes) throws InitializationException;
	
	/**
	 * Returns the topology of the actual network. It cannot be modified.
	 * @return The topology of the network.
	 */	
	public String getTopology();
	
	/**
	 * Returns the number of Nodes that contains the actual network.
	 * @return The number of Nodes that contains the actual network.
	 */
	public int size();
	
	/**
	 * Shows for System.out all information of all nodes,
	 * including its finger table.
	 *
	 */
	public void printNodes();
	
	/**
	 * Shows for each node, its Id, predecessor and successor.
	 */
	public void prettyPrintNodes();
	
	/**
	 * Runs the process of stabilization while the network not has
	 * been stabilized. The number of steps that is running are
	 * undeterministic.
	 * @return Actual number of simulated steps after stabilization.
	 */
	public int stabilize();
	
	/**
	 * Runs the process to simulate one time step. This process is
	 * composed for:
	 * <ol>
	 * <li>Process all nodes one step.</li>
	 * <li>Send all messages generated at this moment.</li>
	 * <li>Remove specified nodes.</li>
	 * </ol>
	 */
	public boolean simulate();
	
	/**
	 * Runs the process to simulate a total number of <b>steps</b>.
	 * This process is identical to the following code:
	 * <pre>
	 * for (int i=0; i < steps; i++) {
	 *     simulate();
	 * }
	 * </pre>
	 * @param steps Number of steps to simulate.
	 * @return Number of actual stabilization steps after run <b>steps</b> steps.
	 */
	public int run(int steps);
	
	/**
	 * Returns a randomly selected node of actual network.
	 * @param r Generator of random numbers.
	 * @return A node radomly selected.
	 */
	public Node getRandomNode(Random r);
	
	/**
	 * Inform if exist on the network one node with NodeHandle <b>node</b>.
	 * @param node NodeHandle of the node to be search.
	 * @return true if and only if exist one node with the same <b>node</b> NodeHandle.
	 */
	public boolean existNode(NodeHandle node);
	
	/**
	 * Gets a reference of some application on any node of the network.
	 * @param appId Application identification for searching it.
	 * @return A reference of an existing application on any node.
	 */
	public Application getRandomApplication(String appId);
	
	/**
	 * Gets an Iterator to get all nodes.
	 * @return An Iterator to iterate over nodes on the network.
	 */
	public java.util.Iterator iterator();
	
	/**
	 * Gets the actual number of simulated steps.
	 * @return actual number of simulated steps.
	 */
	public int getSimulatedSteps();
	
	/**
	 * Evaluates the proximity between the two nodes.
	 * @return The proximity between the two nodes.
	 */
	public int getProximity(NodeHandle nodeA, NodeHandle nodeB);
}
