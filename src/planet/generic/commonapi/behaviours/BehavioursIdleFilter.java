package planet.generic.commonapi.behaviours;

import planet.commonapi.Node;
import planet.commonapi.RouteMessage;
import planet.commonapi.behaviours.BehavioursFilter;
import planet.generic.commonapi.factory.GenericFactory;
import planet.simulate.Logger;

/**
 * IdleFilter class does not filter any RouteMessage.
 * @author <a href="mailto: marc.sanchez@urv.net">Marc Sanchez</a>
 * @author <a href="mailto: jordi.pujol@estudiants.urv.es">Jordi Pujol</a>
 * 05-jul-2005
 */
public class BehavioursIdleFilter  implements BehavioursFilter { 
    
    /**
     * Empty constructor. Does nothing.
     */
    public BehavioursIdleFilter() {}
    
	/**
	 * Given a RouteMessage and a Node as input, filter's method filters the 
	 * input RouteMessage if does not satisfy filter's precondition. 
	 * @param msg RouteMessage taken as input.
	 * @param node Node taken as input.
	 * @return true when the <b>msg</b> must be filtered. false when
     * this RouteMessage must be processed by the behaviours.
	 */
	public boolean filter(RouteMessage msg, Node node) {
		if (!node.isAlive()) {
			Logger.log("Node " + node.getId() + " is not in, but it receives messages!!", Logger.ERROR_LOG);
			GenericFactory.freeMessage(msg);
			return true;
		}
		
		if (msg.getDestination() == null) {
			Logger.log("destination null", Logger.ERROR_LOG);
			GenericFactory.freeMessage(msg);
			return true;
		}
		return false;
	}
    
	/**
	 * @return Returns the name of the filter. 
	 */
	public String getName() {
		return "BehavioursIdleFilter";
	}
    
	/**
	 * @return Returns a string representation of the filter. In general, the toString
	 * method returns a string that "textually represents" this behaviour. The result should 
	 * be a concise but informative representation that is easy for a person to read. 
	 */ 
	public String toString() {
		return getName();
	}
}
