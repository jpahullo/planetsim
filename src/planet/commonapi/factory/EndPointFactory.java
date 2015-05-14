package planet.commonapi.factory;

import planet.commonapi.Application;
import planet.commonapi.EndPoint;
import planet.commonapi.Node;
import planet.commonapi.exception.InitializationException;

/**
 * This interface attempts to abstract building EndPoints, using the
 * pattern design Factory Method. For this, requires an instance of
 * Application and other one of Node.
 * <br><br>
 * Any future implementation must contain the no argument constructor.
 * <br><br>
 * @author <a href="mailto: jordi.pujol@estudiants.urv.es">Jordi Pujol</a>
 * 07-jul-2005
 */
public interface EndPointFactory {
    /**
     * Sets initial values for this EndPointFactory.
     * @param endPoint Class reference to build new EndPoint instances.
     * @return The same instance once it has been updated.
     * @throws InitializationException if some error occurs during the
     * initialization process.
     */
    public EndPointFactory setValues(Class endPoint) throws InitializationException;
	/**
	 * Builds a new EndPoint that relates the Application <b>app</b>
	 * with the underlying Node <b>node</b>.
	 * @param app Application to install at this node.
	 * @param node Node where will be installed the Application.
	 * @return A new instance of EndPoint that relates the Application
	 * and the Node. 
	 */
	public EndPoint buildEndPoint(Application app, Node node) throws InitializationException;
}
