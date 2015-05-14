package planet.chord;

import planet.commonapi.Id;
import planet.commonapi.results.ResultsConstraint;
import planet.commonapi.results.ResultsEdge;

/**
 * Constraints for GML edge selection according to Integer congruences partitions. 
 * @author Marc Sanchez <marc.sanchez@estudiants.urv.es>
 */
public class GMLConstraintZmod implements ResultsConstraint {
	// Z mod set
	protected int Zmod;
	// partition class of Z mod set.
	protected int pt;

	// Constructor
	public GMLConstraintZmod(int Zmod, int pt) {
		// Z mod set
		this.Zmod = Zmod;
		// partition class of Z mod set.
		this.pt = pt;
	}
	/**
	 * @return Returns the partition equivalence class.
	 */
	public int getPt() {
		return pt;
	}
	/**
	 * @param pt The partition equivalence class to set.
	 */
	public void setPt(int pt) {
		this.pt = pt;
	}
	/**
	 * @return Returns the zmod.
	 */
	public int getZmod() {
		return Zmod;
	}
	/**
	 * @param zmod The zmod to set.
	 */
	public void setZmod(int zmod) {
		Zmod = zmod;
	}
	/* (non-Javadoc)
	 * @see planet.GML.GMLConstraint#isACompliantEdge(planet.GML.GMLEdgeImpl)
	 */
	public boolean isACompliantEdge(ResultsEdge e) {
		// Chord Id of 32 bits 
		long target = ((int[]) e.getTarget().getValue())[0] & 0x0ffffffffL;
		// Chord Id of 32 bits
		long source = ((int[]) e.getSource().getValue())[0] & 0x0ffffffffL;
		return (target % Zmod == pt) && (source  % Zmod == pt);
	}
	/* (non-Javadoc)
	 * @see planet.GML.GMLConstraint#isACompliantNode(planet.commonapi.Id id)
	 */
	public boolean isACompliantNode(Id id) {
		// Chord Id of 32 bits
		long Nid = ((int []) id.getValue())[0] & 0x0ffffffffL;
		return Nid % Zmod == pt;
	}
}
