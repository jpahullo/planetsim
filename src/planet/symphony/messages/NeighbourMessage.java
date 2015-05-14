package planet.symphony.messages;

import java.util.Collection;

/**
 * Is a neighbour's set performed as a message.
 * @author Marc Sanchez (<a href=mailto:marc.sanchez@estudiants.urv.es>marc.sanchez@estudiants.urv.es</a>)
 */
public class NeighbourMessage implements planet.commonapi.Message {
	Collection neighbourhoodSet;
	
	public NeighbourMessage() {
		this.neighbourhoodSet = null;
	}
	
	public NeighbourMessage(Collection neighbourhoodSet) {
		this.neighbourhoodSet = neighbourhoodSet;
	}
	/**
	 * @return Returns the neighbourhoodSet.
	 */
	public Collection getNeighbourhoodSet() {
		return neighbourhoodSet;
	}
	/**
	 * @param neighbourhoodSet The neighbourhoodSet to set.
	 */
	public void setNeighbourhoodSet(Collection neighbourhoodSet) {
		this.neighbourhoodSet = neighbourhoodSet;
	}
	
	public String toString() {
		return "<setNeighbourhoodMessage neighbourSet=" + neighbourhoodSet.toString() + "/>";	
	}
}
