package planet.commonapi.behaviours;

import java.util.*;
/**
 * BehaviourRoleSelector's Interface is aimed at providing a mechanism to set the role of 
 * each node within the <b>overlay</b> network. The node's role can only be determined on
 * behaviour's context. It uses the following several from behaviour properties file:
 * <ol>
 * <li> DEFAULT_FAULTY_NODES: percentatge of faulty nodes within the network.
 * <li> DEFAULT_MALICIOUS_DISTRIBUTION: type of distribution of the faulty nodes: 
 *      <ol> 
 * 			<li> BehaviourProperties.UNIFORM: faulty nodes distributed uniformaly along the
 * 				 network.
 * 			<li> BehaviourProperties.CHAIN: faulty nodes forming a continous chain of faulty
 * 				 nodes.
 * 		</ol>
 * </ol> 
 * @author Marc Sánchez <marc.sanchez@estudiants.urv.es>
 */
public interface BehavioursRoleSelector extends java.io.Serializable {
	/**
	 * This methods selects malicious nodes from the overlay according to a percentage and
	 * distribution of them along the network.
	 * @param network Iterator of all ids. 
	 * @param percentage Percentage of faulty nodes.
	 * @param distribution Distribution's type of faulty nodes: chain or uniform.
	 * @throws ClassCastException if <b>network</b> is not a collection of id's objects. 
	 * @return Returns a set with Ids of malicious nodes.
	 */
	public Set select(Iterator network, double percentage, int distribution);
}
