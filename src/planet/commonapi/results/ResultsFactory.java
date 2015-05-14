package planet.commonapi.results;

import planet.commonapi.Id;
import planet.commonapi.exception.InitializationException;

/**
 * Its objective is build any required element to build a graph.
 * <br><br>
 * Any future implementation must contain the no argument constructor.
 * <br><br>
 * @author <a href="mailto: jordi.pujol@estudiants.urv.es">Jordi Pujol</a>
 * 21/02/2005
 */
public interface ResultsFactory extends java.io.Serializable{

    /**
     * Sets the initial values for this ResultsFactory.
     * @param resultsEdge Class reference of the current ResultsEdge implementation.
     * @param resultsConstraint Class reference of the current ResultsConstraint
     * implementation.
     * @param resultsGenerator Class reference of the current ResultsGenerator 
     * implementation.
     * @return The same instance once it has been updated.
     * @throws InitializationException if some error occurs during the
     * initialization process.
     */
    public ResultsFactory setValues(Class resultsEdge, Class resultsConstraint, Class resultsGenerator) throws InitializationException;
    /**
     * Build an edge with the specified values.
     * @param source Source node Id.
     * @param target Destination node Id.
     * @param directed true when the edge is directed. false in other case.
     * @param fill Fill color in "#RRGGBB" format.
     * @return An instance of the required Edge implementation.
     * @throws InitializationException if an error occurs during the initialization.
     */
    public ResultsEdge buildEdge(Id source, Id target, boolean directed, String fill) throws InitializationException;
    
    /**
     * Builds the Generator specified in the properties file.
     * @return The required Generator.
     * @throws InitializationException if an error occurs during the initialization.
     */
    public ResultsGenerator buildGenerator() throws InitializationException ;
    
    /**
     * Build the Constraint specified in the properties file.
     * @return The Constraint.
     * @throws InitializationException if an error occurs during the initialization.
     */
    public ResultsConstraint buildConstraint() throws InitializationException ;
    
}
