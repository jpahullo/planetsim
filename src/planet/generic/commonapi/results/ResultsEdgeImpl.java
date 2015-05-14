package planet.generic.commonapi.results;

import planet.commonapi.Id;
import planet.commonapi.results.ResultsEdge;
/**
 * This wrapper class is used to store an edge of a directed or undirected
 * graph based on GML file format.
 * @see <a href="http://infosun.fmi.uni-passau.de/Graphlet/GML/">GML</a> 
 * @author Marc Sanchez <msa.ei@estudiants.urv.es>
 */
public class ResultsEdgeImpl implements ResultsEdge {
	// source of the edge
	protected Id source;
	// target of the edge
	protected Id target;
	// Claims if edges are part of a directed graph
	protected boolean isDirected = true;
	// Color of the edge
	protected String fill;
	
    /**
     * Empty constructor.
     */
	public ResultsEdgeImpl() {
	}
    
	/**
	 * @return Returns the fill color of the edge.
	 */
	public String getFill() {
		return fill;
	}
	/**
	 * @param fill The fill color of the edge to set.
	 */
	public void setFill(String fill) {
		this.fill = fill;
	}
	/**
	 * @return Returns the source.
	 */
	public Id getSource() {
		return source;
	}
	/**
	 * @param source The source to set.
	 */
	public void setSource(Id source) {
		this.source = source;
	}
	/**
	 * @return Returns the target.
	 */
	public Id getTarget() {
		return target;
	}
	/**
	 * @param target The target to set.
	 */
	public void setTarget(Id target) {
		this.target = target;
	}
	/**
	 * @return Returns the isDirected.
	 */
	public boolean isDirected() {
		return isDirected;
	}
	/**
	 * @param isDirected The isDirected to set.
	 */
	public void setDirected(boolean isDirected) {
		this.isDirected = isDirected;
	}
	
	/**
	 * Object.equals(Object o) implementation for EdgeImpl. 
	 */
	public boolean equals(Object o) {
		ResultsEdgeImpl edge = (ResultsEdgeImpl) o;
		
		boolean directedGraphCND = edge.getSource().equals(this.source) && edge.getTarget().equals(this.target);
		if (isDirected()) return directedGraphCND;
		else {
			if (directedGraphCND) return true;
			boolean undirectedGraphCND = edge.getSource().equals(this.target) && edge.getTarget().equals(this.source);
			return undirectedGraphCND;
		}
	}
	
	/**
	 * Object.hashCode(Object o) implementation for EdgeImpl. 
	 */
	public int hashCode() {
	 	return 0;
	}
	
	/**
	 * Object.toString() implementation for EdgeImpl.
	 */
	public String toString() {
		return "Edge [" +
				"\n    source " + source + 
				"\n    target " + target +
				"\n    graphics" +
				"\n    [" + 
				"\n          fill	\"" + fill + "\"" +
				"\n    ]" + 
				"\n]";
 	}
    
    
    /**
     * Sets the initial values for this edge.
     * @param source Source node.
     * @param target Destination node.
     * @param isDirected true when edge is directed.
     * @param fill The color for the edge. May be null when not is used.
     * @return The same instance after it has been updated.
     * @see planet.commonapi.results.ResultsEdge#setValues(planet.commonapi.Id, planet.commonapi.Id, boolean, java.lang.String)
     */
    public ResultsEdge setValues(Id source, Id target, boolean isDirected, String fill) {
        this.source = source;
        this.target = target;
        this.isDirected = isDirected;
        this.fill = fill;
        return this;
    }
}
