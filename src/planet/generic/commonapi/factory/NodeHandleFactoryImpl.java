package planet.generic.commonapi.factory;

import planet.commonapi.Id;
import planet.commonapi.NodeHandle;
import planet.commonapi.exception.InitializationException;
import planet.commonapi.factory.NodeHandleFactory;

/**
 * It is a specific implementation of NodeHandleFactory that permits
 * to build any class of NodeHandle with any type of Id. So, it requires
 * the Class reference for the related NodeHandle.
 * 
 * @author <a href="mailto: jordi.pujol@estudiants.urv.es">Jordi Pujol</a>
 * 07-jul-2005
 */
public class NodeHandleFactoryImpl implements NodeHandleFactory {
	/**
	 * Actual instance of Class for new NodeHandles.
	 */
	private Class nodeHandle = null;
	
	/**
     * Builds a NodeHandleFactoryImpl instance. Does nothing.
     * Requires the <b>setValues(...)</b> invokation.
	 */
	public NodeHandleFactoryImpl() {}

    /**
     * Sets the initial values for the NodeHandleFactory.
     * @param nodeHandle Class reference to build new NodeHandles.
     * @return The same instance once it has been updated.
     * @throws InitializationException if some error occurs during the
     * initialization process.
     * @see planet.commonapi.factory.NodeHandleFactory#setValues(java.lang.Class)
     */
    public NodeHandleFactory setValues(Class nodeHandle)
            throws InitializationException {
        this.nodeHandle = nodeHandle;
        return this;
    }
    
	/**
	 * Builds a NodeHandle with the actual implementation class of NodeHandle and
	 * specifieds <b>id</b> and <b>alive</b> flag.
	 * @param id Id of the related Node, to assign to the new instance of NodeNandle.
	 * @param alive true if the related Node is alive. false in other case.
	 * @return An instance of actual implementation class of NodeHandle.
	 * @see planet.commonapi.factory.NodeHandleFactory#buildNodeHandle(planet.commonapi.Id, boolean)
	 * @see planet.commonapi.Id Id
	 */
	public NodeHandle buildNodeHandle(Id id, boolean alive) throws InitializationException {
		return ((NodeHandle)GenericFactory.newInstance(nodeHandle)).setValues(id,alive);
	}
}
