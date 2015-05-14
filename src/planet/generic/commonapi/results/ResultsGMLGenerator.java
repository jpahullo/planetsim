package planet.generic.commonapi.results;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import planet.commonapi.Network;
import planet.commonapi.Node;
import planet.commonapi.exception.InitializationException;
import planet.commonapi.results.ResultsConstraint;
import planet.commonapi.results.ResultsGenerator;
import planet.util.Properties;

/**
 * @author <a href="mailto: jordi.pujol@estudiants.urv.es">Jordi Pujol</a>
 * 15/02/2005
 */
public class ResultsGMLGenerator implements ResultsGenerator {

    /**
     * Anything to do.
     */
    public ResultsGMLGenerator() { }

	/**
	 * This method generates GML information into <b>out</b> file 
	 * for a stable Overlay ring using GML format. 
	 * @param network Network.
	 * @param out Path of the file to write it out.
	 * @param constraint Constraint used to select edges for resulting Overlay graph
     * @param wholeNetworkLayout This boolean indicates if we want to shown 
     * all the nodes of the network.
     * @see planet.commonapi.results.ResultsGenerator#generateResults(planet.commonapi.Network,
     *      java.lang.String, planet.commonapi.results.ResultsConstraint, boolean)
     */
    public void generateResults(Network network, String out,
            ResultsConstraint constraint, boolean wholeNetworkLayout) {
        StringBuffer buffer = new StringBuffer(
                "graph [\n    " + 
                	"\tid 0\n" +
                	"\tdirected 0\n" + 
                	"\thierarchic 1\n" + 
                	"\tlabel	\"\"\n");
        
        java.util.Iterator it = network.iterator();
        
        ResultsGMLProperties gmlProps = null;
        try {
            gmlProps = (ResultsGMLProperties)Properties.getResultsPropertiesInstance(ResultsNames.GML);
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

        java.util.Collection E = new java.util.HashSet();
        //  node GML clause
        while (it.hasNext()) {
            Node node = (Node) it.next();
            x = radius * (1 + Math.cos(a));
            y = radius * (1 + Math.sin(a));

			///////////////////// GML Template header \\\\\\\\\\\\\\\\\\\\\\\\\\\
			String header = "\tnode [\n\t\tid \"" + node.getId() + "\"\n" +
			  "\t\tlabel \"" + node.getId() + "\"\n" +
				  "\t\tgraphics\n" +
				  "\t\t[\n" +
				  "\t\t\tx  " + x + "\n" + 
				  "\t\t\ty  " + y + "\n" +
				  "\t\t\tw	" + gmlProps.width + "\n" + 
				  "\t\t\th	" + gmlProps.height + "\n" +
				  "\t\t\ttype	\"" + gmlProps.shape + "\"\n";
			
			//////////////////// GML Template footer \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
			String footer = "\t\t\toutline	\"" + gmlProps.outline + "\"\n" + 
			  "\t\t]\n" + 
			  "\t\tLabelGraphics\n" + 
			  "\t\t[\n" + 
			  "\t\t\ttext	\"" + node.getId() + "\"\n" + 
			  "\t\t\tfontSize	" + gmlProps.fontSize + "\n" + 
			  "\t\t\tfontName	\"" + gmlProps.fontName + "\"\n" +
			  "\t\t\tmodel	\"null\"\n" + 
			  "\t\t\tanchor	\"null\"\n" + 
			  "\t\t]\n" + 
	  		  "\t]\n";

            if (!wholeNetworkLayout) {
                if (constraint.isACompliantNode(node.getId()))
                    buffer.append(header + "\t\t\tfill	\"" + gmlProps.fill
                            + "\"\n" + footer);
            } else {
                if (constraint.isACompliantNode(node.getId())) {
                    buffer.append(header + "\t\t\tfill	\"" + gmlProps.fill
                            + "\"\n" + footer);
                } else {
                    buffer.append(header + "\t\t\tfill	\""
                            + gmlProps.alternativeFill + "\"\n" + footer);
                }
            }
            a = a + da;
        }

        it = network.iterator();
        while (it.hasNext()) {
            Node node = (Node) it.next();
            node.buildEdges(ResultsNames.GML,E, constraint);
        }

        it = E.iterator();
        while (it.hasNext()) {
            ResultsEdgeImpl e = (ResultsEdgeImpl) it.next();
	  		buffer.append("\tedge [\n\t\tsource \"" + e.getSource() +	"\"\n" + 
	  				"\t\ttarget \"" + e.getTarget() + "\"\n" +
					"\t\tlabel	\"\"\n" + 
					"\t\tgraphics\n" +  
					"\t\t[\n" + 
					"\t\t\tfill	\"" +  e.getFill() + "\"\n" + 
					"\t\t]\n" + 
					"\t\tLabelGraphics\n" +
					"\t\t[\n" +
					"\t\t]\n" + 
	  				"\t]\n");
        }
        buffer.append("\n]");
        try {
            FileOutputStream F = new FileOutputStream(out);
            BufferedWriter BF = new BufferedWriter(new OutputStreamWriter(F));
            BF.write(buffer.toString());
            BF.close();
        } catch (IOException e) {
        }

    }

}
