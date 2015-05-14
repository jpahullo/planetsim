package planet.generic.commonapi.behaviours;

import java.util.*;
import planet.util.Properties;
import planet.commonapi.Id;
import planet.commonapi.behaviours.BehavioursRoleSelector;

/**
 * BehaviourRoleSelectorImpl provides a default implementation for BehaviourRoleSelector
 * interface. The node's role can only be determined on  behaviour's context. It uses the 
 * following behaviour properties file:
 * <ol>
 * <li> BEHAVIOURS_PROPERTIES_FAULTY_NODES: percentatge of faulty nodes within the network.
 * <li> BEHAVIOURS_PROPERTIES_MALICIOUS_DISTRIBUTION: type of distribution of the faulty nodes: 
 *      <ol> 
 * 			<li> BehaviourPropertiesImpl.BEHAVIOUR_UNIFORM_MALICIOUS_DISTRIBUTION_MASK: 
 *               faulty nodes distributed uniformly along the network.
 * 			<li> BehaviourPropertiesImpl.BEHAVIOUR_CHAIN_MALICIOUS_DISTRIBUTION_MASK: 
 *               faulty  nodes  forming a continous chain of faulty nodes.
 * 		</ol>
 * </ol> 
 * @author Marc Sánchez <marc.sanchez@estudiants.urv.es> *
 */
public class BehavioursRoleSelectorImpl implements BehavioursRoleSelector {
	/**
	 * This methods selects malicious nodes from the overlay according to a percentage and
	 * distribution of them along the network.
	 * @param network Iterator of all ids. 
	 * @param percentage Percentage of faulty nodes.
	 * @param distribution Distribution's type of faulty nodes: chain or uniform.
	 * @throws ClassCastException if <b>network</b> is not a collection of id's objects. 
	 * @return Returns a set with Ids of malicious nodes.
	 */
	public Set select(Iterator network, double percentage, int distribution) {
			int howMany = (int)Math.round(percentage*(double)Properties.factoriesNetworkSize);
			boolean uniformDT = distribution == BehavioursPropertiesImpl.BEHAVIOUR_UNIFORM_MALICIOUS_DISTRIBUTION_MASK; 
			int interleaving = 0;
			if (howMany > 0) interleaving = uniformDT? Properties.factoriesNetworkSize / howMany: 1;
			else return null;
			Set badSet = new HashSet();
			for (int step = 0; network.hasNext();) {
				Id node = (Id) network.next(); 
				if (++step == interleaving) {
					badSet.add(node);
					step = 0;
					if (--howMany <= 0) break;
				}
			}
			return badSet;
	}
}
