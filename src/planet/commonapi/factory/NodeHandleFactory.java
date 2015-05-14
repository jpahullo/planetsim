package planet.commonapi.factory;

import planet.commonapi.*;
import planet.commonapi.exception.*;

/**
 * It's a factory for NodeHandles abstraction. It pretends build NodeHandles of any
 * class, using the factory method pattern design.
 * <br><br>
 * Any future implementation must contain the no argument constructor.
 * <br><br>
 * @author <a href="mailto: jordi.pujol@estudiants.urv.es">Jordi Pujol</a>
 * @see planet.commonapi.NodeHandle
 */
public interface NodeHandleFactory extends java.io.Serializable {
    
    /**
     * Sets the initial values for the NodeHandleFactory.
     * @param nodeHandle Class reference to build new NodeHandles.
     * @return The same instance once it has been updated.
     * @throws InitializationException if some error occurs during the
     * initialization process.
     */
    public NodeHandleFactory setValues(Class nodeHandle) throws InitializationException;
	/**
	 * Builds a NodeHandle using the default NodeHandle class.
	 * @return A new instance of the default NodeHandle.
	 */
	public NodeHandle buildNodeHandle(Id nodeId, boolean alive) throws InitializationException;
}
