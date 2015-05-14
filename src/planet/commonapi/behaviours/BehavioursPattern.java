package planet.commonapi.behaviours;

import java.io.Serializable;

/**
 * This empty interface stablish no contract. Its objective is to store a 
 * pattern for incoming messages on a node. This pattern is used to
 * dispatch a behaviour whenever the pattern matches with the incoming message. 
 * To allow behaviours to execute randomly on time, every pattern may have a 
 * probability.
 * @author <a href="mailto: jordi.pujol@estudiants.urv.es">Jordi Pujol</a>
 * 01-jul-2005
 */
public interface BehavioursPattern extends Serializable {

}
