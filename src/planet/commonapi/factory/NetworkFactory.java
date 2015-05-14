package planet.commonapi.factory;

import planet.commonapi.*;
import planet.commonapi.exception.InitializationException;

/**
 * This interface is focused to allow the ability of build networks
 * of different topologies, size and Nodes, using the factory method
 * pattern design. 
 * <br><br>
 * The future implementations
 * must use the values specified at <b>setValues(...)</b> method as the
 * default values to build networks. For all non specified values into the 
 * <b>buildNetwork(...)</b> methods you will use the default ones. Each
 * Network implementation will interprete the actual <b>network topology</b> 
 * value. The default interpreted network topologies appear at
 * planet.generic.commonapi.factory.Topology class.
 * 
 * @author <a href="mailto: jordi.pujol@estudiants.urv.es">Jordi Pujol</a>
 * 07-jul-2005
 */
public interface NetworkFactory {
	
    /**
     * Sets the initial values for the NetworkFactory instance with the 
     * specified values.
     * @param network The Class reference for the current Network implementation.
     * @param size Desired network size.
     * @param nodeFactory The NodeFactory implementation to be used.
     * @param topology Desired network topology.
     * @return The same instance once it has been updated.
     * @throws InitializationException if any error occurs during the 
     * initialization process.
     */
    public NetworkFactory setValues(Class network, int size, NodeFactory nodeFactory, String topology)
        throws InitializationException;
    
	/**
	 * Generates a new Network with the actual size of the Network. The class
	 * of Nodes is specified by the actual NodeFactory.
	 * @return A new instance of Network with the number of nodes specified.
	 */
	public Network buildNetwork()
		throws InitializationException;
	
	/**
	 * Generates a new Network with the <b>size</b> of Nodes specified
	 * by parameter. The class of Nodes is specified by the actual NodeFactory.
	 * @param size Number of Nodes to generate in the network.
	 * @return A new instance of Network with the specified <b>size</b>.
	 */
	public Network buildNetwork(int size)
		throws InitializationException;
	
	/**
	 * Generates a new Network with the <b>size</b> of Nodes specified
	 * by parameter. The class of Nodes is specified by <b>nodeFactory</b>.
	 * @param size Number of Nodes to generate in the network.
	 * @param nodeFactory NodeFactory to use to build Nodes for the new network.
	 * @return A new instance of Network with the specified <b>size</b>.
	 */
	public Network buildNetwork(int size, NodeFactory nodeFactory)
		throws InitializationException;
	
	/**
	 * Generates a new Network with the <b>size</b> of Nodes specified
	 * by parameter. The class of Nodes is the obtained by the default nodeFactory.
	 * The <b>topology</b> specifies the network topology for the new network. 
	 * @param size Number of Nodes to generate in the network.
	 * @param topology The network topology for the new network.
	 * @return A new instance of Network with the specified <b>size</b> with
	 * the specified <b>topology</b>.
	 */
	public Network buildNetwork(int size, String topology)
		throws InitializationException;
	
	/**
	 * Generates a new Network with the <b>size</b> of Nodes specified
	 * by parameter. The class of Nodes is specified by <b>nodeFactory</b>.
	 * The <b>topology</b> specifies the network topology for the new network.
	 * @param size Number of Nodes to generate in the network.
	 * @param nodeFactory NodeFactory to use to build Nodes for the new network.
	 * @param topology The network topology for the new network.
	 * @return A new instance of Network with the specified <b>size</b> with
	 * the specified <b>topology</b>.
	 */
	public Network buildNetwork(int size, NodeFactory nodeFactory, String topology)
		throws InitializationException;
}
