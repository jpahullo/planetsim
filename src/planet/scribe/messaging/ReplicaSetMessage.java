package planet.scribe.messaging;
import java.util.Vector;
import planet.commonapi.Id;
/**
 * Message of data to be send a request of the replica set of an another node
 * 
 * @author Ruben Mondejar
 * @author <a href="mailto: jordi.pujol@estudiants.urv.es">Jordi Pujol </a>
 *         Date: 02/07/2004
 */
public class ReplicaSetMessage implements planet.commonapi.Message {
	private Id messageKey = null;
	private Id sourceId = null;
	private boolean request = false;
	private Vector replicaSet = null;
	private int maxRank = 0;
	
	/**
	 * Builds the message to inform the maximum number of replica
	 * set that is required.
	 * @param sourceId Key from which to find the replica set.
	 * @param maxRank Maximum number of the replica set.
	 */
	public ReplicaSetMessage(Id sourceId, int maxRank) {
		this.messageKey = sourceId;
		this.request = true;
		this.maxRank = maxRank;
	}
	
	/**
	 * Builds the message to inform the required replica set.
	 * @param sourceId Key from which to find the replica set.
	 * @param replicaSet Replica set for this key.
	 */
	public ReplicaSetMessage(Id sourceId, Vector replicaSet) {
		this.messageKey = sourceId;
		this.request = false;
		this.replicaSet = replicaSet;
	}
	
	/**
	 * @return Returns the messageKey.
	 */
	public Id getMessageKey() {
		return messageKey;
	}
	
	/**
	 * @param messageKey
	 *            The messageKey to set.
	 */
	public void setMessageKey(Id messageKey) {
		this.messageKey = messageKey;
	}
	
	/**
	 * @return Returns true if is request type.
	 */
	public boolean isRequest() {
		return request;
	}
	
	/**
	 * @param request true if is request type.
	 */
	public void setRequest(boolean request) {
		this.request = request;
	}
	
	/**
	 * @return Returns the replicaSet.
	 */
	public Vector getReplicaSet() {
		return replicaSet;
	}

	/**
	 * @param replicaSet
	 *            Node replica set. 
	 */
	public void setReplicaSet(Vector replicaSet) {
		this.replicaSet = replicaSet;
	}
	
	public String toString() {
		if (request)
			return "ReplicaSetMessage: Key[" + messageKey + "] request";
		else
			return "ReplicaSetMessage: Key[" + messageKey
					+ "] reply with ReplicaSet = " + replicaSet;
	}
	
	/**
	 * @return Returns the maxRank.
	 */
	public int getMaxRank() {
		return maxRank;
	}
	
	/**
	 * @param maxRank
	 *            The maxRank to set.
	 */
	public void setMaxRank(int maxRank) {
		this.maxRank = maxRank;
	}
}