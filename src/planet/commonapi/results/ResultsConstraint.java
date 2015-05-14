package planet.commonapi.results;

import planet.commonapi.Id;

/**
 * This interface is used to define constraints on edge and node selection.
 * <br><br>
 * Any future implementation must incorpore the no arguments constructor.
 * <br><br> 
 * @author Marc Sanchez <marc.sanchez@estudiants.urv.es>
 */
public interface ResultsConstraint extends java.io.Serializable {
	/**
	 * This method returns true if a Edge is compliant according to this Constraint.
	 * @param e Edge to check.  
	 * @return true if Edge is compliant according to this constraint
	 */
	public boolean isACompliantEdge(ResultsEdge e);
	/**
	 * This method returns true if a Node is compliant according to this Constraint.
	 * @param id Id to check.
	 * @return true if Id is compliant accordint to this constraint.
	 */
	public boolean isACompliantNode(Id id);
	
}
