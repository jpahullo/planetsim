package planet.commonapi.factory;

import planet.commonapi.*;
import planet.commonapi.exception.*;

/**
 * It's a factory for Nodes abstraction. It pretends build Nodes of any
 * class using the factory method pattern design.
 * <br><br>
 * Any future implementation must contain the no argument constructor.
 * <br><br>
 * @see planet.commonapi.Node
 * @author <a href="mailto: jordi.pujol@estudiants.urv.es">Jordi Pujol</a>
 * 07-jul-2005
 */
public interface NodeFactory extends java.io.Serializable {
    
    /**
     * Sets the initial values for this NodeFactory.
     * @param idFactory IdFactory to use to build all required Ids.
     * @param nodeClass Class reference of the current Node implementation.
     * @return The same instance once it has been updated.
     * @throws InitializationException if some error occurs during the
     * initialization process.
     */
    public NodeFactory setValues(IdFactory idFactory, Class nodeClass) throws InitializationException;
	/**
	 * Builds a Node using the default IdFactory  and the default
	 * Class for him.
	 * @return A new instance of the default Node.
	 */
	public Node buildNode() throws InitializationException;
	
	/**
	 * Builds a Node using the <b>Id</b> that appears in parameter,
	 * using the default Class for him.
	 * @param id
	 * @return A new instance of the default Node with the specified Id.
	 */
	public Node buildNode(Id id) throws InitializationException;
}
