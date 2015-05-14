package planet.generic.commonapi.factory;

import planet.commonapi.Application;
import planet.commonapi.EndPoint;
import planet.commonapi.Node;
import planet.commonapi.exception.InitializationException;
import planet.commonapi.factory.EndPointFactory;

/**
 * This class uses the Factory Method pattern design to build new EndPoints.
 * @author <a href="mailto: jordi.pujol@estudiants.urv.es">Jordi Pujol</a>
 * 07-jul-2005
 */
public class EndPointFactoryImpl implements EndPointFactory {
	/**
	 * Implementation Class of the EndPoint to use for build new EndPoints.
	 */
	private Class endPoint = null;
	
	/**
     * Build a uninitialized EndPointFactoryImpl. It requires the
     * <b>setValues(...)</b> invokation.
	 */
	public EndPointFactoryImpl() {}
    
    /**
     * Sets initial values for this EndPointFactory.
     * @param endPoint Class reference to build new EndPoint instances.
     * @return The same instance once it has been updated.
     * @throws InitializationException if some error occurs during the
     * initialization process.
     */
    public EndPointFactory setValues(Class endPoint) throws InitializationException {
        this.endPoint = endPoint;
        return this;
	}
	
	/**
	 * Builds a new instance of EndPoint, relating specified Application and Node.
	 * @see planet.commonapi.factory.EndPointFactory#buildEndPoint(planet.commonapi.Application, planet.commonapi.Node)
	 * @param app Application to run over the node.
	 * @param node Node over which run the specified application.
	 */
	public EndPoint buildEndPoint(Application app, Node node) throws InitializationException {
        return ((EndPoint)GenericFactory.newInstance(endPoint)).setValues(app,node);
	}
}
