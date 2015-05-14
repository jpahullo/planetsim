package planet.chord.message;

import java.util.Vector;

/**
 * @author <a href="mailto: jordi.pujol@estudiants.urv.es">Jordi Pujol</a>
 * Date: 01/07/2004
 */
public class SuccListMessage implements planet.commonapi.Message {
	private Vector succs = null;
	
	/**
	 * Build the message with an empty Vector with initial size equals
	 * to successor list size specified on context.
	 */
	public SuccListMessage() {
		succs = new Vector();
	}
	
	/**
	 * Build the message with the Vector <b>succs</b> as initial value.
	 * @param succs Initial value for this message.
	 */
	public SuccListMessage(Vector succs) {
        this.succs = new Vector (succs);
	}
	
	/**
	 * @return Returns the succs.
	 */
	public Vector getSuccs() {
		return succs;
	}
	/**
	 * @param succs
	 *            The succs to set.
	 */
	public void setSuccs(Vector succs) {
	    this.succs.clear();
		this.succs.addAll(succs);
	}
	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "SuccListMessage: Successors:[" + succs + "]";
	}
}

