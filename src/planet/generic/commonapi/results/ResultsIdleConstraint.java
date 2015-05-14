package planet.generic.commonapi.results;

import planet.commonapi.Id;
import planet.commonapi.results.ResultsConstraint;
import planet.commonapi.results.ResultsEdge;

/**
 * IdleGMLConstraint is used to select the whole topology of peer-to-peer
 * network.
 * @author Marc Sanchez <marc.sanchez@estudiants.urv.es>
 */
public class ResultsIdleConstraint implements ResultsConstraint {
	/**
	 * Always return true.
	 * @see planet.commonapi.results.ResultsConstraint#isACompliantEdge(planet.commonapi.results.ResultsEdge)
	 */
	public boolean isACompliantEdge(ResultsEdge e) {
		return true;
	}
	/**
	 * Always return true.
	 * @see planet.commonapi.results.ResultsConstraint#isACompliantNode(planet.commonapi.Id)
	 */
	public boolean isACompliantNode(Id id) {
		return true;
	}
}
