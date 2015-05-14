package planet.commonapi.behaviours;

import java.io.Serializable;

import planet.commonapi.Node;
import planet.commonapi.RouteMessage;

/**
 * This interface permits to invoke the required behaviour when a new 
 * RouteMessage is arrived to any node. Only one Behaviour can be associated
 * to one BehaviourInvoker.
 * @author <a href="mailto: jordi.pujol@estudiants.urv.es">Jordi Pujol</a>
 * 01-jul-2005
 */
public interface BehavioursInvoker extends Serializable {
    /**
     * This method invokes the related behaviour. 
     * @param msg The RouteMessage recently arrived to the <b>node</b>.
     * @param node The Node which holds the behaviour.
     * @return true when the related behaviour has been invoked. false in other
     * case.
     */
    public boolean invoke(RouteMessage msg, Node node);
}
