package planet.results;

import java.io.Serializable;

/**
 *  Statistic class pattern-oriented.
 *  It stores the number of RouteMessages with <b>type</b>
 *  and <b>mode</b> issued and the total number of hops to
 *  reach its destination. 
 *  @author Marc Sanchez	   <marc.sanchez@estudiants.urv.es>
 *  @version 1.0
 */
public class PatternStatus implements Serializable {
	/**
	 * RouteMessage's type: human readable string representation of the
	 * type of a RouteMessage which depends on the overlay impl. 
	 * @see planet.commonapi.Node 
	 */
	private String typeOf;
	/**
	 * RouteMessage's mode: human readable string representation of the
	 * mode of a RouteMessage which depends on the overlay impl. 
	 * @see planet.commonapi.Node 
	 */
	private String modeOf;
	/**
	 * Total number of messages of RouteMessage's type <b>typeOf</b> and 
	 * mode <b>modeOf</b>.
	 */
	private int messagesPerType = 1;
	/**
	 * Stores the number of hops indexed by RouteMessage's type and mode.
	 */
	private int hopsPerType = 0;
	
	//Constructor
	public PatternStatus(String typeOf) {
		this.typeOf = typeOf;
		this.modeOf = "'*'";
	}
	
	public PatternStatus(String typeOf, String modeOf) {
		this.typeOf = typeOf;
		this.modeOf = modeOf;
	}
	
	public void updateHops() { hopsPerType++;  }
	public void updateMessages() { messagesPerType++; }
	/**
	 * @return Returns the RouteMessage's type
	 */
	public String getType() {
		return this.typeOf;
	}
	/**
	 * @return Returns the RouteMessage's mode
	 */
	public String getMode() {
		return this.modeOf;
	}
	/**
	 * @return Returns the total number of hops per RouteMessage's type.
	 */
	public int getHopsPerType() {
		return this.hopsPerType;
	}
	/**
	 * @return Returns the total number of messages per RouteMessage's type.
	 */
	public int getMessagesPerType() {
		return this.messagesPerType;
	}
}
