package planet.generic.commonapi.factory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.ObjectInputStream;

import planet.commonapi.Network;
import planet.commonapi.exception.InitializationException;
import planet.commonapi.factory.NetworkFactory;
import planet.commonapi.factory.NodeFactory;
import planet.util.Properties;

/**
 * This class allows the ability of build networks
 * of different topologies, size and Nodes. This implementation
 * offers network building with:
 * <ol>
 * <li>Any topology specified at planet.generic.commonapi.factory.Topology class.
 * <li>Any positive network size [0..INTEGER_MAX].
 * <li>Any current and future node implementation.
 * </ol>
 * 
 * @author <a href="mailto: jordi.pujol@estudiants.urv.es">Jordi Pujol</a>
 * 07-jul-2005
 */
public class NetworkFactoryImpl implements NetworkFactory {
    /**
     * The default Class reference for the current Network implementation.
     */
    protected Class network   = null;
	/**
	 * The default NodeFactory.
	 */
	protected NodeFactory nodeFactory = null;
	/**
	 * The default network size to use for build a network.
	 */
	protected int networkSize;
	/**
	 * The default topology to use for build a network.
	 */
	protected String topology = null;
	
    /**
     * Builds a NetworkFactoryImpl instance. Does nothing.
     * Requires the <b>setValues(...)</b> invokation to set the initial values.
     */
    public NetworkFactoryImpl() {}
    
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
     * @see planet.commonapi.factory.NetworkFactory#setValues(java.lang.Class, int, planet.commonapi.factory.NodeFactory, java.lang.String)
     */
    public NetworkFactory setValues(Class network, int size, NodeFactory nodeFactory, String topology)
        throws InitializationException
    {
        //setting values
        this.network     = network;
        this.networkSize = size;
        this.nodeFactory = nodeFactory;
        this.topology    = topology;
        
        //testing values
        if (!Topology.isValid(topology))
            throw new InitializationException("An unknown network topology is specified with value '"+topology+"'");
        if (Topology.SERIALIZED.equalsIgnoreCase(topology))
            Properties.activateSerializationAttributes();
        
        return this;
    }
    
	/**
	 * Builds a network with the actual specified properties. It is,
	 * NodeFactory, size of network, number of bits and topology.
	 * @see planet.commonapi.factory.NetworkFactory#buildNetwork()
	 * @return A network with the specified number of Nodes.
	 * @throws InitializationException if occurs any problem during
	 * the process to build the network.
	 */
	public Network buildNetwork() throws InitializationException {
		return _buildNetwork(this.networkSize,this.nodeFactory,this.topology);
	}
	
	/**
	 * Builds a network with the actual specified properties, but
	 * overwriting the number of nodes to build within.
	 * @see planet.commonapi.factory.NetworkFactory#buildNetwork(int)
	 * @param size The number of Nodes for the new Network
	 * @return A network with the specified <b>size</b> of Nodes.
	 * @throws InitializationException if occurs any problem during
	 * the process to build the network.
	 */
	public Network buildNetwork(int size) throws InitializationException {
		return _buildNetwork(size,this.nodeFactory,this.topology);
	}
	
	/**
	 * Builds a network with the specified <b>size</b> for the network, building it
	 * under this concrete <b>topology</b>. The rest of properties are used the
	 * actually ones in use.
	 * @see planet.commonapi.factory.NetworkFactory#buildNetwork(int, java.lang.String)
	 * @param size The number of Nodes for the new Network
	 * @param topology The network topology to constructs new network.
	 * @return A network with the specified <b>size</b> of Nodes.
	 * @throws InitializationException if occurs any problem during
	 * the process to build the network.
	 */
	public Network buildNetwork(int size, String topology) throws InitializationException {
		return _buildNetwork(size,this.nodeFactory,topology);
	}

	/**
	 * Builds a new network with the specified <b>size</b> and this concrete
	 * <b>nodeFactory</b>. The rest of properties are used the actually ones
	 * in use.
	 * @see planet.commonapi.factory.NetworkFactory#buildNetwork(int, planet.commonapi.factory.NodeFactory)
	 * @param size The number of Nodes for the new Network
	 * @param nodeFactory The NodeFactory to use to build Nodes for the new network.
	 * @return A network with the specified <b>size</b> of Nodes.
	 * @throws InitializationException if occurs any problem during
	 * the process to build the network.
	 */
	public Network buildNetwork(int size, NodeFactory nodeFactory) throws InitializationException {
		return _buildNetwork(size,nodeFactory,this.topology);
	}

	/**
	 * Builds a new network with the specified <b>size</b>, this concrete
	 * <b>nodeFactory</b> and network <b>topology</b>. 
	 * The rest of properties are used the actually ones in use.
	 * @see planet.commonapi.factory.NetworkFactory#buildNetwork(int, planet.commonapi.factory.NodeFactory, java.lang.String)
	 * @param size The number of Nodes for the new Network
	 * @param nodeFactory The NodeFactory to use to build Nodes for the new network.
	 * @param topology The network topology to constructs new network.
	 * @return A network with the specified <b>size</b> of Nodes.
	 * @throws InitializationException if occurs any problem during
	 * the process to build the network.
	 */
	public Network buildNetwork(int size, NodeFactory nodeFactory,
			String topology) throws InitializationException {
		return _buildNetwork(size,nodeFactory,topology);
	}

	/**
	 * Builds a network with the specified parameters. If the <b>topology</b> is 
     * Topology.SERIALIZED, it loads the network state from the a file,
     * found in Properties.SERIALIZATION_INPUT_FILE.
	 * @param size Number of nodes to build under in the network.
	 * @param nodeFactory NodeFactory to use to build new Nodes.
	 * @param topology Topology of the new network.
	 * @return An instance of a Network. This network is a deserialized Network 
     * if <b>topology</b> is a Topology.SERIALIZED, o a new instance in other 
     * case, with those parameters.
	 * @throws InitializationException if occurs any problem during the 
     * initialization process.
     * @see planet.generic.commonapi.factory.Topology
     * @see planet.util.Properties
	 */
	protected Network _buildNetwork(int size, NodeFactory nodeFactory, String topology) throws InitializationException {
        //loads a serialized state
		if (topology.equalsIgnoreCase(Topology.SERIALIZED)) {
			Network netToReturn = null;
			try {
				ObjectInputStream ois = new ObjectInputStream(new FileInputStream(Properties.serializedInputFile));
				netToReturn = (Network)ois.readObject();
				ois.close();
			} catch (FileNotFoundException e) {
				throw new InitializationException("Cannot find file '"+Properties.serializedInputFile+"'.",e);
			} catch (Exception e) {
				throw new InitializationException("Cannot load the serialized state of a network from the file '"+Properties.serializedInputFile+"'.",e);
			}
			return netToReturn;
		}
        
        //builds a new Network instance
		Network net = ((Network)GenericFactory.newInstance(network)).setValues(topology,nodeFactory);
		net.joinNodes(size);
		return net;
	}
}
