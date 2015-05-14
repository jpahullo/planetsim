package planet.commonapi.behaviours;

import planet.commonapi.Node;
import planet.commonapi.RouteMessage;
import planet.commonapi.behaviours.exception.NoBehaviourDispatchedException;
import planet.commonapi.behaviours.exception.NoSuchBehaviourException;

/**
 * The BehavioursPool's interface is aimed at providing an internal  scheduler of
 * node's behaviuours. At startup, behaviours are registered to the pool from
 * the current configuration.
 * <br><br>
 * Then, a message interceptor is built up and is ready to invoke  behaviours
 * when a message pattern matches to those provided by the current configuration.
 * <br><br>
 * Any future implementation must incorpore the no arguments constructor.
 * <br><br>
 * @author <a href="mailto: marc.sanchez@estudiants.urv.es">Marc Sanchez</a>
 * @author <a href="mailto: jordi.pujol@estudiants.urv.es">Jordi Pujol</a>
 * Date: 10/10/2004   
 */
public interface BehavioursPool extends java.io.Serializable {
	/**
	 * Given a RouteMessage and a Node as input, onMessage's method intends to
	 * invoke some behaviours only if RouteMessage's type and mode fields matches
	 * some behaviour's pattern.  
	 * @param msg RoteMessage taken as input.
	 * @param node Node taken as input.
	 * @return Returns either an array of RouteMessages or null when no messages
	 * need to transmit this node.
	 * @throws NoSuchBehaviourException when no behaviour matches RouteMessage's
	 * pattern. 
	 */
	public void onMessage(RouteMessage msg, Node node) throws NoSuchBehaviourException,NoBehaviourDispatchedException;
}
