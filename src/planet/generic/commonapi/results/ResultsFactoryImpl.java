package planet.generic.commonapi.results;

import planet.commonapi.Id;
import planet.commonapi.exception.InitializationException;
import planet.commonapi.results.ResultsConstraint;
import planet.commonapi.results.ResultsEdge;
import planet.commonapi.results.ResultsFactory;
import planet.commonapi.results.ResultsGenerator;
import planet.generic.commonapi.factory.GenericFactory;

/**
 * Factory to build any specified results implementation classes.
 * @author <a href="mailto: marc.sanchez@estudiants.urv.es">Marc Sanchez</a>
 * @author <a href="mailto: jordi.pujol@estudiants.urv.es">Jordi Pujol</a>
 */
public class ResultsFactoryImpl implements ResultsFactory {
    
    /** ResultsEdge implementation class to use. */
    private Class resultsEdge = null;
    /** ResultsGenerator implementation class to use. */
    private Class resultsGenerator = null;
    /** ResultsConstraint implementation class to use. */
    private Class resultsConstraint = null;
    

    /**
     * Builds an uninitialized ResultsFactoryImpl. Requires the 
     * <b>setValues(...)</b> invokation.
     */
    public ResultsFactoryImpl() {}
    
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
     * @see planet.commonapi.results.ResultsFactory#setValues(java.lang.Class, java.lang.Class, java.lang.Class)
     */
    public ResultsFactory setValues(Class resultsEdge, Class resultsConstraint,
            Class resultsGenerator) throws InitializationException {
        this.resultsEdge = resultsEdge;
        this.resultsConstraint = resultsConstraint;
        this.resultsGenerator = resultsGenerator;
        return this;
    }
    
    /**
     * Build a ResultsEdge with the specified values.
     * @param source Source node Id.
     * @param target Destination node Id.
     * @param directed true when the edge is directed. false in other case.
     * @param fill Fill color in "#RRGGBB" format.
     * @return An instance of ResultsEdge.
     * @throws InitializationException if an error occurs during the initialization.
     */
    public ResultsEdge buildEdge(Id source, Id target, boolean directed, String fill) throws InitializationException
    {
        return ((ResultsEdge)GenericFactory.newInstance(resultsEdge)).setValues(source,target,directed,fill);
    }

    
    /**
     * Builds the ResultsGenerator specified in the current configuration.
     * @return The ResultsGenerator.
     * @throws InitializationException if an error occurs during the initialization.
     */
    public ResultsGenerator buildGenerator() throws InitializationException 
    {
        return (ResultsGenerator)GenericFactory.newInstance(resultsGenerator);
    }
    
    /**
     * Build the ResultsConstraint specified in the current configuration.
     * @return The ResultsConstraint.
     * @throws InitializationException if an error occurs during the initialization.
     */
    public ResultsConstraint buildConstraint() throws InitializationException 
    {
        return (ResultsConstraint)GenericFactory.newInstance(resultsConstraint);
    }
}
