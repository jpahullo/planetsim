package planet.commonapi.results;

import planet.commonapi.Network;

/**
 * This interface builds the required results, with an iterative process
 * whole network, saving any output into an external file.
 * <br><br>
 * Any future implementation must incorpore the no arguments constructor.
 * <br><br> 
 * @author <a href="mailto: jordi.pujol@estudiants.urv.es">Jordi Pujol</a>
 * 15/02/2005
 */
public interface ResultsGenerator extends java.io.Serializable{

    /**
	   * This method generates required information for the actual <b>network</b>
       * using the required output format. 
	   * @param network Network descriptor.
	   * @param out Path of the external file to write it out.
	   * @param constraint Constraint used to select edges for results
	   * @param wholeNetworkLayout This boolean indicates if we want to shown all the nodes of the network.
	   */
	  public void generateResults(Network network, String out, ResultsConstraint constraint, boolean wholeNetworkLayout);
}
