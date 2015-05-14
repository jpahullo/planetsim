
package planet.generic.commonapi.results;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;

import planet.commonapi.Network;
import planet.commonapi.Node;
import planet.commonapi.exception.InitializationException;
import planet.commonapi.results.ResultsConstraint;
import planet.commonapi.results.ResultsGenerator;
import planet.util.Properties;

/**
 * This generator builds an output in Pajek format. Uses some GML specific
 * implementations.
 * @author <a href="mailto: pedro.garcia@urv.net">Pedro Garcia</a>
 * @author <a href="mailto: jordi.pujol@estudiants.urv.es">Jordi Pujol</a>
 * 15/02/2005
 */
public class ResultsPajekGenerator implements ResultsGenerator {

	
	private static final long serialVersionUID = 34747578L;
	
    /**
     * Anything to do.
     */
    public ResultsPajekGenerator() { }

	/**
	 * This method generates the Pajek information into <b>out</b> file 
	 * for the specified <b>network</b>. 
	 * @param network Network.
	 * @param out Path of the file to write it out.
	 * @param constraint Constraint used to select edges for the results
     * @param wholeNetworkLayout This boolean indicates if we want to shown 
     * all the nodes of the network.
     * @see planet.commonapi.results.ResultsGenerator#generateResults(planet.commonapi.Network,
     *      java.lang.String, planet.commonapi.results.ResultsConstraint, boolean)
     */
    public void generateResults(Network network, String out,
            ResultsConstraint constraint, boolean wholeNetworkLayout) {
        StringBuffer buffer = new StringBuffer(
                "*Vertices " +  network.size()
                + "\r\n");
        
        java.util.Iterator it = network.iterator();
        
        ResultsGMLProperties gmlProps = null;
        try {
            gmlProps = (ResultsGMLProperties)Properties.getResultsPropertiesInstance(ResultsNames.PAJEK);
        } catch (InitializationException e)
        {
            e.printStackTrace();
            System.exit(-1);
        }

        double radius = (network.size() * gmlProps.minimalNodeDistance)
                / (2.0 * Math.PI);
        double da = (2.0 * Math.PI) / network.size();
        // x cartesian coordinate of the node
        double x;
        // y cartesian coordinate of the node
        double y;
        // Initial angle of the circular Identifier Space
        double a = 0.0;

		int countNodes = 1;
		
        java.util.Collection E = new java.util.HashSet();
		
		HashMap edges = new HashMap();
		
        while (it.hasNext()) {
            Node node = (Node) it.next();
            x = radius * (1 + Math.cos(a));
            y = radius * (1 + Math.sin(a));

			buffer.append("      "+ countNodes+ " \""+ node.getId()+"\"\r\n");
			edges.put(node.getId(),String.valueOf(countNodes));
			countNodes ++;

            a = a + da;
        }
		buffer.append("*Edges \r\n");
        it = network.iterator();
        while (it.hasNext()) {
            Node node = (Node) it.next();
            node.buildEdges(ResultsNames.PAJEK,E, constraint);
        }

        it = E.iterator();
        while (it.hasNext()) {
            ResultsEdgeImpl e = (ResultsEdgeImpl) it.next();
	  		buffer.append("      "+edges.get(e.getSource())+ "      "+ edges.get(e.getTarget())+ "      1\r\n");
        }
  
        try {
            FileOutputStream F = new FileOutputStream(out);
            BufferedWriter BF = new BufferedWriter(new OutputStreamWriter(F));
            BF.write(buffer.toString());
            BF.close();
        } catch (IOException e) {
        }
    }
}
