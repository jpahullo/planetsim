
package planet.simulate;

import planet.util.Properties;

/**
 * This class contains as static manner all of the types and
 * string representatations of them requireds for the simulator. 
 * @author <a href="mailto: pedro.garcia@urv.net">Pedro Garcia</a>
 * @author <a href="mailto: ruben.mondejar@estudiants.urv.es">Ruben Mondejar</a>
 * @author <a href="mailto: jordi.pujol@estudiants.urv.es">Jordi Pujol</a>
 * 08-jul-2005
 */
public class Globals {
	/**
	 * For internal use to correct value for the TYPES array.
	 */
	private static final int MINIMUM	= 200;
	/**
	 * Event type: Adding a new node to the network.
	 */
	public final static int JOIN		= 200;
	/**
	 * Event type: Node leaving network.
	 */
	public final static int LEAVE		= 201;
	/**
	 * Event type: Node fails.
	 */
	public final static int FAIL 		= 202;
	
	/* END ******************  CONSTANTS FOR EVENTS *********************/
	
	/* ******************  CONSTANTS FOR MODE OF ROUTEMESSAGE *******/
	/**
	 * Mode value: This mode is returned by the simulator always when the
	 * receiver of a message not exist in the actual network. 
	 */
	public final static int ERROR 		= 203;
	
	/* END ******************  CONSTANTS FOR MODE OF ROUTEMESSAGE *******/

	/**
	 * This String contains a string representation of each of 
	 * event types and RouteMessage type.
	 */	
	private final static String[] TYPES = {"JOIN","LEAVE","FAIL","ERROR"}; 

	/**
	 * Returns a string representation of each of event type and 
	 * RouteMessage mode.
	 * @param type Type of the RouteMessage to get its 
	 * String representation.
	 * @return String representation of the type of RouteMessage.
	 */
	public static final String typeToString(int type) {
		try {
			return Properties.overlayPropertiesInstance.typeToString(type);
		} catch(ArrayIndexOutOfBoundsException e) {
			return TYPES[type-MINIMUM];
		}
	}
	/**
	 * Returns a string representation of each of event mode and 
	 * RouteMessage mode.
	 * @param mode Mode of the RouteMessage to get its 
	 * String representation.
	 * @return String representation of the mode of RouteMessage.
	 */
	public static final String modeToString(int mode) {
		try {
			return Properties.overlayPropertiesInstance.modeToString(mode);
		} catch(ArrayIndexOutOfBoundsException e) {
			return TYPES[mode-MINIMUM];
		}
	}
}
