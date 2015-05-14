package planet.generic.commonapi.factory;

/**
 * This class allow to the programmer specify all the topologies of the
 * networks. Actually only exists three topologies:
 * <ul>
 * <li><b>RANDOM ("Random")</b>: Where the Id of nodes are distributed randomly.</li>
 * <li><b>CIRCULAR ("Circular")</b>: Where the Id of nodes are distributed uniformly.</li>
 * <li><b>SERIALIZED ("Serialized")</b>: Where the entire ring are restored from serialized
 * state file.</li>
 * </ul>
 * @author Jordi Pujol
 * @see planet.commonapi.Id Id
 */
public class Topology {
	/**
	 * This topology specify that the Id of nodes are distributed randomly.
	 */
	public static final String RANDOM = "Random";

	/**
	 * This topology specify that the Id of nodes are distributed uniformly
	 * in the ring.
	 */
	public static final String CIRCULAR = "Circular";
	
	/**
	 * This topology specify that the entire ring are restored from serialized
	 * state file.
	 */
	public static final String SERIALIZED = "Serialized";
	
	/**
	 * Identify if the <b>topology</b> specified is valid or not.
	 * @param topology Topology to test if is valid.
	 * @return true if <b>topology</b> is valid. false in another case.
	 */
	public static boolean isValid(String topology) {
		return
            topology != null && (
			topology.equalsIgnoreCase(RANDOM)   ||
			topology.equalsIgnoreCase(CIRCULAR) ||
			topology.equalsIgnoreCase(SERIALIZED));
	}
	
	/**
	 * Inform when the specified <b>topology</b> is valid to
	 * build <b>new instances with sentence <i>new</i></b>.
	 * Actually, only the SERIALIZED topology is not valid.
	 * @param topology Topology to test.
	 * @return true if the topology is RANDOM or CIRCULAR. false
	 * in other case.
	 */
	public static boolean isValidForNew(String topology) {
		return 
			topology.equalsIgnoreCase(RANDOM) ||
			topology.equalsIgnoreCase(CIRCULAR);
	}
}
