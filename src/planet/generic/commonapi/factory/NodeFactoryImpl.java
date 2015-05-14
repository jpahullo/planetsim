package planet.generic.commonapi.factory;

import planet.commonapi.Id;
import planet.commonapi.Node;
import planet.commonapi.exception.InitializationException;
import planet.commonapi.factory.IdFactory;
import planet.commonapi.factory.NodeFactory;

/**
 * This is a specific implementation of NodeFactory that permits
 * build any class of Node with any type of Id.
 * @author <a href="mailto: jordi.pujol@estudiants.urv.es">Jordi Pujol</a>
 * 07-jul-2005
 */
public class NodeFactoryImpl implements NodeFactory {
	/**
	 * Actual instance of IdFactory.
	 */
	private IdFactory idFactory = null;
	/**
	 * Actual instance of Class for new Nodes.
	 */
	private Class node = null;
	
	
	
	/**
     * Builds an uninitialized NodeFactoryImpl. Does nothing.
     * Requires the <b>setValues(...)</b> method invokation.
	 */
	public NodeFactoryImpl() {}
    
    /**
     * Sets the initial values for this NodeFactory.
     * @param idFactory IdFactory to use to build all required Ids.
     * @param node Class reference of the current Node implementation.
     * @return The same instance once it has been updated.
     * @throws InitializationException if some error occurs during the
     * initialization process.
     * @see planet.commonapi.factory.NodeFactory#setValues(planet.commonapi.factory.IdFactory, java.lang.Class)
     */
    public NodeFactory setValues(IdFactory idFactory, Class node) throws InitializationException {
		this.idFactory = idFactory;
		this.node = node;
        return this;
	}
	
	
	/**
	 * Builds a NodeImpl with the actual IdFactory and class for the nodes.
	 * @see planet.commonapi.factory.NodeFactory#buildNode()
	 * @see planet.commonapi.factory.IdFactory IdFactory
	 * @see planet.commonapi.Id Id
	 * @see planet.commonapi.Node Node
	 */
	public Node buildNode() throws InitializationException {
		return buildNode(idFactory.buildId());
	}

	/**
	 * Builds a node with the actual implementation class of NodeImpl and the
	 * specified <b>id</b>.
	 * @param id Id to assign to the new instance of NodeImpl.
	 * @return An instance of actual implementation class of NodeImpl.
	 * @see planet.commonapi.factory.NodeFactory#buildNode(planet.commonapi.Id)
	 * @see planet.commonapi.factory.IdFactory IdFactory
	 * @see planet.commonapi.Id Id
	 * @see planet.commonapi.Node Node
	 */
	public Node buildNode(Id id) throws InitializationException {
        return ((Node)GenericFactory.newInstance(node)).setValues(id);
	}
}
