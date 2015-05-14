package planet.commonapi.results;

import planet.commonapi.Id;
/**
 * This wrapper class is used to store an edge of a directed or undirected
 * graph based on GML file format.
 * <br><br>
 * Any future implementation must incorpore the no arguments constructor.
 * <br><br> 
 * @see <a href="http://infosun.fmi.uni-passau.de/Graphlet/GML/">GML</a> 
 * @author Marc Sanchez <msa.ei@estudiants.urv.es>
 * @author Jordi Pujol <jordi.pujol@estudiants.urv.es>
 */
public interface ResultsEdge extends java.io.Serializable {

    /**
	 * @return Returns the fill color of the edge.
	 */
	public String getFill();
	/**
	 * @param fill The fill color of the edge to set.
	 */
	public void setFill(String fill);
	/**
	 * @return Returns the source.
	 */
	public Id getSource();
	/**
	 * @param source The source to set.
	 */
	public void setSource(Id source);
	/**
	 * @return Returns the target.
	 */
	public Id getTarget();
	/**
	 * @param target The target to set.
	 */
	public void setTarget(Id target);
	/**
	 * @return Returns the isDirected.
	 */
	public boolean isDirected();
	/**
	 * @param isDirected The isDirected to set.
	 */
	public void setDirected(boolean isDirected) ;	
    
    /**
     * Sets the values for this edge.
     * @param source Source node.
     * @param target Target node.
     * @param isDirected True when the edge is directed.
     * @param fill The color for the edge.
     * @return The same instance after it has been updated.
     */
    public ResultsEdge setValues(Id source, Id target, boolean isDirected, String fill);
}
